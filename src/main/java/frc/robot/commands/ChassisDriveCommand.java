
package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
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
    double rotation = joystick.getX();
    m_chassis.arcadeDrive(speed, rotation);
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
