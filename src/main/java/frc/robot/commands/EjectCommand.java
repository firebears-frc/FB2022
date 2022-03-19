// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Acquisition;

public class EjectCommand extends CommandBase {
  Acquisition acquisition;

  public EjectCommand(Acquisition acquisition) {
    this.acquisition = acquisition;
    addRequirements(acquisition);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    acquisition.spin(-1);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (acquisition.isLowered()) {
      acquisition.spin(1);
    } else {
      acquisition.spin(0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
