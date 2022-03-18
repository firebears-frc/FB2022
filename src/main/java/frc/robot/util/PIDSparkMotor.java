package frc.robot.util;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;

/**
 * Wrapper around {@code CANSparkMax} allowing us to do closed-loop driving.
 */
public class PIDSparkMotor implements MotorController {

	public final double MAX_RPM = 8000.0;
	public final double ENCODER_TICKS_PER_INCH;
	public static final int SECONDARY_SLOT = 1;

	private final CANSparkMax motor;
	private final SparkMaxPIDController pidController;
	private final RelativeEncoder encoder;
	private boolean closedLoop = true;
	private boolean invertEncoder = false;
	private double currentSpeed = 0.0;
	private double maxEncoderVelocity = 0.0;
	private double encoderOffset = 0.0;

	public PIDSparkMotor(CANSparkMax m, double kP, double kI, double kD) {
		this(m, kP, kI, kD, 0.4449);
	}

	public PIDSparkMotor(CANSparkMax m, double kP, double kI, double kD, double encoderTicksPerInch) {
		motor = m;
		pidController = motor.getPIDController();
		pidController.setP(kP);
		pidController.setI(kI);
		pidController.setD(kD);
		ENCODER_TICKS_PER_INCH = encoderTicksPerInch;
		encoder = motor.getEncoder();
	}

	public void setSecondaryPID(double kP, double kI, double kD) {
		pidController.setP(kP, SECONDARY_SLOT);
		pidController.setI(kI, SECONDARY_SLOT);
		pidController.setD(kD, SECONDARY_SLOT);
		pidController.setOutputRange(-1.0, 1.0, SECONDARY_SLOT);
	}

	public void setClosedLoop(boolean b) {
		closedLoop = b;
	}

	public boolean isClosedLoop() {
		return closedLoop;
	}

	public void setInvertEncoder(boolean b) {
		invertEncoder = b;
	}

	public void resetEncoder() {
		encoderOffset = encoder.getPosition();
	}

	public double inchesTraveled() {
		double encoderPosition = encoder.getPosition() - encoderOffset;
		return (invertEncoder ? -1 : 1) * encoderPosition / ENCODER_TICKS_PER_INCH;
	}

	public void driveToPosition(double inches) {
		double setPointPosition = (invertEncoder ? -1 : 1) * inches * ENCODER_TICKS_PER_INCH;
		if (pidController.setReference(setPointPosition, ControlType.kPosition, SECONDARY_SLOT) != REVLibError.kOk) {
			System.out.println("Error in setting " + this);
		} else {
			System.out.println("Inches " + this);
		}
	}

	/**
	 * Common interface for setting the speed of a speed controller.
	 *
	 * @param speed The speed to set. Value should be between -1.0 and 1.0.
	 */
	@Override
	public void set(double speed) {
		currentSpeed = speed;
		if (closedLoop) {
			double setPointVelocity = speed * MAX_RPM;
			if (pidController.setReference(setPointVelocity, ControlType.kVelocity) != REVLibError.kOk) {
				System.out.println("ERROR: Failed to set setpoint on " + this);
			}
		} else {
			motor.set(speed);
			maxEncoderVelocity = Math.max(maxEncoderVelocity, Math.abs(encoder.getVelocity()));
		}
	}

	/**
	 * Common interface for getting the current set speed of a speed controller.
	 *
	 * @return The current set speed. Value is between -1.0 and 1.0.
	 */
	@Override
	public double get() {
		if (closedLoop) {
			return currentSpeed;
		} else {
			return motor.get();
		}
	}

	/**
	 * Common interface for inverting direction of a speed controller.
	 *
	 * @param isInverted The state of inversion true is inverted.
	 */
	@Override
	public void setInverted(boolean isInverted) {
		motor.setInverted(isInverted);
	}

	/**
	 * Common interface for returning if a speed controller is in the inverted state
	 * or not.
	 *
	 * @return isInverted The state of the inversion true is inverted.
	 */
	@Override
	public boolean getInverted() {
		return motor.getInverted();
	}

	/**
	 * Disable the speed controller.
	 */
	@Override
	public void disable() {
		motor.disable();
	}

	/**
	 * Stops motor movement. Motor can be moved again by calling set without having
	 * to re-enable the motor.
	 */
	@Override
	public void stopMotor() {
		motor.stopMotor();
	}

	@Override
	public String toString() {
		return "PIDSparkMotor(" + motor.getDeviceId() + ")";
	}

	public double getMaxEncoderVelocity() {
		return maxEncoderVelocity;
	}

	public double getMaxRPM() {
		return MAX_RPM;
	}
}
