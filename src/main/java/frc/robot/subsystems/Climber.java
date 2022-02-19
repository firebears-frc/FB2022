package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Climber extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;

    public Climber() {
        leftMotor = new CANSparkMax(6, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);

        rightMotor = new CANSparkMax(7, MotorType.kBrushless);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }
}