package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

import static frc.robot.Constants.*;

public class Chassis extends SubsystemBase {
    private CANSparkMax frontLeftMotor;
    private CANSparkMax frontRightMotor;
    private CANSparkMax rearLeftMotor;
    private CANSparkMax rearRightMotor;
    private PIDSparkMotor leftPIDSparkMotor;
    private PIDSparkMotor rightPIDSparkMotor;
    private DifferentialDrive differentialDrive;
    private RelativeEncoder leftEncoder;
    private RelativeEncoder righEncoder;
    private AnalogPotentiometer ultrasonic;
    private double leftOffSet = 0;
    private double rightOffSet = 0;

    public Chassis() {
        ultrasonic = new AnalogPotentiometer(0, 100, 0);
        frontLeftMotor = new CANSparkMax(CHASSIS_FRONT_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontLeftMotor.restoreFactoryDefaults();
        frontLeftMotor.setInverted(false);
        frontLeftMotor.setIdleMode(IdleMode.kBrake);
        frontLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        leftPIDSparkMotor = new PIDSparkMotor(frontLeftMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        leftPIDSparkMotor.setSecondaryPID(CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        leftEncoder = frontLeftMotor.getEncoder();
        addChild("leftEncoder", new SparkEncoder(leftEncoder));

        frontRightMotor = new CANSparkMax(CHASSIS_FRONT_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontRightMotor.restoreFactoryDefaults();
        frontRightMotor.setInverted(true);
        frontRightMotor.setIdleMode(IdleMode.kBrake);
        frontRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        righEncoder = frontRightMotor.getEncoder();
        addChild("righEncoder", new SparkEncoder(righEncoder));

        rightPIDSparkMotor = new PIDSparkMotor(frontRightMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        rightPIDSparkMotor.setSecondaryPID(CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);


        rearLeftMotor = new CANSparkMax(CHASSIS_REAR_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearLeftMotor.restoreFactoryDefaults();
        rearLeftMotor.setInverted(false);
        rearLeftMotor.setIdleMode(IdleMode.kBrake);
        rearLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        rearLeftMotor.follow(frontLeftMotor);

        rearRightMotor = new CANSparkMax(CHASSIS_REAR_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearRightMotor.restoreFactoryDefaults();
        rearRightMotor.setInverted(true);
        rearRightMotor.setIdleMode(IdleMode.kBrake);
        rearRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        rearRightMotor.follow(frontRightMotor);

        leftPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);
        rightPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);

        differentialDrive = new DifferentialDrive(leftPIDSparkMotor, rightPIDSparkMotor);
        addChild("differentialDrive", differentialDrive);



    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("ultrasonic", getUltrasonicDistanceInches());
        SmartDashboard.putNumber("getDistance", getEncoderDistance());
    }

    @Override
    public void simulationPeriodic() {
    }

    public void arcadeDrive(double speed, double rotation) {
        differentialDrive.arcadeDrive(speed, rotation);
    }

    /**
     * Reset encoder to zero.
     */
    public void resetEncoder() {
        leftOffSet = leftEncoder.getPosition();
        rightOffSet = righEncoder.getPosition();
    }

    /**
     * @return distance in inches.
     */
    public double getEncoderDistance() {
        return 2.3 * ((leftEncoder.getPosition() - leftOffSet) + (righEncoder.getPosition() - rightOffSet)) / 2;
    }

    public double getUltrasonicDistanceInches() {
        return ultrasonic.get() / 0.193;
    }

    public void driveToPosition(double inches) {
        rightPIDSparkMotor.resetEncoder();
        leftPIDSparkMotor.resetEncoder();

        System.out.println(leftPIDSparkMotor.inchesTraveled());
        System.out.println(rightPIDSparkMotor.inchesTraveled());

        
        rightPIDSparkMotor.driveToPosition(inches);
        leftPIDSparkMotor.driveToPosition(inches);
    }

    public void setBrake(Boolean brake) {
        if (brake) {
            frontLeftMotor.setIdleMode(IdleMode.kBrake);
            frontRightMotor.setIdleMode(IdleMode.kBrake);
            rearLeftMotor.setIdleMode(IdleMode.kBrake);
            rearRightMotor.setIdleMode(IdleMode.kBrake);
        } else {
            frontLeftMotor.setIdleMode(IdleMode.kCoast);
            frontRightMotor.setIdleMode(IdleMode.kCoast);
            rearLeftMotor.setIdleMode(IdleMode.kCoast);
            rearRightMotor.setIdleMode(IdleMode.kCoast);
        }
    }

}