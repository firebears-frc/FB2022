// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Climber;

/** Unlock the brake that was holding in the Climber arms. */
public class ClimberUnlockBrake extends SequentialCommandGroup {

  Climber m_climber;

  /** Unlock the brake that was holding in the Climber arms. */
  public ClimberUnlockBrake(Climber climber) {
    m_climber = climber;
    addCommands(new ClimberDriveSpeed(0.1, m_climber),
        new WaitCommand(0.1),
        new ClimberDriveSpeed(0.0, m_climber));
  }
}
