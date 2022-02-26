package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;

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

    public Chassis() {
        frontLeftMotor = new CANSparkMax(CHASSIS_FRONT_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontLeftMotor.restoreFactoryDefaults();
        frontLeftMotor.setInverted(true);
        frontLeftMotor.setIdleMode(IdleMode.kCoast);
        frontLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        leftPIDSparkMotor = new PIDSparkMotor(frontLeftMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        leftEncoder = frontLeftMotor.getEncoder();
        addChild("leftEncoder", new SparkEncoder(leftEncoder));

        frontRightMotor = new CANSparkMax(CHASSIS_FRONT_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontRightMotor.restoreFactoryDefaults();
        frontRightMotor.setInverted(true);
        frontRightMotor.setIdleMode(IdleMode.kCoast);
        frontRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        righEncoder = frontRightMotor.getEncoder();
        addChild("righEncoder", new SparkEncoder(righEncoder));

        rightPIDSparkMotor = new PIDSparkMotor(frontRightMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);

        rearLeftMotor = new CANSparkMax(CHASSIS_REAR_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearLeftMotor.restoreFactoryDefaults();
        rearLeftMotor.setInverted(true);
        rearLeftMotor.setIdleMode(IdleMode.kCoast);
        rearLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        rearLeftMotor.follow(frontLeftMotor);

        rearRightMotor = new CANSparkMax(CHASSIS_REAR_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearRightMotor.restoreFactoryDefaults();
        rearRightMotor.setInverted(true);
        rearRightMotor.setIdleMode(IdleMode.kCoast);
        rearRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);

        rearRightMotor.follow(frontRightMotor);

        leftPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);
        rightPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);

        differentialDrive = new DifferentialDrive(leftPIDSparkMotor, rightPIDSparkMotor);
        addChild("differentialDrive", differentialDrive);
    }

    @Override
    public void periodic() {
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
        leftEncoder.setPosition(0);
        righEncoder.setPosition(0);

    }

    /**
     * @return distance in inches.
     */
    public double getEncoderDistance() {
        return (leftEncoder.getPosition() + righEncoder.getPosition()) / 2;
    }
}