/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class AutonomousSubsystem extends SubsystemBase {
  //Calls the limelight and then gathers data
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry limelightX = table.getEntry("tx"); //tx
  NetworkTableEntry limelightY = table.getEntry("ty"); //ty
  NetworkTableEntry limelightArea = table.getEntry("ta"); //ta
  NetworkTableEntry limelightTargetFound = table.getEntry("tv"); //tv

  //Drive Train Talons --> Will need to be changed to NEOs for new drive train
  private WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(Constants.LEFT_FRONT_TALON_ID);
  private WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(Constants.RIGHT_FRONT_TALON_ID);
  private WPI_TalonSRX leftBackTalon = new WPI_TalonSRX(Constants.LEFT_BACK_TALON_ID);
  private WPI_TalonSRX rightBackTalon = new WPI_TalonSRX(Constants.RIGHT_BACK_TALON_ID);

  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, MotorType.kBrushless);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, MotorType.kBrushless);

  
  /**
   * Creates a new AutonomousSubsystem.
   */
  public AutonomousSubsystem() {
    //Sets two motors inverted to prevent motors from turning against each other
    rightFrontTalon.setInverted(true);
    rightBackTalon.setInverted(true);

    /* Spark max inversions to replace Talons when chassis is ready
    rightFrontSparkController.setInverted(true);
    rightBackSparkController.setInverted(true);*/
  }

  @Override
  public void periodic() {
    //Gather selection from Shuffleboard that was declared in Robot
    String choice = Robot.startingPositionChooser.getSelected();
    System.out.println("Choice: " + choice); //test print to see choice

    //Checks to make sure there is a value
    if (choice != null)
    {
      //Selects position based on Shuffleboard selection
      if (choice.equals("Left"))
      {
        targetPositionOne();
      }
      else if (choice.equals("Center"))
      {
        targetPositionTwo();
      }
      else if (choice.equals("Right"))
      {
        targetPositionThree();
      }
    }
  }

  //System of events for Position One (Left)
  private void targetPositionOne()
  {
    //Gathers data 
    double limelightXValue = limelightX.getDouble(0.0); //tx
    double limelightYValue = limelightY.getDouble(0.0); //ty
    double limelightAreaValue = limelightArea.getDouble(0.0); //ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); //tv

    //Booleans to determine proper timing of events
    boolean targetingOkay = true;
    boolean shootingOkay = false;

    //Calculated distance based of area encompassed by target within Limelight
    double distance = -2.13 * limelightAreaValue + 14.79;
    
    //Targeting process
    if (targetingOkay == true)
    {
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
      else if (distance > 9.0) //Might change to just ta
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
        targetingOkay = false;
        shootingOkay = true;
      }
    }
    //Shooting Process
    if (shootingOkay == true)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_ONE);
      shootingOkay = false;
    }
  }

  //System for targeting from Position Two
  private void targetPositionTwo()
  {
    //Gather data
    double limelightXValue = limelightX.getDouble(0.0);
    double limelightYValue = limelightY.getDouble(0.0);
    double limelightAreaValue = limelightArea.getDouble(0.0);
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0);

    //Booleans to determine order of autonomous
    boolean targetingOkay = true;
    boolean shootingOkay = false;

    //Distance calculated based off of area of target within limelight
    double distance = -2.13 * limelightAreaValue + 14.79;

    //Targeting Process
    if (targetingOkay == true)
    {
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
      else if (limelightAreaValue < 2.2) //Will need to be changed based on calculations
      {
        moveBackward();
      }
      else
      {
        stopMoving();
        targetingOkay = false;
        shootingOkay = true;
      }
    }
    //Shooting process
    if (shootingOkay == true)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO);
      shootingOkay = false;
    }
  }

  //System for targeting from position three
  private void targetPositionThree()
  {
    /** Planned path for position three, will need to be tested once we have NEOs and encoders
     * Move backwards 
     * Turn Right
     * Move forward (moves across field)
     * Being targeting by locating target by turning left
     * Find target
     * Shoot
     */

    //Gather data
    double limelightXValue = limelightX.getDouble(0.0);
    double limelightYValue = limelightY.getDouble(0.0);
    double limelightAreaValue = limelightArea.getDouble(0.0);
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0);

    //Booleans to determine order of autonomous
    boolean movingOkay = true;
    boolean targetingOkay = false;
    boolean shootingOkay = false;

    //Distance calculated based off of area of target within limelight
    double distance = -2.13 * limelightAreaValue + 14.79;

    //Movement Process
    if (movingOkay == true)
    {
      //Encoder logic will need to be here 
      //moveBackward();
      //turnRight();
      //moveForward();
      movingOkay = false;
      targetingOkay = true;
    }
    //Targeting Process
    if (targetingOkay == true)
    {
      if (limelightTargetFoundValue != 1.0)
      {
        turnLeft();
      }
      else if (limelightXValue < -2.0)
      {
        turnLeft();
      }
      else if (limelightXValue > 2.0)
      {
        turnRight();
      }
      else if (limelightAreaValue < 2.2) //Will need to be changed based on calculations
      {
        moveBackward();
      }
      else
      {
        stopMoving();
        targetingOkay = false;
        shootingOkay = true;
      }
    }
    //Shooting process
    if (shootingOkay == true)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO);
      shootingOkay = false;
    }
  }

  //Turns robot right
  private void turnRight()
  {
    leftFrontTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);

    /* Spark Maxs' to replace talons when chassis is ready
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);*/

  }

  //Turns robot left
  private void turnLeft()
  {
    leftFrontTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);

    /* Spark Max
    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);*/
  }

  //Moves robot forward
  private void moveForward()
  {
    leftFrontTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    rightFrontTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    leftBackTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    rightBackTalon.set(Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);

    /* Spark Max
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);*/
  }

  //Moves robot backward
  private void moveBackward()
  {
    leftFrontTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    rightFrontTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    leftBackTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);
    rightBackTalon.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED_SLOW);

    /* Spark Max
    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);*/
  }

  //Stops the robot from moving
  private void stopMoving()
  {
    leftFrontTalon.set(Constants.NO_SPEED);
    rightFrontTalon.set(Constants.NO_SPEED);
    leftBackTalon.set(Constants.NO_SPEED);
    rightBackTalon.set(Constants.NO_SPEED);

    /* Spark Max
    leftFrontSparkController.set(Constants.NO_SPEED);
    rightFrontSparkController.set(Constants.NO_SPEED);
    leftBackSparkController.set(-Constants.NO_SPEED);
    rightBackSparkController.set(Constants.NO_SPEED);*/
  }

  //Shoots ball
  private void shootBall(double speed)
  {
    //Would be code for whichever shooter we use
  }

  //Moves robot off the line after targeting process. Will probably involve encoders
  private void moveOffLine()
  {
    //Would be move forward or move backward for certain time depending on situation
  }
}
