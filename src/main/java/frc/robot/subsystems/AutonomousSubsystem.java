/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class AutonomousSubsystem extends SubsystemBase {
  
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry limelightX = table.getEntry("tx");
  NetworkTableEntry limelightY = table.getEntry("ty");
  NetworkTableEntry limelightArea = table.getEntry("ta");
  NetworkTableEntry limelightTargetFound = table.getEntry("tv");

  private WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(Constants.LEFT_FRONT_TALON_ID);
  private WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(Constants.RIGHT_FRONT_TALON_ID);
  private WPI_TalonSRX leftBackTalon = new WPI_TalonSRX(Constants.LEFT_BACK_TALON_ID);
  private WPI_TalonSRX rightBackTalon = new WPI_TalonSRX(Constants.RIGHT_BACK_TALON_ID);

  
  /**
   * Creates a new AutonomousSubsystem.
   */
  public AutonomousSubsystem() {
    rightFrontTalon.setInverted(true);
    rightBackTalon.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double limelightXValue = limelightX.getDouble(0.0);
    double limelightYValue = limelightY.getDouble(0.0);
    double limelightAreaValue = limelightArea.getDouble(0.0);
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0);
    //DESMOS calculated slope y ~ mx + b
    double distance = -2.13 * limelightAreaValue + 14.79;
    //double accurateDistance = (84.0 - 23.0) / (Math.tan(Math.toRadians(68.0) - Math.toRadians(limelightYValue)));
    

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);

    /*if (limelightAreaValue > 2.4 || limelightAreaValue < 2.0 || limelightTargetFoundValue != 1.0 || -1.0 < limelightXValue || limelightXValue > 1.0)
    {
     turnRight();
    }
    else 
    {
     stopMoving();
    }*/

    //Position 1
    /*if (limelightTargetFoundValue != 1.0)
    {
      turnRight();
    }
    else if (limelightXValue < -2.0)
    {
      turnLeft();
    }
    else if (limelightXValue > 2.0)
    {
      turnRight();
    }
    else if (distance > 9.0)
    {
     moveBackward();
    }
    else if (distance < 8.0)
    {
      moveForward();
    }
    else
    {
      stopMoving();
    }*/
    
    //Position 2
    if (limelightTargetFoundValue != 1.0)
    {
      turnRight();
    }
    else if (limelightXValue < -2.0)
    {
      turnLeft();
    }
    else if (limelightXValue > 2.0)
    {
      turnRight();
    }
    else if (limelightAreaValue < 2.2)
    {
      moveBackward();
    }
    else
    {
      stopMoving();
    }
  }

  private void turnRight()
  {
    leftFrontTalon.set(Constants.MOVEMENT_SPEED);
    rightFrontTalon.set(-Constants.MOVEMENT_SPEED);
    leftBackTalon.set(Constants.MOVEMENT_SPEED);
    rightBackTalon.set(-Constants.MOVEMENT_SPEED);
  }

  private void turnLeft()
  {
    leftFrontTalon.set(-Constants.MOVEMENT_SPEED);
    rightFrontTalon.set(Constants.MOVEMENT_SPEED);
    leftBackTalon.set(-Constants.MOVEMENT_SPEED);
    rightBackTalon.set(Constants.MOVEMENT_SPEED);
  }

  private void moveForward()
  {
    leftFrontTalon.set(Constants.MOVEMENT_SPEED_SLOW);
    rightFrontTalon.set(Constants.MOVEMENT_SPEED_SLOW);
    leftBackTalon.set(Constants.MOVEMENT_SPEED_SLOW);
    rightBackTalon.set(Constants.MOVEMENT_SPEED_SLOW);
  }

  private void moveBackward()
  {
    leftFrontTalon.set(-Constants.MOVEMENT_SPEED_SLOW);
    rightFrontTalon.set(-Constants.MOVEMENT_SPEED_SLOW);
    leftBackTalon.set(-Constants.MOVEMENT_SPEED_SLOW);
    rightBackTalon.set(-Constants.MOVEMENT_SPEED_SLOW);
  }

  private void stopMoving()
  {
    leftFrontTalon.set(0.0);
    rightFrontTalon.set(0.0);
    leftBackTalon.set(0.0);
    rightBackTalon.set(0.0);
  }
}
