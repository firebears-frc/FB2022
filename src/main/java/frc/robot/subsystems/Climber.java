package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;



public class Climber extends SubsystemBase {
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;
    private final RelativeEncoder encoder;

    public Climber() {
        leftMotor = new CANSparkMax(6, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        leftMotor.setInverted(false);
        leftMotor.setIdleMode(IdleMode.kCoast);
        encoder = leftMotor.getEncoder();

        rightMotor = new CANSparkMax(7, MotorType.kBrushless);

        rightMotor.restoreFactoryDefaults();
        rightMotor.setInverted(false);
        rightMotor.setIdleMode(IdleMode.kCoast);
        rightMotor.follow(leftMotor);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    public void extend(double position){
    
    }

    public double getEncoderPosition(){
        return 0.0;
    }

    public void resetEncoder(){
        encoder.setPosition(0);
    }

    public void reachOut(){

    }

    public void reachBack(){

    }
}