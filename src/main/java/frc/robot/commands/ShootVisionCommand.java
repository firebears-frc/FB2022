// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

public class ShootVisionCommand extends CommandBase {
  /** Creates a new ShootVisionCommand. */
  Vision vs;
  Shooter shooter;

  public ShootVisionCommand(Shooter localShooter, Vision visionSystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    vs = visionSystem;
    shooter = localShooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setTurretPosition(vs.getAngle());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
