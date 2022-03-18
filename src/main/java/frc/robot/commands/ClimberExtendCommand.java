// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

 /** Extend Climber arms to a given position in inches. */
public class ClimberExtendCommand extends CommandBase {
  Climber m_climber;
  double m_position;

  /** Extend Climber arms to a given position in inches. */
  public ClimberExtendCommand(double position, Climber climber) {
    m_climber = climber;
    m_position = position;
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    m_climber.extend(m_position);
  }

  @Override
  public void execute() {
  }
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
