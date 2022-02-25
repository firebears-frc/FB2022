package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;
import frc.robot.util.SparkMotor;

import com.revrobotics.CANSparkMax.IdleMode;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.*;

public class Climber extends SubsystemBase {
    private SparkMotor leftMotor;
    private SparkMotor rightMotor;
    private final RelativeEncoder encoder;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private PIDSparkMotor pidMotor;

    public Climber() {
        leftMotor = new SparkMotor(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("leftMotor", leftMotor);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);
        encoder = leftMotor.getEncoder();
        addChild("encoder", new SparkEncoder(encoder));

        pidMotor = new PIDSparkMotor(leftMotor, CLIMBER_P, CLIMBER_I, CLIMBER_D);

        rightMotor = new SparkMotor(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("rightMotor", rightMotor);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightMotor.follow(leftMotor);

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 4, 3);
            rightSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 5);
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