package frc.robot.subsystems;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
<<<<<<< HEAD
=======

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.revrobotics.REVLibError;
>>>>>>> 7fc3df1399d0b14c1dfbecdb0c0c1acb98d9a692
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

<<<<<<< HEAD
=======
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
>>>>>>> 7fc3df1399d0b14c1dfbecdb0c0c1acb98d9a692
public class Chassis extends SubsystemBase {
    private CANSparkMax frontLeftMotor;
    private CANSparkMax frontRightMotor;
    private CANSparkMax rearLeftMotor;
    private CANSparkMax rearRightMotor;

    public Chassis() {
        frontLeftMotor = new CANSparkMax(2, MotorType.kBrushless);

        frontLeftMotor.restoreFactoryDefaults();
        frontLeftMotor.setInverted(false);
        frontLeftMotor.setIdleMode(IdleMode.kCoast);

        frontRightMotor = new CANSparkMax(3, MotorType.kBrushless);

        frontRightMotor.restoreFactoryDefaults();
        frontRightMotor.setInverted(false);
        frontRightMotor.setIdleMode(IdleMode.kCoast);

        rearLeftMotor = new CANSparkMax(4, MotorType.kBrushless);

<<<<<<< HEAD
        rearLeftMotor.restoreFactoryDefaults();
        rearLeftMotor.setInverted(false);
        rearLeftMotor.setIdleMode(IdleMode.kCoast);

        rearRightMotor = new CANSparkMax(5, MotorType.kBrushless);

        rearRightMotor.restoreFactoryDefaults();
        rearRightMotor.setInverted(false);
        rearRightMotor.setIdleMode(IdleMode.kCoast);
=======
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
>>>>>>> 7fc3df1399d0b14c1dfbecdb0c0c1acb98d9a692
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
    }
}