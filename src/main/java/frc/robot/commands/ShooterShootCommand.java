// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;

public class ShooterShootCommand extends SequentialCommandGroup {
  
  private Shooter m_shooter;

  /** Shoot the ball. */
  public ShooterShootCommand(Shooter shooter) {
    m_shooter = shooter;
    addCommands(new ShooterOutputCommand(0.9, m_shooter),
                new WaitCommand(1.0),
                new ShooterPushCommand(m_shooter),
                new WaitCommand(1.0),
                new ShooterResetCommand(m_shooter));
  }
}
