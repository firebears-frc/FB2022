// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class Vector3<T> {
  private T FirstVector;
  private T SecondVector;
  private T ThirdVector;

  public Vector3(T a, T b, T c){
    FirstVector = a;
    SecondVector = b;
    ThirdVector = c;
  }

  public T getX(){
    return FirstVector;
  }
  public T getY(){
    return SecondVector;
  }
  public T getZ(){
    return ThirdVector;
  }

  public void setX(T X){
    FirstVector = X;
  }
  public void setY(T Y){
    SecondVector = Y;
  }
  public void setZ(T Z){
    ThirdVector = Z;
  }
}

class VisionObject {
  // Where It Was On The Camera
  public VisionObject(int FiducialId){
    FID = FiducialId;
  }

  private int FID = -1;
  private Vector3<Double> CameraPosition = new Vector3<Double>(0.0,0.0,0.0);
  private double LastSeen = System.currentTimeMillis();

  public Vector3<Double> getCameraPosition(){ return CameraPosition; }
  public double getLastSeenTarget(){ return LastSeen; }
  public int getFID(){ return FID; }

  public void setCameraPosition(double X, double Y, double Z){
    CameraPosition.setX(X);
    CameraPosition.setY(Y);
    CameraPosition.setZ(Z);
    LastSeen = System.currentTimeMillis();
  }
}

public class Vision extends SubsystemBase {
  PhotonCamera PhotonCam;

  //Tracked Targets / Vision Objects
  private VisionObject[] VisionObjects = new VisionObject[10];
  private VisionObject BestTarget = null;

  /** Creates a new VisionNew. */
  public Vision(String CameraName) {
    PhotonCam = new PhotonCamera(CameraName);
  }

  private void onVisionTargetFound(PhotonTrackedTarget t){
    //Check if the target is alowed to function
    if(t.getFiducialId() <= VisionObjects.length){
      Transform3d transform = t.getCameraToTarget();
      VisionObject v = VisionObjects[t.getFiducialId()];

      //X = camera left -> right
      //Y = Camera up -> down,
      //Z = distence
      v.setCameraPosition(transform.getY(),transform.getZ(),transform.getX());
      VisionObjects[t.getFiducialId()] = v;
      BestTarget = v;
    }
    else{
      Transform3d transform = t.getCameraToTarget();
      VisionObject v = new VisionObject(t.getFiducialId());

      //X = camera left -> right
      //Y = Camera up -> down,
      //Z = distence
      v.setCameraPosition(transform.getY(),transform.getZ(),transform.getX());
      VisionObjects[t.getFiducialId()] = v;
      BestTarget = v;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    PhotonPipelineResult result = PhotonCam.getLatestResult();
    if(result.hasTargets()){
      for (PhotonTrackedTarget _target : result.getTargets()) {
        onVisionTargetFound(_target);
      }

      // Update the 'best' target
      PhotonTrackedTarget bestTarg = result.getBestTarget();
      onVisionTargetFound(bestTarg);
    }
  }

  public VisionObject getLatestTarget() { return BestTarget; }
}
