
package frc.robot.commands;

import org.opencv.core.Mat;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

/** Default command to drive the Chassis. */
public class ChassisDriveCommand extends CommandBase {
  private Chassis m_chassis;
  private Joystick joystick;

  /** Default command to drive the Chassis. */
  public ChassisDriveCommand(Chassis c, Joystick j) {
    m_chassis = c;
    joystick = j;
    addRequirements(m_chassis);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    double speed = joystick.getY();
    double rotation = joystick.getZ();
  
    m_chassis.arcadeDrive(speed, rotation * MathUtil.clamp((joystick.getRawAxis(3)+1.0)/2,0,1));
    SmartDashboard.putNumber("Axis 3", joystick.getRawAxis(3));
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
