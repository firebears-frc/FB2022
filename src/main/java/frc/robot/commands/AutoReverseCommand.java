// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.*;

public class AutoReverseCommand extends SequentialCommandGroup {

  Chassis chassis;
  Shooter shooter;
  public AutoReverseCommand(Chassis c, Shooter s) {
    chassis = c;
    shooter = s;
    addCommands(
      new ShooterOutputCommand(.8, s), new WaitCommand(1.5), 
      new ShooterPushCommand(shooter), new WaitCommand(1.5),
      new DriveToPositionCommand(48.0, chassis).withTimeout(1.5)
    );
  }
}
