// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import static frc.robot.Constants.*;

/** Extend Climber arms to a given position in inches. */
public class ClimberExtendUpCommand extends CommandBase {
  Climber m_climber;
  double position;
  Timer timer = new Timer();

  /** Extend Climber arms to a given position in inches. */
  public ClimberExtendUpCommand(Climber climber) {
    m_climber = climber;
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {

    position = CLIMBER_SETPOINT_TOP_2;
    if (m_climber.isVertical()) {
      position = CLIMBER_SETPOINT_TOP_1;
    }

    timer.reset();
    timer.start();
    m_climber.extend(position);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    if (timer.hasElapsed(4.0)) {
      return true;
    }
    if (m_climber.upperLimitPressed()) {
      return true;
    }
    return Math.abs(m_climber.getEncoderPosition() - position) < 0.5;
  }
}
