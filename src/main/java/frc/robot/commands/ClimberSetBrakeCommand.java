// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;

public class ClimberSetBrakeCommand extends CommandBase {
  /** Creates a new ClimberSetBrakeCommand. */
  private boolean m_enabled;
  private Climber m_climber;
  public ClimberSetBrakeCommand(boolean enabled, Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_enabled = enabled;
    m_climber = climber;
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.setBrake(m_enabled);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
