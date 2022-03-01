package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.*;
import static frc.robot.Constants.*;

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

  // The robot's subsystems
  public final Shooter m_shooter = new Shooter();
  public final Acquisition m_acquisition = new Acquisition();
  public final Climber m_climber = new Climber();
  public final Chassis m_chassis = new Chassis();
  public final Lights m_lights = new Lights();
  public final Vision m_vision = new Vision("CameraName", 0, Units.metersToFeet(8), 0);

  // Joysticks
  private final XboxController xController1 = new XboxController(0);
  private final XboxController xController2 = new XboxController(1);

  // Pneumatics
  public Compressor compressor;
  public PneumaticsModuleType pneumaticsType;
  public NetworkTableEntry motorSpeed;
  public ShuffleboardTab shooterTab;

  // Driver's cameras and vision cameras
  public UsbCamera camera1;

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  private RobotContainer() {

    // Pneumatics
    if (PRACTICE_ROBOT) {
      pneumaticsType = CTREPCM;
      compressor = new Compressor(0, CTREPCM);
    } else {
      pneumaticsType = REVPH;
      compressor = new Compressor(1, REVPH);
    }
    compressor.enableDigital();

    // Driver's cameras and vision cameras
    if (DRIVER_CAMERAS_ENABLED) {
      camera1 = CameraServer.startAutomaticCapture(0);

    }

    // Smartdashboard Subsystems

    // SmartDashboard Buttons
    

    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    m_chassis.setDefaultCommand(new ChassisDriveCommand(m_chassis, xController1));

    // Configure autonomous sendable chooser
    // m_chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());

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
   * then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Reload
    final JoystickButton bButton = new JoystickButton(xController1, XboxController.Button.kB.value);
    bButton.whenPressed(new ShooterResetCommand(m_shooter), true);
    // bButton.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    // Shoot
    final JoystickButton aButton = new JoystickButton(xController1, XboxController.Button.kA.value);
    aButton.whenPressed(new ShooterShootCommand(m_shooter), true);

    final JoystickButton leftBumperButton = new JoystickButton(xController1, XboxController.Button.kLeftBumper.value);
    leftBumperButton.whenPressed(new AcquisitionStartCommand(m_acquisition), true);

    final JoystickButton rightBumperButton = new JoystickButton(xController1, XboxController.Button.kRightBumper.value);
    rightBumperButton.whenPressed(new AcquisitionStopCommand(m_acquisition), true);

    // aButton.whenPressed(new ClimberReachOutCommand(m_climber), true);

    // final JoystickButton bButton = new JoystickButton(xController1,
    // XboxController.Button.kB.value);
    // bButton.whenPressed(new ClimberReachBackVerticalCommand(m_climber), true);

    // final JoystickButton aButton = new JoystickButton(xController1,
    // XboxController.Button.kA.value);
    // aButton.whenPressed(new ClimberReachOutCommand(m_climber), true);

    final JoystickButton xButton = new JoystickButton(xController1, XboxController.Button.kX.value);
    xButton.whenPressed(new ShooterOutputCommand(motorSpeed.getDouble(1.0), m_shooter), true);

    final JoystickButton yButton = new JoystickButton(xController1, XboxController.Button.kY.value);
    yButton.whenPressed(new ShooterOutputCommand(0, m_shooter), true);

    //final JoystickButton rightBumperButton = new JoystickButton(xController1, XboxController.Button.kRightBumper.value);
    //rightBumperButton.whenPressed(new DriveToDistancePIDCommand(50, m_chassis), true);
  }

  /**
   * Reset subsystems before Teleop or Autonomous.
   */
  public void resetRobot() {
    m_shooter.retractPusher();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return new AutonomousCommand(m_chassis, m_shooter); // m_chooser.getSelected();
  }
}
