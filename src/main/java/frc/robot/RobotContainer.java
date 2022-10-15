package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.simulation.JoystickSim;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

//gyro or navX
import com.kauailabs.navx.frc.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic
 * should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including subsystems, commands, and button mappings)
 * should be declared here.
 */
public class RobotContainer {

  private static RobotContainer m_robotContainer = null;

  // PowerDistribution
  private final PowerDistribution powerDistribution = new PowerDistribution();

  // The robot's subsystems
  public final Shooter m_shooter = new Shooter();
  public final Acquisition m_acquisition = new Acquisition();
  public final Climber m_climber = new Climber();
  public final Chassis m_chassis = new Chassis(powerDistribution);
  public final Lights m_lights = new Lights();
  public final AHRS m_navx = new AHRS(SerialPort.Port.kUSB);
  public final Vision m_vision = new Vision("CameraName", 0, Units.metersToFeet(8), 0, m_navx);

  // Joysticks
  private final Joystick joystick = new Joystick(0);
  private final XboxController xController = new XboxController(1);

  // Pneumatics
  public Compressor compressor;
  public PneumaticsModuleType pneumaticsType;
  public NetworkTableEntry motorSpeed;
  public ShuffleboardTab shooterTab;
  public PneumaticHub pneumaticHub = null;

  
  // Driver's cameras and vision cameras
  public UsbCamera camera1;

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  private RobotContainer() {

    powerDistribution.clearStickyFaults();

    // Pneumatics
    if (PRACTICE_ROBOT) {
      pneumaticsType = CTREPCM;
      compressor = new Compressor(0, CTREPCM);
    } else {
      pneumaticsType = REVPH;
      compressor = new Compressor(0, CTREPCM);  // Pneumatics Hack: Competition robot uses a CTRE PCM to control compressor
      // compressor = new Compressor(1, REVPH);
      // pneumaticHub = new PneumaticHub();
    }
    compressor.enableDigital();

    // Driver's cameras and vision cameras
    if (DRIVER_CAMERAS_ENABLED) {
      camera1 = CameraServer.startAutomaticCapture(0);
    }

    // Smartdashboard Subsystems

    // SmartDashboard Buttons
    m_chassis.addChild("Compressor", compressor);

    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    m_chassis.setDefaultCommand(new ChassisDriveCommand(m_chassis, joystick));

    // Configure autonomous sendable chooser
    m_chooser.setDefaultOption("two ball", new AutoTwoBallCommand(m_chassis, m_shooter, m_acquisition));
    m_chooser.addOption("one ball", new AutoOneBallCommand(m_chassis, m_shooter, m_acquisition));

    SmartDashboard.putData("Auto Mode", m_chooser);

    shooterTab = Shuffleboard.getTab("Shooter");

    motorSpeed = shooterTab.add("Shooter Speed", 1.0).getEntry();
  }

  public static RobotContainer getInstance() {
    if (m_robotContainer == null) {
      m_robotContainer = new RobotContainer();
    }
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and
   * then passing it to 
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Reload
    final JoystickButton joystick2 = new JoystickButton(joystick, 2);
    joystick2.whenPressed(new ShooterResetCommand(m_shooter), true);
    // bButton.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    // Shoot
    //

    final JoystickButton joystick1 = new JoystickButton(joystick, 1);
    joystick1.whenPressed(new ShooterPushCommand(m_shooter), true);

    final JoystickButton joystick3 = new JoystickButton(joystick, 3);
    joystick3.whenPressed(new AcquisitionStartCommand(m_acquisition), true);

    final JoystickButton joystick4 = new JoystickButton(joystick, 4);
    joystick4.whenPressed(new AcquisitionStopCommand(m_acquisition), true);

    final JoystickButton joystick12 = new JoystickButton(joystick, 12);
    joystick12.whenPressed(new AcquisitionEjectCommand(m_acquisition), true);

    // aButton.whenPressed(new ClimberReachOutCommand(m_climber), true);

    // final JoystickButton bButton = new JoystickButton(Controller,
    // XboxController.Button.kB.value);
    // bButton.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    // final JoystickButton aButton = new JoystickButton(Controller,
    // XboxController.Button.kA.value);
    // aButton.whenPressed(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton joystick5 = new JoystickButton(joystick, 5);
    joystick5.whenPressed(new ShooterOutputCommand(0.95, m_shooter), true);

    final JoystickButton joystick6 = new JoystickButton(joystick, 6);
    joystick6.whenPressed(new ShooterOutputCommand(0.05, m_shooter), true);

    //final JoystickButton menuButton = new JoystickButton(Controller, XboxController.Button.kStart.value);
    //menuButton.whenPressed(new EjectCommand(m_acquisition).withTimeout(1.5));
    
    final JoystickButton startButton2 = new JoystickButton(xController, XboxController.Button.kStart.value);
    startButton2.whenPressed(new ClimberUnlockBrake(m_climber));

    /*final JoystickButton rightBumperButton = new JoystickButton(xController,XboxController.Button.kRightBumper.value);
    rightBumperButton.whenPressed(new DriveToDistancePIDCommand(50, m_chassis),true);

    

    final JoystickButton backButton2 = new JoystickButton(xController, XboxController.Button.kBack.value);
    backButton2.whenPressed(new AutoClimberCommand(m_climber));

    final JoystickButton yButton2 = new JoystickButton(xController, XboxController.Button.kY.value);
    yButton2.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    final JoystickButton xButton2 = new JoystickButton(xController, XboxController.Button.kX.value);
    xButton2.whenPressed(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton bButton2 = new JoystickButton(xController, XboxController.Button.kB.value);
    bButton2.whileHeld(new ClimberDriveSpeed(0.2, m_climber), true);

    //final JoystickButton yButton2 = new JoystickButton(xController, XboxController.Button.kY.value);
    //yButton2.whenPressed(new ClimberDeadCommand(m_climber), true);

    final JoystickButton aButton2 = new JoystickButton(xController, XboxController.Button.kA.value);
    aButton2.whenPressed(new ClimberExtendCommand(12, m_climber), true);

    final JoystickButton leftBumper2 = new JoystickButton(xController, XboxController.Button.kLeftBumper.value);
    leftBumper2.whenPressed(new ClimberExtendUpCommand(m_climber), true);

    final JoystickButton rightBumper2 = new JoystickButton(xController, XboxController.Button.kRightBumper.value);
    rightBumper2.whenPressed(new ClimberExtendCommand(CLIMBER_SETPOINT_BOTTOM, m_climber), true);*/
    
    
    final JoystickButton bButton = new JoystickButton(xController, XboxController.Button.kB.value);
    bButton.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    final JoystickButton xButton = new JoystickButton(xController, XboxController.Button.kX.value);
    xButton.whenPressed(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton aButton = new JoystickButton(xController, XboxController.Button.kA.value);
    aButton.whenHeld(new ClimberDriveSpeedMod(() -> ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    final JoystickButton yButton = new JoystickButton(xController, XboxController.Button.kY.value);
    yButton.whenHeld(new ClimberDriveSpeedMod(() -> -1 * ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    //final POVButton povButtonUp = new POVButton(xController, 0);
    //povButtonUp.whileHeld(new ClimberDriveSpeedMod(() -> -1 * ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    //final POVButton povButtonDown = new POVButton(xController, 180);
    //povButtonDown.whileHeld(new ClimberDriveSpeedMod(() -> ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    //final POVButton povButtonLeft = new POVButton(xController, 270);
    //povButtonLeft.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    //final POVButton povButtonRight = new POVButton(xController, 90);
    //povButtonRight.whenPressed(new ClimberReachOutCommand(m_climber), true);
  }

  /**
   * Reset subsystems before Teleop or Autonomous.
   */
  public void resetRobot() {
    m_shooter.retractPusher();
    m_acquisition.raise();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }

  public double getPneumaticsPressure() {
    if (pneumaticHub == null) {
      return 0.0;
    } else {
      return pneumaticHub.getPressure(0);
    }
  }

  public double getPdpCurrent(int channel) {
    return powerDistribution.getCurrent(channel);
  }

}
