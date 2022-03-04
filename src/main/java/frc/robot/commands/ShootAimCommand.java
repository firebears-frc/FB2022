// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

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
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turretPosition = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.

  // how fast or slow the turret will move based off the xbox controllers
  // left stick X position, (-1 0 1) * Sensisitivity
  double sensitivity = 0.2;

  @Override
  public void execute() {
    if (Xbox.getPOV() > 90 && Xbox.getPOV() < 270) { // POV is the dpad
      // Reload
      //new ShooterResetCommand(shooter);
      //turretPosition += sensitivity;
      shooter.setTurretPosition(turretPosition);
    } else if (Xbox.getPOV() > 270 || Xbox.getPOV() < 90) {
      //new ShooterPushCommand(shooter);
      //turretPosition -= sensitivity;
      //shooter.setTurretPosition(turretPosition);
    }
    //turretPosition += (Xbox.getRightX() * sensitivity);
    //shooter.setTurretPosition(turretPosition);
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