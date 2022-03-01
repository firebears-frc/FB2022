// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*
  SubSystem for Vision Code 2022
  Vision Will Return Best Target , Target Data , Ect
*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.*;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.*;

import static frc.robot.Constants.*;

public class Vision extends SubsystemBase {
  /** Creates a new Vision Subsystem. */

  PhotonCamera photonCam;
  PhotonTrackedTarget photonTarget;

  //gyro
  AHRS navX;

  double robotYaw;
  double visionYaw;

  private double camHeight;
  private double targetHeight;
  private double camPitch;

  /**
   * 
   * @param CameraName - The Name Of The Camera
   * @param cH - Camera Height : How High The Camera Is Off The Ground
   * @param tH - Target Height : How Tall The Target Is , (8ft this year)
   * @param cP - Camera Pitch : The Angle Of The Camera,
   * @param gyro - Gyro NavX System Subsystem - Used To Detect The Rotation Of The Robot
   */
  public Vision(String CameraName, double cH, double tH, double cP, AHRS gyro) {
    photonCam = new PhotonCamera(CameraName);
    camHeight = cH;
    targetHeight = tH;
    camPitch = cP;
    navX = gyro;
  }

  // every update we get the best target for the Vision Subsystem
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var result = photonCam.getLatestResult();
    robotYaw = navX.getAngle();

    SmartDashboard.putNumber("Robot Yaw", robotYaw%360);

    if (result.hasTargets()) {
      photonTarget = photonCam.getLatestResult().getBestTarget();
      updateVisionYaw();
    } else {
      photonTarget = null;
    }
  }

  /* Functions That Return Values To Main.Java */

  /**
   * 
   * @return Returns The Best Target From PhotonVision IE: Largest, Center-Most Target
   */
  public PhotonTrackedTarget getBestTarget() {
    return photonTarget;
  }

  /**
   * 
   * @return Returns The Angle ( -1 To 1 ), 0 Being The Middle
   */
  public double getAngle() {
    if (photonTarget != null) {
      return photonTarget.getYaw()/30;
    } else {
      return 0;
    }
  }

  /**
   * 
   * @return Returns The Distence, Based Off Robot's Camera and Target Height, + Camera and Target Pitch, Returns In Inches
   */
  public double getDistence() {
    if (photonTarget == null)
      return 0;
    return Units.metersToInches(PhotonUtils.calculateDistanceToTargetMeters(
        camHeight,
        targetHeight,
        camPitch,
        Units.degreesToRadians(photonTarget.getPitch())));
  }

  /*
  public void setRobotYaw(double y) {
    robotYaw = y;
  }
  */

  void updateVisionYaw() {
    if (photonTarget == null)
      return;
    visionYaw = photonTarget.getYaw();
  }

  /**
   * 
   * @return Returns The Last 'Known' Angle Of The Robot, Returns The Angle Based Off Of The Robots Yaw, Robot Is North And Target Will Be Position Likewise
   */
  public double getLastAngle() {
    System.out.println(visionYaw + robotYaw);
    return visionYaw + robotYaw;
  }
}
