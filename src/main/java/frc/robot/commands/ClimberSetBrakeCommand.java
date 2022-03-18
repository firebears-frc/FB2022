// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberSetBrakeCommand extends CommandBase {

  private boolean m_enabled;
  private Climber m_climber;

  public ClimberSetBrakeCommand(boolean enabled, Climber climber) {
    m_enabled = enabled;
    m_climber = climber;
    addRequirements(m_climber);
  }
  
  @Override
  public void initialize() {
    m_climber.setBrake(m_enabled);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
