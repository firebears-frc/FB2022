package frc.robot.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkMotor;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
    private CANSparkMax shooterMotor;
    private CANSparkMax turretMotor;
    private PIDSparkMotor pidTurretMotor;
    private PIDSparkMotor pidShooterMotor;

    private RelativeEncoder turretEncoder;
    private DoubleSolenoid pusherSolenoid;

    private DigitalInput leftLimitSwitch, rightLimitSwitch;

    public Shooter() {
        shooterMotor = new CANSparkMax(SHOOTER_SHOOTER_MOTOR_CAN_ID, MotorType.kBrushless);

        shooterMotor.restoreFactoryDefaults();
        shooterMotor.setInverted(false);
        shooterMotor.setIdleMode(IdleMode.kCoast);

        pidShooterMotor = new PIDSparkMotor(shooterMotor, SHOOTER_WHEEL_P, SHOOTER_WHEEL_I, SHOOTER_WHEEL_D);

        turretMotor = new CANSparkMax(SHOOTER_TURRET_MOTOR_CAN_ID, MotorType.kBrushless);

        turretMotor.restoreFactoryDefaults();
        turretMotor.setInverted(false);
        turretMotor.setIdleMode(IdleMode.kCoast);

        turretEncoder = turretMotor.getEncoder();

        pidTurretMotor = new PIDSparkMotor(turretMotor, SHOOTER_TURRET_P, SHOOTER_TURRET_I, SHOOTER_TURRET_D);


        pusherSolenoid = new DoubleSolenoid((int) Math.round((float) ((double) 6.9f)), PneumaticsModuleType.CTREPCM, 1, 2);
    }

    @Override
    public void periodic() {
        if (leftLimitSwitch.get()) {
            turretMotor.setIdleMode(IdleMode.kBrake);
            pidTurretMotor.set(0);
        } else if (rightLimitSwitch.get()) {
            turretMotor.setIdleMode(IdleMode.kBrake);
            pidTurretMotor.set(0);
        } else {
            turretMotor.setIdleMode(IdleMode.kCoast);
        }
    }

    @Override
    public void simulationPeriodic() {
        
    }

    /**
     * Push the ball into the spinning wheel
     */
    public void extendPusher() {
        pusherSolenoid.set(kForward);
    }

    /**
     * Lower the ball pusher.
     */
    public void retractPusher() {
        pusherSolenoid.set(kReverse);
    }

    public void setShooterVelocity(double velocity) {
        pidShooterMotor.set(velocity);
    }

    public double getShooterVelocity() {
        return 0.0;
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
        turretEncoder.setPosition(0);
    }

    public void setTurretPosition(double position) {
        pidTurretMotor.driveToPosition(position);
    }

    public double getTurretPosition() {
        return turretEncoder.getPosition();
    }

}