// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/** Reach Climber arms out to the side. */
public class ClimberReachOutCommand extends CommandBase {
  Climber m_climber;

  /** Reach Climber arms out to the side. */
  public ClimberReachOutCommand(Climber climber) {
    m_climber = climber;
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    m_climber.reachOutToSide();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
