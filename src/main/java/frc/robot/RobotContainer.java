package frc.robot;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.CTREPCM;
import static edu.wpi.first.wpilibj.PneumaticsModuleType.REVPH;
import static frc.robot.Constants.DRIVER_CAMERAS_ENABLED;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import static frc.robot.Constants.PRACTICE_ROBOT;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.simulation.JoystickSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AcquisitionEjectCommand;
import frc.robot.commands.AcquisitionStartCommand;
import frc.robot.commands.AcquisitionStopCommand;
import frc.robot.commands.AutoOneBallCommand;
import frc.robot.commands.AutoTwoBallCommand;
import frc.robot.commands.ChassisDriveCommand;
import frc.robot.commands.ClimberDriveSpeedMod;
import frc.robot.commands.ClimberReachBackVerticalCommand;
import frc.robot.commands.ClimberReachOutCommand;
import frc.robot.commands.ClimberUnlockBrake;
import frc.robot.commands.DriveTo;
import frc.robot.commands.ShooterOutputCommand;
import frc.robot.commands.ShooterPushCommand;
import frc.robot.commands.ShooterResetCommand;
import frc.robot.subsystems.Acquisition;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

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
  //public final AHRS m_navx = new AHRS(SerialPort.Port.kUSB);
  public final Vision m_vision = new Vision("mmal_service_16.1");

  // Joysticks
  private final Joystick joystick = new Joystick(0);
  private final XboxController xController = new XboxController(1);

  // Pneumatics
  public Compressor compressor;
  public PneumaticsModuleType pneumaticsType;
  public GenericEntry motorSpeed;
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

    LiveWindow.disableAllTelemetry();

    LiveWindow.disableAllTelemetry();

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
    joystick2.onTrue(new ShooterResetCommand(m_shooter));
    // bButton.onTrue(new ClimberReachBackVerticalCommand(m_climber), true);

    // Shoot
    //

    final JoystickButton joystick1 = new JoystickButton(joystick, 1);
    joystick1.onTrue(new ShooterPushCommand(m_shooter));

    final JoystickButton joystick3 = new JoystickButton(joystick, 3);
    joystick3.onTrue(new AcquisitionStartCommand(m_acquisition));

    final JoystickButton joystick4 = new JoystickButton(joystick, 4);
    joystick4.onTrue(new AcquisitionStopCommand(m_acquisition));

    final JoystickButton joystick12 = new JoystickButton(joystick, 12);
    joystick12.onTrue(new AcquisitionEjectCommand(m_acquisition));

    // aButton.onTrue(new ClimberReachOutCommand(m_climber), true);

    // final JoystickButton bButton = new JoystickButton(Controller,
    // XboxController.Button.kB.value);
    // bButton.onTrue(new ClimberReachBackVerticalCommand(m_climber), true);

    // final JoystickButton aButton = new JoystickButton(Controller,
    // XboxController.Button.kA.value);
    // aButton.onTrue(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton joystick5 = new JoystickButton(joystick, 5);
    joystick5.onTrue(new ShooterOutputCommand(0.95, m_shooter));

    final JoystickButton joystick6 = new JoystickButton(joystick, 6);
    joystick6.onTrue(new ShooterOutputCommand(0.05, m_shooter));

    //final JoystickButton menuButton = new JoystickButton(Controller, XboxController.Button.kStart.value);
    //menuButton.onTrue(new EjectCommand(m_acquisition).withTimeout(1.5));

    final JoystickButton startButton2 = new JoystickButton(xController, XboxController.Button.kStart.value);
    startButton2.onTrue(new ClimberUnlockBrake(m_climber));

    /*final JoystickButton rightBumperButton = new JoystickButton(xController,XboxController.Button.kRightBumper.value);
    rightBumperButton.onTrue(new DriveToDistancePIDCommand(50, m_chassis));

    

    final JoystickButton backButton2 = new JoystickButton(xController, XboxController.Button.kBack.value);
    backButton2.onTrue(new AutoClimberCommand(m_climber));

    final JoystickButton yButton2 = new JoystickButton(xController, XboxController.Button.kY.value);
    yButton2.onTrue(new ClimberReachBackVerticalCommand(m_climber));

    final JoystickButton xButton2 = new JoystickButton(xController, XboxController.Button.kX.value);
    xButton2.onTrue(new ClimberReachOutCommand(m_climber));

    final JoystickButton bButton2 = new JoystickButton(xController, XboxController.Button.kB.value);
    bButton2.whileHeld(new ClimberDriveSpeed(0.2, m_climber));

    //final JoystickButton yButton2 = new JoystickButton(xController, XboxController.Button.kY.value);
    //yButton2.onTrue(new ClimberDeadCommand(m_climber));

    final JoystickButton aButton2 = new JoystickButton(xController, XboxController.Button.kA.value);
    aButton2.onTrue(new ClimberExtendCommand(12, m_climber));

    final JoystickButton leftBumper2 = new JoystickButton(xController, XboxController.Button.kLeftBumper.value);
    leftBumper2.onTrue(new ClimberExtendUpCommand(m_climber));

    final JoystickButton rightBumper2 = new JoystickButton(xController, XboxController.Button.kRightBumper.value);
    rightBumper2.onTrue(new ClimberExtendCommand(CLIMBER_SETPOINT_BOTTOM, m_climber));*/


    final JoystickButton bButton = new JoystickButton(xController, XboxController.Button.kB.value);
    bButton.onTrue(new ClimberReachBackVerticalCommand(m_climber));

    final JoystickButton xButton = new JoystickButton(xController, XboxController.Button.kX.value);
    xButton.onTrue(new ClimberReachOutCommand(m_climber));

    final JoystickButton aButton = new JoystickButton(xController, XboxController.Button.kA.value);
    aButton.whenHeld(new ClimberDriveSpeedMod(() -> ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber));

    final JoystickButton yButton = new JoystickButton(xController, XboxController.Button.kY.value);
    yButton.whenHeld(new ClimberDriveSpeedMod(() -> -1 * ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber));

    //final POVButton povButtonUp = new POVButton(xController, 0);
    //povButtonUp.whileHeld(new ClimberDriveSpeedMod(() -> -1 * ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    //final POVButton povButtonDown = new POVButton(xController, 180);
    //povButtonDown.whileHeld(new ClimberDriveSpeedMod(() -> ((xController.getRightTriggerAxis()*0.5)+0.3), m_climber), true);

    //final POVButton povButtonLeft = new POVButton(xController, 270);
    //povButtonLeft.onTrue(new ClimberReachBackVerticalCommand(m_climber), true);

    //final POVButton povButtonRight = new POVButton(xController, 90);
    //povButtonRight.onTrue(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton VButton = new JoystickButton(joystick,7);
    VButton.onTrue(new DriveTo(m_vision, m_chassis));
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
