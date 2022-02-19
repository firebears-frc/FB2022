package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import static frc.robot.Constants.*;

public class Acquisition extends SubsystemBase {

    private CANSparkMax aquisitionMotor;

    public Acquisition() {
        aquisitionMotor = new CANSparkMax(ACQUISITION_MOTOR_CAN_ID, MotorType.kBrushless);
        aquisitionMotor.restoreFactoryDefaults();
        aquisitionMotor.setInverted(false);
        aquisitionMotor.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }
}