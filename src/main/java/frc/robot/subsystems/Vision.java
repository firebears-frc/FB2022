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

public class Vision extends SubsystemBase {
  /** Creates a new Vision Subsystem. */

  PhotonCamera photonCam;
  PhotonTrackedTarget photonTarget;

  double robotYaw;
  double visionYaw;

  private double camHeight;
  private double targetHeight;
  private double camPitch;

  public Vision(String CameraName, double cH, double tH, double cP) {
    photonCam = new PhotonCamera(CameraName);
    camHeight = cH;
    targetHeight = tH;
    camPitch = cP;
  }

  // every update we get the best target for the Vision Subsystem
  @Override
  public void periodic() {
    // // This method will be called once per scheduler run
    // var result = photonCam.getLatestResult();

    // if (result.hasTargets()) {
    //   photonTarget = photonCam.getLatestResult().getBestTarget();
    //   updateVisionYaw();
    // } else {
    //   photonTarget = null;
    // }
  }

  /* Functions That Return Values To Main.Java */

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

  // returns distences in inches
  public double getDistence() {
    if (photonTarget == null)
      return 0;
    return Units.metersToInches(PhotonUtils.calculateDistanceToTargetMeters(
        camHeight,
        targetHeight,
        camPitch,
        Units.degreesToRadians(photonTarget.getPitch())));
  }

  public void setRobotYaw(double y) {
    robotYaw = y;
  }

  void updateVisionYaw() {
    if (photonTarget == null)
      return;
    visionYaw = photonTarget.getYaw();
  }

  public double getLastAngle() {
    System.out.println(visionYaw + robotYaw);
    return visionYaw + robotYaw;
  }
}
