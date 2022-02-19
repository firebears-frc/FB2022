package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter extends SubsystemBase {
    private CANSparkMax shooterMotor;
    private CANSparkMax turretMotor;

    public Shooter() {
        shooterMotor = new CANSparkMax(11, MotorType.kBrushless);

        shooterMotor.restoreFactoryDefaults();
        shooterMotor.setInverted(false);
        shooterMotor.setIdleMode(IdleMode.kCoast);

        turretMotor = new CANSparkMax(12, MotorType.kBrushless);

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