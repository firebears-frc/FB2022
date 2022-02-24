package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDSparkMotor;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;



public class Climber extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private final RelativeEncoder encoder;
    private Solenoid solenoid;
    private PIDSparkMotor pidMotor;

    public Climber() {
        leftMotor = new CANSparkMax(6, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);
        encoder = leftMotor.getEncoder();
        
        pidMotor = new PIDSparkMotor(leftMotor, 0, 0, 0);

        rightMotor = new CANSparkMax(7, MotorType.kBrushless);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightMotor.follow(leftMotor);

        solenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 6);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    public void extend(double position){
    pidMotor.driveToPosition(position);
    }

    public double getEncoderPosition(){
        return encoder.getPosition();
    }

    public void resetEncoder(){
        encoder.setPosition(0);
    }

    public void reachOut(){
        solenoid.set(true);
    }

    public void reachBack(){
        solenoid.set(false);

    }
}