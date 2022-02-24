// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShootAimCommand extends CommandBase {
  XboxController Xbox;
  Shooter shooter;

  double turretPosition;

  /** Creates a new ShootCommand. */
  public ShootAimCommand(Shooter localShooter, XboxController localXbox) {
    addRequirements(localShooter);
    shooter = localShooter;
    Xbox = localXbox;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //set shooter position to zero
    turretPosition = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.

  //how fast or slow the turret will move based off the xbox controllers
  //left stick X position, (-1 0 1) * Sensisitivity
  double sensitivity = 0.2;
  @Override
  public void execute() {
    turretPosition += (Xbox.getRightX()*sensitivity);
    shooter.setTurretPosition(turretPosition);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // reset shooter position to zero
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}