// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShooterShootCommand extends SequentialCommandGroup {
  /** Creates a new ShooterShootCommand. */
  private Shooter m_shooter;
  public ShooterShootCommand(Shooter shooter) {
    m_shooter = shooter;
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new ShooterOutputCommand(1.0, m_shooter),
                new WaitCommand(1.0),
                new ShooterPushCommand(m_shooter),
                new WaitCommand(1.0),
                new ShooterResetCommand(m_shooter));
  }
}
