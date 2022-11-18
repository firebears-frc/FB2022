// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Vision;

public class DriveTo extends CommandBase {
  private Vision vision;
  private Chassis chassis;

  /** Creates a new DriveTo. */
  public DriveTo(Vision v,Chassis c) {
    // Use addRequirements() here to declare subsystem dependencies.
    vision = v;
    chassis = c;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //SmartDashboard.putNumber("Distence", vision.TargetDist);
    //SmartDashboard.putNumber("leftRight", vision.TargetX);

    if(vision.FID != -1){
      //Tag #1
      if(vision.TargetDist > 0.6){
        chassis.arcadeDrive(-0.6, 0);
      }
      else {
        chassis.arcadeDrive(0, 0);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
