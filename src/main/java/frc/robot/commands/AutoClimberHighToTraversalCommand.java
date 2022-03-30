// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Climber;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoClimberHighToTraversalCommand extends SequentialCommandGroup {
  /** Creates a new AutoClimberSecondBarCommand. */
  Climber m_climber;

  public AutoClimberHighToTraversalCommand(Climber climber) {
    m_climber = climber;

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
    // extends arms 12 in
        new ClimberExtendCommand(12.0, m_climber),

        // arms fall back
        new ClimberReachOutCommand(m_climber),

        // arms extend to catch next rung
        new ClimberExtendUpCommand(m_climber),

        new WaitCommand(0.5),

        // arms reach back to hold onto next rung
        new ClimberReachBackVerticalCommand(m_climber),

        new WaitCommand(1.5),

        // pulls until Trex arms come loose
        new ClimberExtendCommand(12.0, m_climber),

        new WaitCommand(2),

        // pull up til Trex arms catch next rung
        //new ClimberExtendCommand(CLIMBER_SETPOINT_BOTTOM, m_climber),
        new ClimberDriveToLowerLimitCommand(m_climber),

      
        // makes sure Trex arms are holding onto the rung
        new ClimberExtendCommand(12.0, m_climber)
    
    );
  }
}
