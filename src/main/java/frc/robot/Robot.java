package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class Robot extends TimedRobot {

    private Command m_autonomousCommand;
    private RobotContainer m_robotContainer;

    public static int I2C_ADDRESS = 0x41;
    public static final int MAX_ANIMATIONS = 3;
    public static final int MAX_STRIPS = 4;

    private byte[] currentAnimation = new byte[MAX_STRIPS];
    private byte[] nextAnimation = new byte[MAX_STRIPS];
    private int myStrip = 0;
    private int myAnim = 0;
    private byte[] dataOut = new byte[1];

    private XboxController xbox = null;
    private I2C i2c = null;

    @Override
    public void robotInit() {
        Constants.init("/home/lvuser/deploy/config.properties",
                "/home/lvuser/config.properties",
                "/u/config.properties");
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);
        xbox = new XboxController(0);
        i2c = new I2C(Port.kMXP, I2C_ADDRESS);
        resetAnimations();
    }

    public void teleopPeriodic() {
        if (xbox.getLeftBumperPressed()) {
          myAnim = (myAnim + 1) % MAX_ANIMATIONS;
          setAnimation(myStrip, myAnim);
        } else if (xbox.getRightBumperPressed()) {
          resetAnimations();
        } else if (xbox.getYButtonPressed()) {
          myStrip = 0;
        } else if (xbox.getBButtonPressed()) {
          myStrip = 1;
        } else if (xbox.getAButtonPressed()) {
          myStrip = 2;
        } else if (xbox.getXButtonPressed()) {
          myStrip = 3;
        }
        sendAllAnimations();
    }

    private void resetAnimations() {
        for (int s = 0; s < MAX_STRIPS; s++) {
          nextAnimation[s] = (byte)0x0f;
          currentAnimation[s] = 0;
        }
        myStrip = 0;
        myAnim = 0;
    }
    
    private void setAnimation(int stripNumber, int animNumber) {
        Integer b = Integer.valueOf(((stripNumber << 4) & 0xF0) | (animNumber & 0x0F));
        nextAnimation[stripNumber] = b.byteValue();
    }

    private void sendAllAnimations() {
        for (int s = 0; s < MAX_STRIPS; s++) {
          if (nextAnimation[s] != currentAnimation[s]) {
            sendOneAnimation(s);
            currentAnimation[s] = nextAnimation[s];
          }
        }
    }

    private void sendOneAnimation(int stripNumber) {
        dataOut[0] = nextAnimation[stripNumber];
        i2c.writeBulk(dataOut, dataOut.length);
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
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}
