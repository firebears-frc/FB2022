package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.Config;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {

    private Command m_autonomousCommand;
    private RobotContainer m_robotContainer;

    @Override
    public void robotInit() {
        Constants.init("/home/lvuser/deploy/config.properties",
                "/home/lvuser/config.properties",
                "/u/config.properties");

        if (Constants.LOGGING) {
            Logger logger = Logger.getInstance();
            logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
            logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
            logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
            logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
            logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
            Config.forEach((key, value) -> {
                logger.recordMetadata("Config/" + key, value);
            });
            if (isReal()) {
                logger.addDataReceiver(new WPILOGWriter("/media/sda1/")); // Log to a USB stick
                logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
            } else {
                setUseTiming(false); // Run as fast as possible
                String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope
                logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
                logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs
            }
            logger.start();
        }

        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        RobotContainer.getInstance().resetRobot();
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        RobotContainer.getInstance().resetRobot();
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}
