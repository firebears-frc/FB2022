package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SparkMotor;

import static frc.robot.Constants.*;

public class Acquisition extends SubsystemBase {

    private CANSparkMax aquisitionMotor;
    private WPI_TalonSRX spinMotor;

    public Acquisition() {
        aquisitionMotor = new CANSparkMax(ACQUISITION_LOWER_MOTOR_CAN_ID, MotorType.kBrushless);
        aquisitionMotor.restoreFactoryDefaults();
        aquisitionMotor.setInverted(false);
        aquisitionMotor.setIdleMode(IdleMode.kCoast);
        aquisitionMotor.setSmartCurrentLimit(ACQUSITION_LOWER_MOTOR_CURRENT_LIMIT);

        spinMotor = new WPI_TalonSRX(ACQUISITION_SPIN_MOTOR_CAN_ID);
        spinMotor.configContinuousCurrentLimit(ACQUSITION_SPIN_MOTOR_CURRENT_LIMIT);
        spinMotor.configPeakCurrentLimit(0);
        spinMotor.enableCurrentLimit(true);
        addChild("spinMotor", spinMotor);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    public void raise() {

    }

    public void lower() {

    }

    public void spin(double speed) {

    }

    public void stop() {

    }
}
