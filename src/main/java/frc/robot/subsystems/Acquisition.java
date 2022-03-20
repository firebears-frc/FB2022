package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

public class Acquisition extends SubsystemBase {

    private WPI_TalonSRX spinMotor;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private boolean isLowered = false;

    public Acquisition() {
        spinMotor = new WPI_TalonSRX(ACQUISITION_SPIN_MOTOR_CAN_ID);
        spinMotor.configFactoryDefault();
        spinMotor.configContinuousCurrentLimit(ACQUSITION_SPIN_MOTOR_CURRENT_LIMIT);
        spinMotor.configPeakCurrentLimit(0);
        spinMotor.enableCurrentLimit(true);
        spinMotor.setInverted(true);
        addChild("spinMotor(" + ACQUISITION_SPIN_MOTOR_CAN_ID + ")", spinMotor);

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
        if (!PRACTICE_ROBOT) {
            SmartDashboard.putNumber("Pneumatics Pressure", RobotContainer.getInstance().getPneumaticsPressure());
        }
    }

    @Override
    public void simulationPeriodic() {
    }

    public void raise() {
        leftSolenoid.set(kForward);
        rightSolenoid.set(kReverse);
        isLowered = false;
    }

    public void lower() {
        leftSolenoid.set(kReverse);
        rightSolenoid.set(kForward);
        isLowered = true;
    }

    /** @return whether the Acquisition is lowered to pick up cargo. */
    public boolean isLowered() {
        return isLowered;
    }

    public void spin(double speed) {
        spinMotor.set(speed);
    }

    public void stop() {
        spinMotor.set(0);
    }
}
