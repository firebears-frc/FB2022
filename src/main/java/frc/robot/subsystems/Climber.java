package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVLibError;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SparkEncoder;
import frc.robot.util.SparkMotor;

import static frc.robot.Constants.*;

public class Climber extends SubsystemBase {

    public static final double ENCODER_TICKS_PER_INCH = 3.2;

    private double m_setpoint;

    private SparkMotor leftMotor;
    private SparkMotor rightMotor;
    private SparkMaxPIDController pidController;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private SparkEncoder encoder;
    private SparkMaxLimitSwitch upperLimitSwitch;
    private SparkMaxLimitSwitch lowerLimitSwitch;

    private boolean isVertical = true;

    public Climber() {
        leftMotor = new SparkMotor(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("leftMotor(" + CLIMBER_LEFT_MOTOR_CAN_ID + ")", leftMotor);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);

        pidController = leftMotor.getPIDController();
        pidController.setP(CLIMBER_P);
        pidController.setI(CLIMBER_I);
        pidController.setD(CLIMBER_D);
        pidController.setFF(CLIMBER_F);

        rightMotor = new SparkMotor(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        // addChild("rightMotor(" + CLIMBER_RIGHT_MOTOR_CAN_ID + ")", rightMotor);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightMotor.follow(leftMotor);

        m_setpoint = 0;

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
    }

    @Override
    public void periodic() {
        if (DEBUG) {
            SmartDashboard.putNumber("ClimberPosition", this.getEncoderPosition());
            SmartDashboard.putBoolean("ClimberUpperLimit", upperLimitSwitch.isPressed());
            SmartDashboard.putBoolean("ClimberLowerLimit", lowerLimitSwitch.isPressed());
            SmartDashboard.putNumber("ClimberPositionTicks", encoder.getPosition());
            System.out.println("Setpoint: " + m_setpoint + " Encoder Position: " + encoder.getRawEncoderPosition());

        }
        if (lowerLimitSwitch.isPressed()) {
            resetEncoder();
        }
    }

    @Override
    public void simulationPeriodic() {
    }

    /** Extend Climber arms to a position in inches. */
    public void extend(double inches) {
        double setPointTicks = encoder.getOffset() - (inches * ENCODER_TICKS_PER_INCH);
        m_setpoint = setPointTicks;
        REVLibError err = pidController.setReference(setPointTicks, ControlType.kPosition);
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
}