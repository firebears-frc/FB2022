// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

public class AutoTwoBallVisionCommand extends SequentialCommandGroup {

  Chassis m_chassis;
  Shooter m_shooter;
  Acquisition m_acquisition;
  Vision m_vision;

  /** Drive back to acquire another ball, shoot both balls, move out of Tarmac. */
  public AutoTwoBallVisionCommand(Chassis chassis, Shooter shooter, Acquisition acquisition, Vision vision) {
    m_chassis = chassis;
    m_shooter = shooter;
    m_acquisition = acquisition;
    m_vision = vision;
    addCommands(
        new AcquisitionStartCommand(m_acquisition),
        new ShooterOutputCommand(0.5, m_shooter), // start spinning up
        new DriveToDistancePIDCommand(72.0, m_chassis, 4),
        new WaitCommand(1.0),
        new AcquisitionStopCommand(m_acquisition),
        new ShooterShootCommand(m_shooter),
        new DriveToDistancePIDCommand(18.0, m_chassis, -1),
        new DriveTo(m_vision, m_chassis, 2.15), // 2.15 meters, size of tarmac
        new WaitCommand(1.0),
        new ShooterShootCommand(m_shooter),
        new WaitCommand(2),
        new ShooterShootCommand(shooter),
        new ShooterOutputCommand(0, m_shooter)
    );
  }
}
