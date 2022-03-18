// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterOutputCommand extends CommandBase {

  double output;
  Shooter shooter;

  /** Set the speed of the Shooter wheel. */
  public ShooterOutputCommand(double o, Shooter s) {
    output = o;
    shooter = s;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
    shooter.setShooterOutput(output);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
