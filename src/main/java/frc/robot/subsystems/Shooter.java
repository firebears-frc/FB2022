package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SparkMotor;

import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
    private CANSparkMax shooterMotor;
    private CANSparkMax turretMotor;

    public Shooter() {
        shooterMotor = new CANSparkMax(SHOOTER_SHOOTER_MOTOR_CAN_ID, MotorType.kBrushless);

        shooterMotor.restoreFactoryDefaults();
        shooterMotor.setInverted(false);
        shooterMotor.setIdleMode(IdleMode.kCoast);

        turretMotor = new CANSparkMax(SHOOTER_TURRET_MOTOR_CAN_ID, MotorType.kBrushless);

        turretMotor.restoreFactoryDefaults();
        turretMotor.setInverted(false);
        turretMotor.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    /**
     * Push the ball into the spinning wheel
     */
    public void extendPusher() {

    }

    /**
     * Lower the ball pusher.
     */
    public void retractPusher() {

    }

    public void setShooterVelocity(double velocity) {

    }

    public double getShooterVelocity() {
        return 0.0;
    }

    /**
     * Set shooter wheel to a minimal speed.
     */
    public void idleShooter() {

    }

    /**
     * Stop shooter wheel completely.
     */
    public void stop() {

    }

    /**
     * Reset encoder to zero.
     */
    public void resetTurretEncoder() {

    }

    public void setTurretPosition(double position) {

    }

    public double getTurretPosition() {
        return 0.0;
    }

}