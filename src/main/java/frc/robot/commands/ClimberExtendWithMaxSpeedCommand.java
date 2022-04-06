// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberExtendWithMaxSpeedCommand extends CommandBase {
  Climber m_climber;
  double m_position;
  double m_originalMaxSpeed;
  double m_maxSpeed;
  Timer timer = new Timer();


  /** Creates a new ClimberExtendWithMaxSpeedCommand. */
  public ClimberExtendWithMaxSpeedCommand(double position, double maxSpeed, Climber climber) {
    m_maxSpeed = maxSpeed;
    m_climber = climber;
    m_position = position;
    addRequirements(m_climber);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_originalMaxSpeed = m_climber.getMaxSpeed();
    m_climber.setMaxSpeed(m_maxSpeed);

    timer.reset();
    timer.start();
    m_climber.extend(m_position);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.setMaxSpeed(m_originalMaxSpeed);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (timer.hasElapsed(2.0)) {
      return true;
    }
    return Math.abs(m_climber.getEncoderPosition() - m_position) < 0.5;
  }
}
