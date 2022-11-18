// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*
  SubSystem for Vision Code 2022
  Vision Will Return Best Target , Target Data , Ect
*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.*;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.*;

import static frc.robot.Constants.*;

import java.util.List;

public class Vision extends SubsystemBase {
  /** Creates a new Vision Subsystem. */

  PhotonCamera photonCam;
  PhotonTrackedTarget photonTarget;
  NetworkTablesJNI nt;

  // gyro
  AHRS navX;

  double robotYaw;
  double visionYaw;

  private double camHeight;
  private double targetHeight;
  private double camPitch;

  public double TargetDist = -1;
  public double TargetX = 0;
  public double TargetY = 0;
  public double FID = -1;
  public double TargetRot = 0;

  private double CameraFOV = 30;

  /**
   * 
   * @param CameraName - The Name Of The Camera
   * @param cH         - Camera Height : How High The Camera Is Off The Ground
   * @param tH         - Target Height : How Tall The Target Is , (8ft this year)
   * @param cP         - Camera Pitch : The Angle Of The Camera,
   * @param gyro       - Gyro NavX System Subsystem - Used To Detect The Rotation
   *                   Of The Robot
   */
  public Vision(String CameraName, double cH, double tH, double cP, AHRS gyro) {
    if (true) {
      photonCam = new PhotonCamera(CameraName);
      nt = new NetworkTablesJNI();
    }
    camHeight = cH;
    targetHeight = tH;
    camPitch = cP;
    navX = gyro;

    SmartDashboard.putString("Pos", "init");
    SmartDashboard.putString("Rot", "init");
    SmartDashboard.putNumber("FID", -1);
    SmartDashboard.putBoolean("VISION_ENABLED?", VISION_ENABLED);
  }

  // every update we get the best target for the Vision Subsystem
  @Override
  public void periodic() {
    Transform3d transform;
    if (true) {
      // This method will be called once per scheduler run
      var result = photonCam.getLatestResult();

      robotYaw = (navX.getAngle()) %  360;

      SmartDashboard.putNumber("Robot Yaw", robotYaw % 360);

      if (result.hasTargets()) {
        photonTarget = photonCam.getLatestResult().getBestTarget();
        transform = photonTarget.getCameraToTarget();
        SmartDashboard.putString("Pos", transform.getTranslation().toString());
        SmartDashboard.putString("Rot", transform.getRotation().toString());
        SmartDashboard.putNumber("FID", photonTarget.getFiducialId());

        FID = photonTarget.getFiducialId();
        TargetX = transform.getX();
        TargetY = transform.getY();
        TargetDist = transform.getZ();
        TargetRot = transform.getRotation().getAngle();

        /*
        for (FIDRunnable fidRunnable : RunnableFunctions) {
          if(photonTarget.getFiducialId() == fidRunnable.FID){
            fidRunnable.RunableFunc.run();
          }
        }
        */

        //updateVisionYaw();
      } else {
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

  /* Functions That Return Values To Main.Java */

  /**
   * 
   * @return Returns The Best Target From PhotonVision IE: Largest, Center-Most
   *         Target
   */
  public PhotonTrackedTarget getBestTarget() {
    return photonTarget;
  }

  /**
   * 
   * @return Returns The Target Yaw From -1 To 1, Based On CameraFOV
   */
  public double getAngle() {
    if (photonTarget != null) {
      return photonTarget.getYaw()/CameraFOV;
    } else {
      return 0;
    }
  }

  /**
   * 
   * @return Returns The Target Yaw (RAW), From CameraFOV *-30 To *30
   */
  public double getAngleRaw() {
    if (photonTarget != null) {
      return photonTarget.getYaw()/CameraFOV;
    }
    else {
      return 0;
    }
  }

  /**
   * 
   * @return Returns The Distence, Based Off Robot's Camera and Target Height, Camera and Target Pitch, Returns In Inches *In.
   */
  public double getDistence() {
    if (photonTarget == null) return 0;
    return Units.metersToInches(PhotonUtils.calculateDistanceToTargetMeters(
        camHeight,
        targetHeight,
        camPitch,
        Units.degreesToRadians(photonTarget.getPitch())));
  }

  void updateVisionYaw() {
    if (photonTarget == null)
      return;
    visionYaw = getAngleRaw();
  }

  /**
   * 
   * @return Returns The Last 'Known' Angle Of The Robot, Returns The Angle Based
   *         Off Of The Robots Yaw, Robot Is North And Target Will Be Position
   *         Likewise **Untested
   */
  public double getLastAngleRaw() {
    System.out.println(visionYaw + robotYaw);
    return visionYaw + robotYaw;
  }

  /**
   * 
   * @return Returns The Targets Yaw Based On The Robots Gyro's Angle And The Last 'Known' Angle of The Vision Target
    */
  public double getLastAngle() {
    System.out.println(visionYaw + robotYaw);
    return -((visionYaw + robotYaw) % 360);
  }

  /**
   * 
   * @return Returns True Or False Based On The Vision Targets Distence From The Robot, From x-y Amount Of Inches Being True, Else Being False
   */
  public boolean inRange(){
    if(photonTarget == null){
      return false;
    }
    double distance = getDistence();
    //change the range, currently 60in to 160in
    return (60 <= distance && distance <= 160);
  }
}
