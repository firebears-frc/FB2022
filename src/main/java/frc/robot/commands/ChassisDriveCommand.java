
package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class ChassisDriveCommand extends CommandBase {
  private Chassis m_chassis;
  private XboxController xbox;

  public ChassisDriveCommand(Chassis c, XboxController x) {
    m_chassis = c;
    xbox = x;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    double speed = xbox.getLeftY();
    double rotation = xbox.getLeftX();
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