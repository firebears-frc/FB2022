package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

public class Acquisition extends SubsystemBase {

    private WPI_TalonSRX spinMotor;

    public Acquisition() {
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
