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

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

public class AutonomousSubsystem extends SubsystemBase {
  /**
   * Calls the limelight and gather basic data
   */
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry limelightX = table.getEntry("tx"); //tx
  NetworkTableEntry limelightY = table.getEntry("ty"); //ty
  NetworkTableEntry limelightArea = table.getEntry("ta"); //ta
  NetworkTableEntry limelightTargetFound = table.getEntry("tv"); //tv

  /**
   * Spark Max's used for drivetrain
   */
  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.RIGHT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);

  private CANEncoder leftFrontSparkEncoder = new CANEncoder(leftFrontSparkController);

  /**
   * Victor SPX controllers to control magazine and shooter
   */
  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);
  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);
  private VictorSPX ballPickupController = new VictorSPX(Constants.BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID);
  
  private boolean moveOffLineOkay = true;
  private boolean targetingOkay = true;
  private boolean shootingOkay = false;
  private boolean movingOkay = false;
  private boolean determineSpeedOkay = false;

  private boolean positionTwoTargetingOkay = true;
  private boolean positionTwoShootingOkay = false;
  private boolean positionTwoMovingOkay = false;

  private double shooterSpeed = 0.8;

  /**
   * Booleans for determining process of operations within shooter method
   */
  private boolean shooterCheckForYellow = false; //Determines whether or not to check for power cell in end of magazine while shooting
  private boolean shooterCheckForNothing = false; //Determines whether or not to check for nothing in end of magazine while shooting
  private boolean startUpCheck = true;
  /**
   * Integer to keep count of how many balls are in the magzine
   */
  private int countOfBallsInMagazine = 3; //3 are initially in Autonomous

  private Timer timer = new Timer();


  /**
   * Creates a new AutonomousSubsystem.
   */
  public AutonomousSubsystem() {
    //Imverts motors for proper driving
    leftFrontSparkController.setInverted(true);
    leftBackSparkController.setInverted(true);
    leftFrontSparkEncoder.setPosition(0.0);
  }

  /**
   * Method to be called on scheduler to run autonomous
   */
  public void autonomous() {
    //Gather selection from Shuffleboard that was declared in Robot
    String choice = Robot.startingPositionChooser.getSelected();

    if (startUpCheck)
    {
      startUp();
    }
    else 
    {
      //Checks to make sure there is a value
      if (choice != null)
      {
        //Selects position based on Shuffleboard selection
        if (choice.equals("Move"))
        {
          moveOffLine();
        }
        else if (choice.equals("Target"))
        {
          target();
        }
        else if (choice.equals("Test"))
        {
          testServo();
        }
      }
    }
  }

  /**
   * Method to be called if we are in position one
   */
  private void target()
  {
    //Gathers data 
    double limelightXValue = limelightX.getDouble(0.0); //tx    0.0 is default speed
    double limelightAreaValue = limelightArea.getDouble(0.0); //ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); //tv
    
    //Targeting process
    if (targetingOkay)
    {
      if (limelightTargetFoundValue != Constants.LIMELIGHT_TARGET_FOUND)
      {
        turnRight();
      }
      else if (limelightXValue < -Constants.LIMELIGHT_X_RANGE_MAXIMUM)
      {
        turnLeft();
      }
      else if (limelightXValue > Constants.LIMELIGHT_X_RANGE_MAXIMUM)
      {
        turnRight();
      }
      else
      {
        stopMoving();
        targetingOkay = false;
        determineSpeedOkay = true;
      }
    }
    if (determineSpeedOkay)
    {
      determineShooterSpeed(limelightAreaValue);
    }
    //Shooting Process
    if (shootingOkay)
    {
      shootBall(shooterSpeed, 0.3);
      timer.delay(5.0);
      shootBall(0.0, 0.0);
      shootingOkay = false;
      movingOkay = true;
      leftFrontSparkEncoder.setPosition(0.0);
      /*if (countOfBallsInMagazine == 0)
      {
        shootingOkay = false;
        leftFrontSparkEncoder.setPosition(0.0);
        movingOkay = true;
      }*/
    }
    if (movingOkay)
    {
      System.out.println(leftFrontSparkEncoder.getPosition());
      if (leftFrontSparkEncoder.getPosition() >= 15.0)
      {
        stopMoving();
        movingOkay = false;
      }
      else if (leftFrontSparkEncoder.getPosition() <= 15.0)
      {
        moveBackward();
      }
    }
  }

  /**
   * Method for autonomous and targeting based on position two
   */
  private void targetPositionTwo()
  {
    //Gather data
    double limelightXValue = limelightX.getDouble(0.0);
    double limelightAreaValue = limelightArea.getDouble(0.0);
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0);

    //Targeting Process
    if (positionTwoTargetingOkay)
    {
      if (limelightTargetFoundValue != Constants.LIMELIGHT_TARGET_FOUND)
      {
        turnRight();
      }
      else if (limelightXValue < -Constants.LIMELIGHT_X_RANGE_MAXIMUM)
      {
        turnLeft();
      }
      else if (limelightXValue > Constants.LIMELIGHT_X_RANGE_MAXIMUM)
      {
        turnRight();
      }
      else if (limelightAreaValue < (Constants.LIMELIGHT_AREA_FOUND_MINIMUM)) 
      {
        moveForward();
      }
      else if (limelightAreaValue > Constants.LIMELIGHT_AREA_FOUND_MAXIMUM) 
      {
        moveBackward();
      }
      else
      {
        stopMoving();
        positionTwoTargetingOkay = false;
        positionTwoShootingOkay = true;
      }
    }
    //Shooting process
    if (positionTwoShootingOkay)
    {
      //shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO);
      if (countOfBallsInMagazine == 0)
      {
        positionTwoShootingOkay = false;
        positionTwoMovingOkay = true;
      }
    }
    if (positionTwoMovingOkay)
    {
      if (leftFrontSparkEncoder.getPosition() <= -15.0)
      {
        stopMoving();
        positionTwoMovingOkay = false;
      }
      else if (leftFrontSparkEncoder.getPosition() >= -15.0)
      {
        moveForward();
      }
    }
  }

  /**
   * Method for autonomous and targeting from position three
   */
  private void moveOffLine()
  {
    System.out.println(leftFrontSparkEncoder.getPosition());
    if (moveOffLineOkay)
    {
       if (leftFrontSparkEncoder.getPosition() >= 15.0)
       {
         stopMoving();
         moveOffLineOkay = false;
       }
       else if (leftFrontSparkEncoder.getPosition() <= 15.0)
       {
         moveBackward();
       }
    }
  }

  /**
   * Turns robot right for positioning 
   */
  private void turnRight()
  {
    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);

  }

  /**
   * Turns robot left for positioning
   */
  private void turnLeft()
  {
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
  }

  /**
   * Moves robot forward for positioning
   */
  private void moveForward()
  {


    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
  }

  /**
   * Moves robot backward for positioning
   */
  private void moveBackward()
  {
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
  }

  /**
   * Stops the robot from moving after targeting is complete
   */
  private void stopMoving()
  {
    leftFrontSparkController.set(Constants.NO_SPEED);
    rightFrontSparkController.set(Constants.NO_SPEED);
    leftBackSparkController.set(-Constants.NO_SPEED);
    rightBackSparkController.set(Constants.NO_SPEED);
  }

  /**
   * Method to shoot power cells and determine how many power cells are left in the magazine
   */
  private void shootBall(double speed, double magazineSpeed)
  {
      //Sets shooter to speed and begins magazine movement
      shooterLeftSideController.set(Constants.SPEED_CONTROL, speed);
      shooterRightSideController.set(Constants.SPEED_CONTROL, -speed);
      magazineController.set(Constants.SPEED_CONTROL, magazineSpeed);

      //Checks for yellow to determine when ball exits 
      /*if (shooterCheckForYellow)
      {
        if (Robot.topRangeFinder.getRangeInches() <= Constants.RANGEFINDER_BALL_DETECTED_DISTANCE)
        {
          countOfBallsInMagazine--;
          shooterCheckForNothing = true;
        }
      }
      //Checks for nothing so there isn't a constant subtracting of balls to the counter
      if (shooterCheckForNothing)
      {
        if (Robot.topRangeFinder.getRangeInches() >= Constants.RANGEFINDER_BALL_AWAY_DISTANCE)
        {
          shooterCheckForYellow = true;
          shooterCheckForNothing = false;
        }
      }
    }
    else //Ends shooting when balls is at zero
    {
      shooterLeftSideController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
      shooterRightSideController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
      magazineController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
    }*/
  }

  /**
   * Method to unleash intake mechanism 
   */
  public void startUp()
  {
    Robot.hookServo.setPosition(0.0);
    ballPickupController.set(Constants.SPEED_CONTROL, -Constants.AUTONOMOUS_RELEASE_INTAKE_MANIPULATOR_SPEED);
    timer.delay(3.0);
    stopStartUp();
    startUpCheck = false;
  }

  /**
   * Method to stop unleashing the intake mechanism
   */
  public void stopStartUp()
  {
    ballPickupController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
  }

  private void testServo()
  {
    Robot.hookServo.setPosition(0.0);
    timer.delay(3.0);
    //Robot.hookServo.setPosition(0.75);

    //System.out.println(Robot.hookServo.getPosition());
  }

  private void determineShooterSpeed(double areaValue)
  {
    //11-15 ft
    if (areaValue > Constants.LIMELIGHT_TARGETING_AREA_LARGE_VALUE)
    {
      shooterSpeed = Constants.SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_LARGE_VALUE;
    }
    //9-11 ft
    else if (areaValue <= Constants.LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE && areaValue >= Constants.LIMELIGHT_TARGETING_AREA_SMALL_VALUE)
    {
      shooterSpeed = Constants.SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE;
    }
    //7-9 ft
    else if (areaValue > Constants.LIMELIGHT_TARGETING_AREA_SMALL_VALUE)
    {
      shooterSpeed = Constants.SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_SMALL_VALUE;
    }

    determineSpeedOkay = false; //Ends targeting process
    shootingOkay = true;
  }
}