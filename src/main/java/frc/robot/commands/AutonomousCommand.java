// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

public class AutonomousCommand extends SequentialCommandGroup {

  Chassis chassis;
  Shooter shooter;
  public AutonomousCommand(Chassis c, Shooter s) {
    chassis = c;
    shooter = s;
    addCommands(
      new DriveToDistancePIDCommand(-24, chassis).withTimeout(2.0),
      new ShooterShootCommand(shooter),
      new DriveToDistancePIDCommand(84, chassis)
    );
  }
}
