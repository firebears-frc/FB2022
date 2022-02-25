package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;

import com.revrobotics.CANSparkMax.IdleMode;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Climber extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private final RelativeEncoder encoder;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private PIDSparkMotor pidMotor;

    public Climber() {
        leftMotor = new CANSparkMax(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);
        encoder = leftMotor.getEncoder();

        pidMotor = new PIDSparkMotor(leftMotor, CLIMBER_P, CLIMBER_I, CLIMBER_D);

        rightMotor = new CANSparkMax(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightMotor.follow(leftMotor);

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 1, 2);
            rightSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 1, 2);
        } else {
            leftSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.REVPH, 1, 2);
            rightSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.REVPH, 1, 2);
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

    public void extend(double position) {
        pidMotor.driveToPosition(position);
    }

    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }

    public void reachOut() {
        leftSolenoid.set(Value.kForward);
        rightSolenoid.set(Value.kForward);
    }

    public void reachBack() {
        leftSolenoid.set(Value.kReverse);
        rightSolenoid.set(Value.kForward);

    }
}