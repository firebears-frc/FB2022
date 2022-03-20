package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;
import frc.robot.util.SparkMotor;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
    private SparkMotor shooterMotor;
    private SparkMotor turretMotor;
    private PIDSparkMotor pidTurretMotor;
    private PIDSparkMotor pidShooterMotor;

    private SparkEncoder turretEncoder;
    private SparkEncoder shooterEncoder;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;

    private DigitalInput leftLimitSwitch, rightLimitSwitch;

    private boolean isLowered = false;

    public Shooter() {
        shooterMotor = new SparkMotor(SHOOTER_SHOOTER_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("spinnerMotor(" + SHOOTER_SHOOTER_MOTOR_CAN_ID + ")", shooterMotor);

        shooterMotor.restoreFactoryDefaults();
        shooterMotor.setInverted(false);
        shooterMotor.setIdleMode(IdleMode.kCoast);
        shooterEncoder = new SparkEncoder(shooterMotor.getEncoder());
        addChild("spinnerEncoder", shooterEncoder);

        pidShooterMotor = new PIDSparkMotor(shooterMotor, SHOOTER_WHEEL_P, SHOOTER_WHEEL_I, SHOOTER_WHEEL_D);

        turretMotor = new SparkMotor(SHOOTER_TURRET_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("turretMotor(" + SHOOTER_TURRET_MOTOR_CAN_ID + ")", turretMotor);

        turretMotor.restoreFactoryDefaults();
        turretMotor.setInverted(false);
        turretMotor.setIdleMode(IdleMode.kCoast);

        turretEncoder = new SparkEncoder(turretMotor.getEncoder());
        addChild("encoder", turretEncoder);

        pidTurretMotor = new PIDSparkMotor(turretMotor, SHOOTER_TURRET_P, SHOOTER_TURRET_I, SHOOTER_TURRET_D);

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(1, CTREPCM, 2, 3);
            rightSolenoid = new DoubleSolenoid(1, CTREPCM, 1, 0);
        } else {
            leftSolenoid = new DoubleSolenoid(1, REVPH, 5, 6);
            rightSolenoid = new DoubleSolenoid(1, REVPH, 11, 10);
        }
        addChild("leftSolenoid", leftSolenoid);
        addChild("rightSolenoid", rightSolenoid);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("shooter velocity", shooterEncoder.getVelocity());
        /*
         * if (leftLimitSwitch.get()) {
         * turretMotor.setIdleMode(IdleMode.kBrake);
         * pidTurretMotor.set(0);
         * } else if (rightLimitSwitch.get()) {
         * turretMotor.setIdleMode(IdleMode.kBrake);
         * pidTurretMotor.set(0);
         * } else {
         * turretMotor.setIdleMode(IdleMode.kCoast);
         * }
         */
    }

    @Override
    public void simulationPeriodic() {

    }

    /**
     * Push the ball into the spinning wheel
     */
    public void extendPusher() {
        leftSolenoid.set(kForward);
        rightSolenoid.set(kReverse);
        isLowered = false;
    }

    /**
     * Lower the ball pusher.
     */
    public void retractPusher() {
        leftSolenoid.set(kReverse);
        rightSolenoid.set(kForward);
        isLowered = true;
    }

    /** @return whether the ball pusher is in the low position. */
    public boolean isLowered() {
        return isLowered;
    }

    public void setShooterVelocity(double velocity) {
        pidShooterMotor.set(velocity);
    }

    public void setShooterOutput(double output) {
        shooterMotor.set(output);
    }

    public double getShooterVelocity() {
        return shooterEncoder.getVelocity();
    }

    /**
     * Set shooter wheel to a minimal speed.
     */
    public void idleShooter() {
        pidShooterMotor.set(0.1);
    }

    /**
     * Stop shooter wheel completely.
     */
    public void stop() {
        pidShooterMotor.set(0);
    }

    /**
     * Reset encoder to zero.
     */
    public void resetTurretEncoder() {
        turretEncoder.resetEncoder();
    }

    public void setTurretPosition(double position) {
        pidTurretMotor.driveToPosition(position);
    }

    public double getTurretPosition() {
        return turretEncoder.getPosition();
    }

    public double getMaxShooterRPM() {
        return pidShooterMotor.getMaxRPM();
    }

}