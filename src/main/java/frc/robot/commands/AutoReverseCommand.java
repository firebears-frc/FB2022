// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;

public class AutoReverseCommand extends SequentialCommandGroup {

  Chassis chassis;
  public AutoReverseCommand(Chassis c) {
    chassis = c;
    addCommands(
      new DriveToDistancePIDCommand(36, chassis)
    );
  }
}
