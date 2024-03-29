// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*
  SubSystem for Vision Code 2022
  Vision Will Return Best Target , Target Data , Ect
*/

package frc.robot.subsystems;

import static frc.robot.Constants.VISION_ENABLED;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  /** Creates a new Vision Subsystem. */

  PhotonCamera photonCam;
  PhotonTrackedTarget photonTarget;
  //NetworkTablesJNI nt;

  // gyro
  /*
  double robotYaw;
  double visionYaw;

  private double camHeight;
  private double targetHeight;
  private double camPitch;
  */

  public double TargetDist = -1;
  public double TargetX = 0;
  public double TargetY = 0;
  public double FID = -1;
  public double TargetRot = 0;
  public double LastTime;

  /**
   *
   * @param CameraName - The Name Of The Camera
   */
  public Vision(String CameraName) {
    if (VISION_ENABLED) {
      photonCam = new PhotonCamera(CameraName);
    }

    SmartDashboard.putString("Pos", "init");
    SmartDashboard.putString("Rot", "init");
    SmartDashboard.putNumber("FID", -1);
    SmartDashboard.putBoolean("VISION_ENABLED?", VISION_ENABLED);
  }

  // every update we get the best target for the Vision Subsystem
  @Override
  public void periodic() {
    Transform3d transform;
    if (VISION_ENABLED) {
      // This method will be called once per scheduler run
      var result = photonCam.getLatestResult();

      if (result.hasTargets()) {
        photonTarget = result.getBestTarget();
        transform = photonTarget.getCameraToTarget();
        SmartDashboard.putString("Pos", transform.getTranslation().toString());
        SmartDashboard.putString("Rot", transform.getRotation().toString());
        SmartDashboard.putNumber("FID", photonTarget.getFiducialId());

        FID = photonTarget.getFiducialId();
        TargetX = transform.getY();
        TargetY = transform.getZ();
        TargetDist = transform.getX();
        TargetRot = transform.getRotation().getAngle();

        LastTime = System.currentTimeMillis();

        /*
        for (FIDRunnable fidRunnable : RunnableFunctions) {
          if(photonTarget.getFiducialId() == fidRunnable.FID){
            fidRunnable.RunableFunc.run();
          }
        }
        */

        //updateVisionYaw();
      }
      else if(System.currentTimeMillis()-LastTime >= 300){
        SmartDashboard.putString("Pos", "no target");
        SmartDashboard.putString("Rot", "no target");
        SmartDashboard.putNumber("FID", -1);
        photonTarget = null;

        FID = -1;
        TargetX = 0;
        TargetY = 0;
        TargetDist = 0;
        TargetRot = 0;
      }
    }
  }
}