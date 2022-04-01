// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Lights;

public class ShooterOutputCommand extends CommandBase {

  double output;
  Shooter shooter;
  Lights lights;

  /** Set the speed of the Shooter wheel. */
  public ShooterOutputCommand(double o, Shooter s, Lights l) {
    output = o;
    shooter = s;
    lights = l;
    addRequirements(shooter);
    if(shooter.getShooterVelocity() == 0){
      lights.setAnimation(lights.LEFT_SPINNER_STRIP, lights.IDLE_FIRE_ANIMATION);
      lights.setAnimation(lights.RIGHT_SPINNER_STRIP, lights.IDLE_FIRE_ANIMATION);
    }
    else{
      lights.setAnimation(lights.LEFT_SPINNER_STRIP, lights.SPIN_ANIMATION);
      lights.setAnimation(lights.RIGHT_SPINNER_STRIP, lights.SPIN_ANIMATION);
    }
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
