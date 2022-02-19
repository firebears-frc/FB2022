package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Chassis extends SubsystemBase {
    private CANSparkMax frontLeftMotor;
    private CANSparkMax frontRightMotor;
    private CANSparkMax rearLeftMotor;
    private CANSparkMax rearRightMotor;

    public Chassis() {
        frontLeftMotor = new CANSparkMax(2, MotorType.kBrushless);

        frontLeftMotor.restoreFactoryDefaults();
        frontLeftMotor.setInverted(false);
        frontLeftMotor.setIdleMode(IdleMode.kCoast);

        frontRightMotor = new CANSparkMax(3, MotorType.kBrushless);

        frontRightMotor.restoreFactoryDefaults();
        frontRightMotor.setInverted(false);
        frontRightMotor.setIdleMode(IdleMode.kCoast);

        rearLeftMotor = new CANSparkMax(4, MotorType.kBrushless);

        rearLeftMotor.restoreFactoryDefaults();
        rearLeftMotor.setInverted(false);
        rearLeftMotor.setIdleMode(IdleMode.kCoast);

        rearRightMotor = new CANSparkMax(5, MotorType.kBrushless);

        rearRightMotor.restoreFactoryDefaults();
        rearRightMotor.setInverted(false);
        rearRightMotor.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }
}