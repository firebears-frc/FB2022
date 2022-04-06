package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVLibError;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.util.SparkEncoder;
import frc.robot.util.SparkMotor;

import static frc.robot.Constants.*;

public class Climber extends SubsystemBase {

    public static final double ENCODER_TICKS_PER_INCH = 3.2;
    public static final int SAMPLES = 25;
    public static final double SWING_THRESHOLD = 0.4;

    private double m_setpointTicks;

    private SparkMotor leftMotor;
    private SparkMotor rightMotor;
    private SparkMaxPIDController pidController;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private SparkEncoder encoder;
    private SparkMaxLimitSwitch upperLimitSwitch;
    private SparkMaxLimitSwitch lowerLimitSwitch;
    private BuiltInAccelerometer accelerometer;

    private boolean isVertical = true;
    private boolean brakeReleased = false;
    private double swingSum = 0.0;
    private double maxSpeed = CLIMBER_MAX_SPEED;

    public Climber() {
        leftMotor = new SparkMotor(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("leftMotor(" + CLIMBER_LEFT_MOTOR_CAN_ID + ")", leftMotor);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kBrake);
        //leftMotor.setSmartCurrentLimit(CLIMBER_STALL_CURRENT_LIMIT, CLIMBER_FREE_CURRENT_LIMIT);
        leftMotor.burnFlash();

        pidController = leftMotor.getPIDController();
        pidController.setP(CLIMBER_P);
        pidController.setI(CLIMBER_I);
        pidController.setD(CLIMBER_D);
        pidController.setFF(CLIMBER_F);
        pidController.setOutputRange(-1 * maxSpeed, maxSpeed);

        rightMotor = new SparkMotor(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kBrake);
        rightMotor.follow(leftMotor);
        //rightMotor.setSmartCurrentLimit(CLIMBER_STALL_CURRENT_LIMIT, CLIMBER_FREE_CURRENT_LIMIT);
        rightMotor.burnFlash();

        m_setpointTicks = 0;

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 4, 3);
            rightSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 5);
        } else {
            leftSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.REVPH, 3, 4);
            rightSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.REVPH, 13, 12);
        }

        addChild("leftSolenoid", leftSolenoid);
        addChild("rightSolenoid", rightSolenoid);

        encoder = new SparkEncoder(leftMotor.getEncoder());
        addChild("encoder", encoder);
        resetEncoder();

        upperLimitSwitch = leftMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        lowerLimitSwitch = leftMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

        accelerometer = new BuiltInAccelerometer();
        addChild("accelerometer", accelerometer);
    }

    @Override
    public void periodic() {
        double sample = Math.abs(accelerometer.getY());
        double currentAverage = swingSum / SAMPLES;
        swingSum = swingSum + sample - currentAverage;

        if (DEBUG) {
            SmartDashboard.putNumber("ClimberPosition", this.getEncoderPosition());
            SmartDashboard.putBoolean("ClimberUpperLimit", upperLimitSwitch.isPressed());
            SmartDashboard.putBoolean("ClimberLowerLimit", lowerLimitSwitch.isPressed());
            SmartDashboard.putNumber("ClimberPositionTicks", encoder.getPosition());
            SmartDashboard.putNumber("ClimberCurrent", this.getPdpCurrent());
            SmartDashboard.putBoolean("vertical", isVertical());

            SmartDashboard.putBoolean("swinging", isSwinging());
            SmartDashboard.putNumber("swing", currentAverage);
            SmartDashboard.putNumber("getY", Math.abs(accelerometer.getY()));
        }
        if (lowerLimitSwitch.isPressed()) {
            resetEncoder();
            brakeReleased = true;
        }
    }

    @Override
    public void simulationPeriodic() {
    }

    /** Extend Climber arms to a position in inches. */
    public void extend(double inches) {
        m_setpointTicks = encoder.getOffset() - (inches * ENCODER_TICKS_PER_INCH);
        REVLibError err = pidController.setReference(m_setpointTicks, ControlType.kPosition);
        if (err != REVLibError.kOk) {
            System.out.println("Error in Climber: " + err);
        }
    }

    /** @return relative encoder position in inches. */
    public double getEncoderPosition() {
        return -1 * encoder.getPosition() / ENCODER_TICKS_PER_INCH;
    }

    public void resetEncoder() {
        encoder.resetEncoder();
    }

    /** Reach arm out to the side. */
    // kReverse and kForward are mismatched here due to reverse
    public void reachOutToSide() {
        leftSolenoid.set(Value.kReverse);
        rightSolenoid.set(Value.kForward);
        isVertical = false;
    }

    /** Move arm to the vertical. */
    public void reachBackVertical() {
        leftSolenoid.set(Value.kForward);
        rightSolenoid.set(Value.kReverse);
        isVertical = true;
    }

    /** @return whether the climbers are upright in the vertical position. */
    public boolean isVertical() {
        return isVertical;
    }

    public boolean isBrakeReleased() {
        return brakeReleased;
    }

    public void driveClimbers(double speed) {
        leftMotor.set(speed);
    }

    public void setBrake(boolean enabled) {
        if (enabled) {
            rightMotor.setIdleMode(IdleMode.kBrake);
            leftMotor.setIdleMode(IdleMode.kBrake);
        } else {
            rightMotor.setIdleMode(IdleMode.kCoast);
            leftMotor.setIdleMode(IdleMode.kCoast);
        }
    }

    public boolean lowerLimitPressed() {
        return lowerLimitSwitch.isPressed();
    }

    public boolean upperLimitPressed() {
        return upperLimitSwitch.isPressed();
    }

    /** @return the maximum current in amps of the climber motor. */
    public double getPdpCurrent() {
        RobotContainer rc = RobotContainer.getInstance();
        double current = 0;
        if (PRACTICE_ROBOT) {
            current = Math.max(rc.getPdpCurrent(0), rc.getPdpCurrent(2));
        } else {
            current = Math.max(rc.getPdpCurrent(4), rc.getPdpCurrent(5));
        }
        if (current > CLIMBER_MAX_CURRENT) {
            System.err.println("WARNING: CLIMBER CURRENT = " + current);
        }
        return current;
    }

    public boolean isSwinging() {
        double currentAverage = swingSum / SAMPLES;
        return Math.abs(currentAverage) > SWING_THRESHOLD;
    }

    public double getYAcceleration() {
        return accelerometer.getY();
    }
    
    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double max) {
        maxSpeed = max;
    }
}