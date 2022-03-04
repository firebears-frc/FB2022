package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;
import frc.robot.util.SparkEncoder;
import frc.robot.util.SparkMotor;

import com.revrobotics.CANSparkMax.IdleMode;

import static frc.robot.Constants.*;

import java.util.function.DoubleSupplier;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends SubsystemBase {
    private SparkMotor leftMotor;
    private SparkMotor rightMotor;
    private final RelativeEncoder encoder;
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    private PIDSparkMotor pidMotor;

    public Climber() {
        leftMotor = new SparkMotor(CLIMBER_LEFT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("leftMotor(" + CLIMBER_LEFT_MOTOR_CAN_ID + ")", leftMotor);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);
        encoder = leftMotor.getEncoder();
        addChild("encoder", new SparkEncoder(encoder));

        pidMotor = new PIDSparkMotor(leftMotor, CLIMBER_P, CLIMBER_I, CLIMBER_D);

        rightMotor = new SparkMotor(CLIMBER_RIGHT_MOTOR_CAN_ID, MotorType.kBrushless);
        addChild("rightMotor(" + CLIMBER_RIGHT_MOTOR_CAN_ID + ")", rightMotor);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);

        if (PRACTICE_ROBOT) {
            leftSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 4, 3);
            rightSolenoid = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 5);
        } else {
            leftSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.REVPH, 3, 4);
            rightSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.REVPH, 13, 12);
        }

        addChild("leftSolenoid", leftSolenoid);
        addChild("rightSolenoid", rightSolenoid);

        rightMotor.follow(leftMotor);

    }

    @Override
    public void periodic() {   
        SmartDashboard.putNumber("ClimberPosition", encoder.getPosition());
    
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

    /** Reach arm out to the side. */
    //kReverse and kForward are mismatched here due to reverse
    public void reachOutToSide() {
        leftSolenoid.set(Value.kReverse);
        rightSolenoid.set(Value.kForward);
    }

    /** Move arm to the vertical */
    public void reachBackVertical() {
        leftSolenoid.set(Value.kForward);
        rightSolenoid.set(Value.kReverse);

    }

    public void driveClimbers(double speed){
        leftMotor.set(speed);
    }
}