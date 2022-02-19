package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
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
}