// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Vision;

public class DriveTo extends CommandBase {
  //In meters
  private double goalDistance = 1;


  private Vision vision;
  private Chassis chassis;

  /** Creates a new DriveTo. */
  public DriveTo(Vision v, Chassis c, double gD) {
    // Use addRequirements() here to declare subsystem dependencies.
    vision = v;
    chassis = c;
    goalDistance = gD;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //SmartDashboard.putNumber("Distence", vision.TargetDist);
    //SmartDashboard.putNumber("leftRight", vision.TargetX);

    if(vision.FID >= 50 && vision.FID <= 53){
      //Tag #1
      double rot = vision.TargetX;
      rot = MathUtil.clamp(rot, -0.5, 0.5);
      if(Math.abs(vision.TargetX) <= 0.1) rot = 0;

      if(vision.TargetDist > goalDistance){
        chassis.arcadeDrive(0.35, rot);
      }
      else {
        chassis.arcadeDrive(0, rot);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    chassis.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(vision.TargetX) <= 0.1) && (vision.TargetDist < goalDistance);
  }
}
