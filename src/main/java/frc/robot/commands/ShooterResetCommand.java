// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Lower the ball pusher.
 */
public class ShooterResetCommand extends CommandBase {
  private final Shooter shooter;

  /** Lower the ball pusher. */
  public ShooterResetCommand(Shooter s) {
    shooter = s;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    shooter.retractPusher();
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
