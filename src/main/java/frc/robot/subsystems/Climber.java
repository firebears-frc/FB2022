package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SparkMotor;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
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

    /**
     * Extend the arm to a given encoder position.
     * 
     * @param position
     */
    public void extend(double position) {

    }

    /**
     * Return the current extended aarm positoin.
     */
    public double getEncoderPosition() {
        return 0.0;
    }

    /**
     * Reset the encoder to zero.
     */
    public void resetEncoder() {

    }

    /**
     * Extend the arms out away from the robot.
     */
    public void reachOut() {

    }

    /**
     * Pull the arms back into a vertical position.
     */
    public void reachBack() {

    }
}