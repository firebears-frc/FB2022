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
  public static final int I2C_ADDRESS = 0x41;

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
    i2c = new I2C(Port.kMXP, I2C_ADDRESS);
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
    for (int stripNumber = 0; stripNumber < MAX_PIXELSTRIPS; stripNumber++) {
      currentAnimation[stripNumber] = -1;
      nextAnimation[stripNumber] = currentAnimation[stripNumber];
    }
    setAnimation(BASE_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(CLIMBER_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(LEFT_SPINNER_STRIP, IDLE_FIRE_ANIMATION);
    setAnimation(RIGHT_SPINNER_STRIP, IDLE_FIRE_ANIMATION);
  }

  /**
   * Set one strip to have the numbered animation.
   */
  public synchronized void setAnimation(int stripNumber, int animNumber) {
    nextAnimation[stripNumber] = animNumber;
  }

  /**
   * Push out all animation changes to the Pico. <br/>
   * This program takes a <em>lazy</em> approach, in that animation signals are
   * only sent out if they <em>need</em> to change. Signals are only sent if the
   * desired animation is different from the current animation.
   * This prevents redundant, unnecessary changes from dominating the I2C bus.
   */
  protected synchronized boolean sendAllAnimations() {
    boolean messagesSent = false;
    for (int stripNumber = 0; stripNumber < MAX_PIXELSTRIPS; stripNumber++) {
      if (nextAnimation[stripNumber] != currentAnimation[stripNumber]) {
        sendOneAnimation(stripNumber);
        currentAnimation[stripNumber] = nextAnimation[stripNumber];
        messagesSent = true;
      }
    }
    if (messagesSent && DEBUG) {
      System.out.printf("Lights: Sent %n%n");
    }
    return messagesSent;
  }

  private void sendOneAnimation(int stripNumber) {
    int animNumber = nextAnimation[stripNumber];
    dataOut[0] = (byte) (stripNumber + '0');
    dataOut[1] = (byte) (animNumber + '0');
    i2c.transaction(dataOut, dataOut.length, dataBack, dataBack.length);
    if (DEBUG) {
      System.out.printf("Lights: setAnimation(%d, %d)%n", stripNumber, animNumber);
    }
  }

}
