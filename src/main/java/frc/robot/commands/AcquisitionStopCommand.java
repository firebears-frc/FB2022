// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Acquisition;

public class AcquisitionStopCommand extends CommandBase {
  private final Acquisition m_acquisition;

  /** Raise the Acquisition and stop it from spinning. */
  public AcquisitionStopCommand(Acquisition subsystem) {
    m_acquisition = subsystem;
    addRequirements(m_acquisition);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    m_acquisition.raise();
    m_acquisition.stop();
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
