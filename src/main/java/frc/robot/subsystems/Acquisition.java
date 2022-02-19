package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import static frc.robot.Constants.*;



public class Acquisition extends SubsystemBase {

    private CANSparkMax aquisitionMotor;
    private TalonSRX spinMotor;

    public Acquisition() {
        aquisitionMotor = new CANSparkMax(ACQUISITION_LOWER_MOTOR_CAN_ID, MotorType.kBrushless);
        aquisitionMotor.restoreFactoryDefaults();
        aquisitionMotor.setInverted(false);
        aquisitionMotor.setIdleMode(IdleMode.kCoast);
        aquisitionMotor.setSmartCurrentLimit(ACQUSITION_LOWER_MOTOR_CURRENT_LIMIT);

        spinMotor = new TalonSRX(ACQUISITION_SPIN_MOTOR_CAN_ID);
        spinMotor.configContinuousCurrentLimit(ACQUSITION_SPIN_MOTOR_CURRENT_LIMIT);
        spinMotor.configPeakCurrentLimit(0);
        spinMotor.enableCurrentLimit(true);
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }

    public void raise(){

    } 

    public void lower(){

    }

    public void spin(double speed){

    }

    public void stop(){
        
    }
}

