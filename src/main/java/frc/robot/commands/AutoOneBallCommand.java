// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;

public class AutoOneBallCommand extends SequentialCommandGroup {
  Chassis m_chassis;
  Shooter m_shooter;
  Acquisition m_acquisition;

  /** Wait, shoot ball, move out of Tarmac. */
  public AutoOneBallCommand(Chassis chassis, Shooter shooter, Acquisition acquisition) {
    m_chassis = chassis;
    m_shooter = shooter;
    m_acquisition = acquisition;
    addCommands(
        new WaitCommand(6.0),
        new DriveToDistancePIDCommand(48.0, m_chassis),
        new WaitCommand(1.0),
        new ShooterShootCommand(m_shooter),
        new WaitCommand(1.5),
        new ShooterOutputCommand(0, m_shooter)
    );

  }
}
