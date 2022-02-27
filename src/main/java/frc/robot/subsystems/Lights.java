// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Lights extends SubsystemBase {

  public static final int MAX_ANIMATIONS = 6;
  public static final int MAX_PIXELSTRIPS = 4;
  public static final int I2C_ADDRESS = 4;

  public static final int IDLE_FIRE_ANIMATION = 0;
  public static final int BLUE_ANIMATION = 1;
  public static final int RED_ANIMATION = 2;
  public static final int RED_PULSE_ANIMATION = 3;
  public static final int SPIN_ANIMATION = 4;
  public static final int TARGET_ANIMATION = 5;

  public static final int BASE_STRIP = 0;
  public static final int CLIMBER_STRIP = 1;
  public static final int LEFT_SPINNER_STRIP = 2;
  public static final int RIGHT_SPINNER_STRIP = 3;

  public final I2C i2c;

  public Lights() {
    i2c = new I2C(Port.kOnboard, I2C_ADDRESS);
  }

  @Override
  public void periodic() {
    // TODO: Detect system changes and switch animations as needed
    sendAllAnimations();
  }

  private byte[] dataOut = new byte[2];
  private byte[] dataBack = new byte[0];
  private int[] currentAnimation = new int[MAX_PIXELSTRIPS];
  private int[] nextAnimation = new int[MAX_PIXELSTRIPS];

  public void resetAllAnimations() {
    for (int s = 0; s < MAX_PIXELSTRIPS; s++) {
      currentAnimation[s] = -1;
      nextAnimation[s] = currentAnimation[s];
    }
    setAnimation(BASE_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(CLIMBER_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(LEFT_SPINNER_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(RIGHT_SPINNER_STRIP, IDLE_FIRE_ANIMATION);
  }

  public synchronized void setAnimation(int s, int a) {
    nextAnimation[s] = a;
  }

  protected synchronized boolean sendAllAnimations() {
    boolean messagesSent = false;
    for (int s = 0; s < MAX_PIXELSTRIPS; s++) {
      if (nextAnimation[s] != currentAnimation[s]) {
        int a = nextAnimation[s];
        dataOut[0] = (byte) (s + '0');
        dataOut[1] = (byte) (a + '0');
        i2c.transaction(dataOut, dataOut.length, dataBack, dataBack.length);
        currentAnimation[s] = a;
        if (DEBUG) {
          System.out.printf("Lights: setAnimation(%d, %d)%n", s, a);
        }
        messagesSent = true;
      }
    }
    if (messagesSent && DEBUG) {
      System.out.printf("Lights: STUFF %n%n");
    }
    return messagesSent;
  }

}
