// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.sql.Time;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem.*;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.*;

public class VisionAutoShootCommand extends CommandBase {
  /** Creates a new VisionDrive. */
  Vision vs;
  Chassis cs;
  Shooter ss;
  
  PIDController pid = new PIDController(VISION_P,VISION_I,VISION_D);

  public VisionAutoShootCommand(Vision vision, Chassis chassis, Shooter shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(vision);
    addRequirements(chassis);
    addRequirements(shooter);

    vs = vision;
    cs = chassis;
    ss = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  double delay = 0;
  double lastSpeed = 0;
  boolean finishedFire = false;
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //timer delay
    delay -= 1;

    //start revving up the moters for the shooters
    ss.setShooterVelocity(1);

    double speed = 0;
    double rotation = 0;

    System.out.println("----- Running Vision Drive Command -----");
    //run the vision drive command
    if(vs.getBestTarget() != null){
      System.out.println("Dist: " + vs.getDistence() + " Inches." + " | Angle: " + vs.getAngleRaw() + " Degrees.");
      if(Math.abs(vs.getAngleRaw()) >= 0.2){
        rotation = vs.getAngle()*2;
      }
    }
    else if(vs.getLastAngle() != 0){
      System.out.println("Last Known Angle: " + vs.getLastAngleRaw() + " Degrees.");
      rotation = vs.getLastAngleRaw()/2;
    }
    else{
      rotation = 0;
    }

    System.out.println("VSystem Distence: " + vs.getDistence());
    double Dist = vs.getDistence();

    if(Dist != 0)
    {
      System.out.println("-------- Dist + " + Dist);
      if(VISION_DISTANCE_TO_FIRE_MIN < Dist && Dist < VISION_DISTANCE_TO_FIRE_MAX){
        //perfect posotion
        speed = 0;
      }
      else if(Dist <= VISION_DISTANCE_TO_FIRE_MIN){
        //too close back up
        speed = Dist/80;
      }
      else{
        //too far move closer
        speed = -(Dist/80);
      }
    }
    else speed = 0;

    System.out.println("The Final Distence : " + speed);
 
    //getting the average speed
    lastSpeed = (lastSpeed + speed) / 2;

    //drive using speed and rotation
    if(speed == 0 && rotation == 0 && delay <= 0){
      //fire the shot
      if(ss.isLowered()){
        ss.extendPusher();
        //end the code
        finishedFire = true;
      }
      else{
        ss.retractPusher();
        delay = 2.5f;
      }
    }
    else{
      cs.arcadeDrive(pid.calculate(lastSpeed),pid.calculate(rotation));
    }
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //stop all motors from driving and reset the shooter
    cs.arcadeDrive(0, 0);
    if(!ss.isLowered()) ss.retractPusher();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finishedFire;
  }
}
