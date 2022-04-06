// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/** Move climber arms to the vertical */
public class ClimberReachBackVerticalCommand extends CommandBase {
  Climber m_climber;
  boolean retract;

  /** Move climber arms to the vertical */
  public ClimberReachBackVerticalCommand(Climber climber) {
    this(climber, true);
  }

  /** Move climber arms to the vertical */
  public ClimberReachBackVerticalCommand(Climber climber, boolean retract) {
    m_climber = climber;
    this.retract = retract;
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    // if (retract) {
    //   if (m_climber.getEncoderPosition() > CLIMBER_SETPOINT_TOP_1) {
    //     m_climber.extend(CLIMBER_SETPOINT_TOP_1);
    //   }
      m_climber.reachBackVertical();
    // }
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
