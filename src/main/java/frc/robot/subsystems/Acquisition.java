package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

public class Acquisition extends SubsystemBase {

    private WPI_TalonSRX spinMotor;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;

    public Acquisition() {
        spinMotor = new WPI_TalonSRX(ACQUISITION_SPIN_MOTOR_CAN_ID);
        spinMotor.configContinuousCurrentLimit(ACQUSITION_SPIN_MOTOR_CURRENT_LIMIT);
        spinMotor.configPeakCurrentLimit(0);
        spinMotor.enableCurrentLimit(true);
        addChild("spinMotor", spinMotor);

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(0, CTREPCM, 2, 1);
            rightSolenoid = new DoubleSolenoid(0, CTREPCM, 6, 7);
        } else {
            leftSolenoid = new DoubleSolenoid(1, REVPH, 2, 1);
            rightSolenoid = new DoubleSolenoid(1, REVPH, 9, 8);
        }
        addChild("leftSolenoid", leftSolenoid);
        addChild("rightSolenoid", rightSolenoid);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    public void raise() {
        leftSolenoid.set(kForward);
        rightSolenoid.set(kForward);
    }

    public void lower() {
        leftSolenoid.set(kReverse);
        rightSolenoid.set(kReverse);
    }

    public void spin(double speed) {
        spinMotor.set(speed);
    }

    public void stop() {
        spinMotor.set(0);
    }
}
