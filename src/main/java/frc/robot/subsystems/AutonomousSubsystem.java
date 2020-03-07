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

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

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
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);

  /**
   * Victor SPX controllers to control magazine and shooter
   */
  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);
  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);
  
  /**
   * Rangefinder used to track number of power cells leaving the bot
   */
  //Top
  private DigitalInput topEcho = new DigitalInput(3);
  private DigitalOutput topPing = new DigitalOutput(2);
  private Ultrasonic topRangeFinder = new Ultrasonic(topPing, topEcho);

  /**
   * Booleans for determining process of operations within shooter method
   */
  private boolean shooterCheckForYellow = false; //Determines whether or not to check for power cell in end of magazine while shooting
  private boolean shooterCheckForNothing = false; //Determines whether or not to check for nothing in end of magazine while shooting

  /**
   * Integer to keep count of how many balls are in the magzine
   */
  private int countOfBallsInMagazine = 3; //3 are initially in Autonomous

  /**
   * Creates a new AutonomousSubsystem.
   */
  public AutonomousSubsystem() {
    //Imverts motors for proper driving
    rightFrontSparkController.setInverted(true);
    rightBackSparkController.setInverted(true);
  }

  /**
   * Method to be called on scheduler to run autonomous
   */
  public void autonomous() {
    //Gather selection from Shuffleboard that was declared in Robot
    String choice = Robot.startingPositionChooser.getSelected();

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

  /**
   * Method to be called if we are in position one
   */
  private void targetPositionOne()
  {
    //Gathers data 
    double limelightXValue = limelightX.getDouble(0.0); //tx    0.0 is default speed
    double limelightAreaValue = limelightArea.getDouble(0.0); //ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); //tv

    //Booleans to determine proper timing of events
    boolean targetingOkay = true;
    boolean shootingOkay = false;
    
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
      else if (limelightAreaValue > Constants.LIMELIGHT_AREA_FOUND_MAXIMUM) 
      {
        moveBackward();
      }
      else if (limelightAreaValue < Constants.LIMELIGHT_AREA_FOUND_MINIMUM)
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
    if (shootingOkay)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_ONE);
      if (countOfBallsInMagazine == 0)
      {
        shootingOkay = false;
      }
    }
    //Add white line w encoders
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

    //Booleans to determine order of autonomous
    boolean targetingOkay = true;
    boolean shootingOkay = false;

    //Targeting Process
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
      else if (limelightAreaValue < (Constants.LIMELIGHT_AREA_FOUND_MINIMUM + 0.9)) 
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
    if (shootingOkay)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO);
      if (countOfBallsInMagazine == 0)
      {
        shootingOkay = false;
      }
    }
    //Add white line w/ encoders
  }

  /**
   * Method for autonomous and targeting from position three
   */
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
    double limelightAreaValue = limelightArea.getDouble(0.0);
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0);

    //Booleans to determine order of autonomous
    boolean movingOkay = true;
    boolean targetingOkay = false;
    boolean shootingOkay = false;

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
    if (targetingOkay)
    {
      if (limelightTargetFoundValue != Constants.LIMELIGHT_TARGET_FOUND)
      {
        turnLeft();
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
        shootingOkay = true;
      }
    }
    //Shooting process
    if (shootingOkay)
    {
      shootBall(Constants.AUTONOMOUS_SHOOTER_SPEED_POSITION_THREE);
      if (countOfBallsInMagazine == 0)
      {
        shootingOkay = false;
      }
    }
  }

  /**
   * Turns robot right for positioning 
   */
  private void turnRight()
  {
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);

  }

  /**
   * Turns robot left for positioning
   */
  private void turnLeft()
  {
    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
  }

  /**
   * Moves robot forward for positioning
   */
  private void moveForward()
  {
    leftFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightBackSparkController.set(Constants.AUTONOMOUS_MOVEMENT_SPEED);
  }

  /**
   * Moves robot backward for positioning
   */
  private void moveBackward()
  {
    leftFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    rightFrontSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
    leftBackSparkController.set(-Constants.AUTONOMOUS_MOVEMENT_SPEED);
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
  private void shootBall(double speed)
  {
    if (countOfBallsInMagazine != 0)
    {
      //Sets shooter to speed and begins magazine movement
      shooterLeftSideController.set(Constants.SPEED_CONTROL, speed);
      shooterRightSideController.set(Constants.SPEED_CONTROL, -speed);
      magazineController.set(Constants.SPEED_CONTROL, Constants.SHOOTER_MAGAZINE_OUTTAKE_SPEED);

      //Checks for yellow to determine when ball exits 
      if (shooterCheckForYellow)
      {
        if (topRangeFinder.getRangeInches() <= Constants.RANGEFINDER_BALL_DETECTED_DISTANCE)
        {
          countOfBallsInMagazine--;
          shooterCheckForNothing = true;
        }
      }
      //Checks for nothing so there isn't a constant subtracting of balls to the counter
      if (shooterCheckForNothing)
      {
        if (topRangeFinder.getRangeInches() >= Constants.RANGEFINDER_BALL_AWAY_DISTANCE)
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
    }
  }

  /**
   * Method to move robot off the white line
   */
  private void moveOffLine()
  {
    //Would be move forward or move backward for certain time depending on situation
    //Unfinished. Needs encoders
  }

  /**
   * Method to unleash intake mechanism 
   */
  public void startUp()
  {
    magazineController.set(Constants.SPEED_CONTROL, -Constants.AUTONOMOUS_RELEASE_INTAKE_MANIPULATOR_SPEED);
  }

  /**
   * Method to stop unleashing the intake mechanism
   */
  public void stopStartUp()
  {
    magazineController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
  }
}