package frc.robot;

import edu.wpi.first.wpilibj.Preferences;
import static frc.robot.util.Config.cleanAllPreferences;
import static frc.robot.util.Config.loadConfiguration;
import static frc.robot.util.Config.printPreferences;

public class Constants {
    public static boolean DEBUG = false;
    public static boolean CHASSIS_CLOSED_LOOP_DRIVING = false;

    public static final int ACQUISITION_MOTOR_CAN_ID = 8;
    public static final int CHASSIS_FRONT_LEFT_MOTOR_CAN_ID = 2;
    public static final int CHASSIS_FRONT_RIGHT_MOTOR_CAN_ID = 3;
    public static final int CHASSIS_REAR_LEFT_MOTOR_CAN_ID = 4;
    public static final int CHASSIS_REAR_RIGHT_MOTOR_CAN_ID = 5;
    public static final int CLIMBER_LEFT_MOTOR_CAN_ID = 6;
    public static final int CLIMBER_RIGHT_MOTOR_CAN_ID = 7;
    public static final int SHOOTER_TURRET_MOTOR_CAN_ID = 11;
    public static final int SHOOTER_SHOOTER_MOTOR_CAN_ID = 12;

    public static void init(String... fileNames) {
        cleanAllPreferences();
        loadConfiguration(fileNames);
        printPreferences(System.out);

        DEBUG = Preferences.getBoolean("DEBUG", false);
        CHASSIS_CLOSED_LOOP_DRIVING = Preferences.getBoolean("CHASSIS_CLOSED_LOOP_DRIVING", false);
    }
}