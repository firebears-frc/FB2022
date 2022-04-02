// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.Constants.*;
import frc.robot.subsystems.Climber;

public class ClimberDriveSpeed extends CommandBase {
  /** Creates a new ClimberDriveSpeed. */
  private Climber m_climber;
  private double m_speed;
  public ClimberDriveSpeed(double speed, Climber climber) {
    m_climber = climber;
    m_speed = speed;

    addRequirements(climber);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double setpoint = CLIMBER_SETPOINT_TOP_1;
    if (!m_climber.isVertical())
      setpoint = CLIMBER_SETPOINT_TOP_2;

    if (m_speed > 0 || m_climber.getEncoderPosition() < setpoint-3) {
      m_climber.driveClimbers(m_speed);
    } else if (m_climber.getEncoderPosition() < setpoint) {
      m_climber.driveClimbers(m_speed/2);
    } else {
      m_climber.driveClimbers(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.driveClimbers(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
