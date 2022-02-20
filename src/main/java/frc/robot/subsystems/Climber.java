package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SparkMotor;

import static frc.robot.Constants.*;

public class Climber extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;

    public Climber() {
        leftMotor = new CANSparkMax(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);

        rightMotor = new CANSparkMax(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);

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