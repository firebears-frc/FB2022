// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterSetVelocityCommand extends CommandBase {
  /** Creates a new ShooterSetSpeedCommand. */
  private double velocity;
  private Shooter shooter;
  public ShooterSetVelocityCommand(double v, Shooter s) {
    velocity = v;
    shooter = s;

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.setShooterVelocity(velocity / shooter.getMaxShooterRPM());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return shooter.getShooterVelocity() >= velocity - 80.0 && shooter.getShooterVelocity() <= velocity + 80.0;
  }
}
