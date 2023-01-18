package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;

import static frc.robot.Constants.*;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class Chassis extends SubsystemBase implements LoggableInputs {
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
    private boolean brakeMode = false;

    private AHRS navXboard;
    private DifferentialDriveOdometry odometry;
    public final Field2d field2d = new Field2d();
    private final PowerDistribution m_powerDistribution;

    public Chassis(PowerDistribution powerDistribution) {
        ultrasonic = new AnalogPotentiometer(0, 100, 0);
        frontLeftMotor = new CANSparkMax(CHASSIS_FRONT_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontLeftMotor.restoreFactoryDefaults();
        frontLeftMotor.setInverted(false);
        frontLeftMotor.setIdleMode(IdleMode.kBrake);
        frontLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        frontLeftMotor.burnFlash();

        leftPIDSparkMotor = new PIDSparkMotor(frontLeftMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        leftPIDSparkMotor.setSecondaryPID(CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        leftEncoder = frontLeftMotor.getEncoder();
        addChild("leftEncoder", new SparkEncoder(leftEncoder));

        frontRightMotor = new CANSparkMax(CHASSIS_FRONT_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        frontRightMotor.restoreFactoryDefaults();
        frontRightMotor.setInverted(true);
        frontRightMotor.setIdleMode(IdleMode.kBrake);
        frontRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        frontRightMotor.burnFlash();
        righEncoder = frontRightMotor.getEncoder();
        addChild("righEncoder", new SparkEncoder(righEncoder));

        rightPIDSparkMotor = new PIDSparkMotor(frontRightMotor, CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
        rightPIDSparkMotor.setSecondaryPID(CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);

        rearLeftMotor = new CANSparkMax(CHASSIS_REAR_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearLeftMotor.restoreFactoryDefaults();
        rearLeftMotor.setInverted(false);
        rearLeftMotor.setIdleMode(IdleMode.kBrake);
        rearLeftMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        rearLeftMotor.burnFlash();

        rearLeftMotor.follow(frontLeftMotor);

        rearRightMotor = new CANSparkMax(CHASSIS_REAR_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        rearRightMotor.restoreFactoryDefaults();
        rearRightMotor.setInverted(true);
        rearRightMotor.setIdleMode(IdleMode.kBrake);
        rearRightMotor.setSmartCurrentLimit(CHASSIS_STALL_CURRENT_LIMIT, CHASSIS_FREE_CURRENT_LIMIT);
        rearRightMotor.burnFlash();

        rearRightMotor.follow(frontRightMotor);

        leftPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);
        rightPIDSparkMotor.setClosedLoop(CHASSIS_CLOSED_LOOP_DRIVING);

        differentialDrive = new DifferentialDrive(leftPIDSparkMotor, rightPIDSparkMotor);
        addChild("differentialDrive", differentialDrive);

        if (NAVX_ENABLED) {
            try {
                navXboard = new AHRS(edu.wpi.first.wpilibj.SerialPort.Port.kUSB);
            } catch (final RuntimeException ex) {
                DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
            }
        }

        odometry = new DifferentialDriveOdometry(getGyroAngle(), 0.0, 0.0);

        m_powerDistribution = powerDistribution;

    }

    /** @return current angle in radians taken from the NavX board. */
    public Rotation2d getGyroAngle() {
        if (navXboard == null) {
            return Rotation2d.fromRadians(0.0);
        }
        double gyroAngle = Units.degreesToRadians(navXboard.getAngle());
        return Rotation2d.fromRadians(gyroAngle);
    }

    /** @return current position of the robot in meters and radians. */
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    /** @param pose current position of the robot in meters and radians. */
    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(getGyroAngle(), 0.0, 0.0, pose);
    }

    @Override
    public void periodic() {
        double leftDistance = Units.inchesToMeters(leftEncoder.getPosition() * 2.3);
        double rightDistance = Units.inchesToMeters(righEncoder.getPosition() * 2.3);
        Pose2d currentPose = odometry.update(getGyroAngle(), leftDistance, rightDistance);
        field2d.setRobotPose(getPose());

        if (DEBUG) {
            SmartDashboard.putNumber("ultrasonic", getUltrasonicDistanceInches());
            SmartDashboard.putNumber("getDistance", getEncoderDistance());
            SmartDashboard.putBoolean("Voltage", m_powerDistribution.getVoltage() > 11);
            if (navXboard != null) {
                SmartDashboard.putString("Pose2d", currentPose.toString());
            }
        }
        if (LOGGING && navXboard != null) {
            Logger.getInstance().recordOutput("Chassis/pose", currentPose);
        }
    }

    @Override
    public void simulationPeriodic() {
    }

    public void arcadeDrive(double speed, double rotation) {
        differentialDrive.arcadeDrive(speed * -1, rotation);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftPIDSparkMotor.setVoltage(leftVolts);
        rightPIDSparkMotor.setVoltage(rightVolts);
        differentialDrive.feed();
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        double leftVelocity = Units.inchesToMeters(leftEncoder.getVelocity() * 2.3);
        double rightVelocity = Units.inchesToMeters(righEncoder.getVelocity() * 2.3);
        return new DifferentialDriveWheelSpeeds(leftVelocity, rightVelocity);
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
        double inches = 2.3 * ((leftEncoder.getPosition() - leftOffSet) + (righEncoder.getPosition() - rightOffSet))
                / 2;
        inches = inches * -1;

        return inches;
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
        if (brake == brakeMode) {
            // do nothing
        } else if (brake) {
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
        brakeMode = brake;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("Chassis/frontLeftMotor", frontLeftMotor.get());
        table.put("Chassis/frontRightMotor", frontRightMotor.get());
        table.put("Chassis/rearLeftMotor", rearLeftMotor.get());
        table.put("Chassis/rearRightMotor", rearRightMotor.get());
        table.put("Chassis/brakeMode", brakeMode);
    }

    @Override
    public void fromLog(LogTable table) {
        frontLeftMotor.set(table.getDouble("Chassis/frontLeftMotor", 0.0));
        frontRightMotor.set(table.getDouble("Chassis/frontRightMotor", 0.0));
        rearLeftMotor.set(table.getDouble("Chassis/rearLeftMotor", 0.0));
        rearRightMotor.set(table.getDouble("Chassis/rearRightMotor", 0.0));
        setBrake(table.getBoolean("Chassis/brakeMode", false));

    }
}