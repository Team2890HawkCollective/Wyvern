/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

import edu.wpi.first.wpilibj.XboxController;

public class ManipulatorSubsystem extends SubsystemBase {
  /**
   * Victor SPX targets to control intake, magazine, and outtake magazines
   */
  private VictorSPX ballPickupController = new VictorSPX(Constants.BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID);
  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);
  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);

  /**
   * Limelight and Data values produced from the Network table
   */
  private NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight"); //limelight network table
  private NetworkTableEntry limelightX = limelightTable.getEntry("tx"); // tx
  private NetworkTableEntry limelightY = limelightTable.getEntry("ty"); // ty
  private NetworkTableEntry limelightArea = limelightTable.getEntry("ta"); // ta
  private NetworkTableEntry limelightTargetFound = limelightTable.getEntry("tv"); // tv

  /**
   * Rangefinders used to track number of power cells in bot at specific time
   */
  //Bottom
  private DigitalInput bottomEcho = new DigitalInput(1);
  private DigitalOutput bottomPing = new DigitalOutput(0);
  private Ultrasonic bottomRangeFinder = new Ultrasonic(bottomPing, bottomEcho);
  //Top
  private DigitalInput topEcho = new DigitalInput(3);
  private DigitalOutput topPing = new DigitalOutput(2);
  private Ultrasonic topRangeFinder = new Ultrasonic(topPing, topEcho);

  /**
   * Booleans for determining process of operations within manipulators methods
   */
  private boolean magazineCheckForYellow = false; //Determines whether or not to check for power cell in beginning of magazine
  private boolean magazineCheckForNothing = false; //Determines whether or not to check for nothing in beginning of magazine
  private boolean shooterCheckForYellow = false; //Determines whether or not to check for power cell in end of magazine while shooting
  private boolean shooterCheckForNothing = false; //Determines whether or not to check for nothing in end of magazine while shooting
  private boolean findTargetOkay = false; //Determines whether or not to find the target while targeting 
  private boolean determineDistanceOkay = false; //Determines whether or not to determine how far we are from target

  /**
   * Booleans for determining whether or not to run a manipulator method
   */
  private boolean magazineOkay = false; //Determines whether or not to run the magazine intake method
  private boolean targetingOkay = false; //Determines whether or not to target 
  private boolean shootingOkay = false; //Determines whether or not to shoot and empty the magazine

  /**
   * Numbers
   */
  private double magazineSpeed = 0.2; //Speed determined for magazine whilst loading balls
  private double shooterSpeed = 0.8; //Speed for the shooter determined after targeting
  private int countOfBallsInMagazine = 0; //Keeps track of how many balls are in the magazine at a given time

  /**
   * Spark Max's used for drive train
   */
  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.RIGHT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);

  /**
   * Xbox controller used by the assistant driver
   */
  private XboxController assistantDriverController = new XboxController(Constants.XBOX_ASSISTANT_DRIVER_CONTROLLER_ID);

  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {
    //Inverts wheels for proper driving
    rightFrontSparkController.setInverted(true);
    rightBackSparkController.setInverted(true);

    //Makes sure rangefinders work properly
    bottomRangeFinder.setAutomaticMode(true);
    topRangeFinder.setAutomaticMode(true);
  }

  /**
   * Called by robot container during teleop and runs periodically
   */
  public void controlManipulators()
  {
    //If the A button is pressed, the magazine intake process will begin 
    if (assistantDriverController.getAButtonPressed())
    {
      magazineOkay = true;
      magazineCheckForYellow = true;
    }
    //Magazine intake process
    if (magazineOkay)
    {
      magazineIntake();
    }

    //If the start button is pressed, all systems will be immediately stopped
    if (assistantDriverController.getStartButtonPressed())
    {
      magazineOkay = false;
      shootingOkay = false;
      targetingOkay = false;
    }

    //If the B button is pressed, the targeting process will begin and shooter speed will be determined
    if (assistantDriverController.getBButtonPressed())
    {
      findTargetOkay = true;
      targetingOkay = true;
    }
    //Target process
    if (targetingOkay)
    {
      findTarget();
    }

    //If the Y button is pressed, the shooting process will begin and emptying of the magazine
    if (assistantDriverController.getYButtonPressed())
    {
      shooterCheckForYellow = true; 
      shootingOkay = true;
    }
    //Temporary stop for the shooter
    if (assistantDriverController.getYButtonReleased())
    {
      shootingOkay = false;
    }
    //Shooting process
    if (shootingOkay)
    {
      shootPowerCell();
    }

    //If the X button is held, the intake system will be run
    if (assistantDriverController.getXButton())
    {
      powerCellIntake(0.275);
    }
    //When the X button is released, the intake will stop rotating
    if (assistantDriverController.getXButtonReleased())
    {
      powerCellIntake(0.0);
    }
  }

  /**
   * Returns count of balls in magazine
   */
  public int returnCountOfBallsInMagazine()
  {
    return countOfBallsInMagazine;
  }

  /**
   * Finds target and centers robot 
   */
  private void findTarget() 
  {
    //Data gathered from limelight network table
    double limelightXValue = limelightX.getDouble(0.0); // tx
    double limelightAreaValue = limelightArea.getDouble(0.0); // ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); // tv

    //If it's okay to target, the limelight will enable the light and move robot depending on position
    if (findTargetOkay)
    {
      //Turns on the limelight LED
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Constants.LIMELIGHT_ON_CODE);
      if (limelightTargetFoundValue != Constants.LIMELIGHT_TARGET_FOUND) //Checks if there's a target
      {
        turnLeft();
      } 
      else if (limelightAreaValue < Constants.LIMELIGHT_AREA_FOUND_MINIMUM) //Checks if the target meets a specific area
      {
        turnLeft();
      }
      else if (limelightXValue < -Constants.LIMELIGHT_X_RANGE_MAXIMUM) //Aligns to center position
      {
        turnRight();
      } 
      else if (limelightXValue > Constants.LIMELIGHT_X_RANGE_MAXIMUM) 
      {
        turnLeft();
      } 
      else //Stops moving when centered
      {
        stopMoving();
        findTargetOkay = false; //Ends targeting process
        determineDistanceOkay = true; //Begins determining distance
      }
    }
    //Begins determining the distance from the target 
    else if (determineDistanceOkay)
    {
      //Turns off light when targeting is done
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Constants.LIMELIGHT_OFF_CODE);
      determineShooterSpeed(limelightAreaValue);
    }
  }

  /**
   * Determines shooter speed based off area of target on screen calculated after targeting
   */
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

    targetingOkay = false; //Ends targeting process
  }

  /**
   * System for shooting power cells through shooter
   */
  private void shootPowerCell()
  {
    //Runs only if there are more than 0 cells in the magazine
    if (countOfBallsInMagazine != 0)
    {
      //Sets shooter to speed and begins magazine movement
      shooterLeftSideController.set(Constants.SPEED_CONTROL, shooterSpeed);
      shooterRightSideController.set(Constants.SPEED_CONTROL, -shooterSpeed);
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

      shootingOkay = false;
    }
  }

  /**
   * Method for intaking and spacing power cells within the magazine
   */
  private void magazineIntake() 
  {
    //Determines what speed the magazine based on how many balls are in the magazine
    determineMagazineSpeed();

    //Checks for yellow first to determine when ball is inside the magazine
    if (magazineCheckForYellow)
    {
      if (bottomRangeFinder.getRangeInches() <= Constants.RANGEFINDER_BALL_DETECTED_DISTANCE)
      {
        magazineController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
        magazineCheckForYellow = false;
        magazineCheckForNothing = true;
        countOfBallsInMagazine++;
      }
      else
      {
        magazineController.set(Constants.SPEED_CONTROL, magazineSpeed);
      }
    }
    //Checks for nothing next to push the power cell slightly inside the magazine
    if (magazineCheckForNothing)
    {
      if (bottomRangeFinder.getRangeInches() >= (Constants.RANGEFINDER_BALL_AWAY_DISTANCE - 1.0)) // -1.0 to create tighter range
      {
        magazineController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
        magazineCheckForNothing = false;
        magazineOkay = false;
      }
      else
      {
        magazineController.set(Constants.SPEED_CONTROL, magazineSpeed);
      }
    }
  }

  /**
   * Determines speed of magazine based on number of power cells in magazine
   */
  private void determineMagazineSpeed()
  {
    if (countOfBallsInMagazine == 0)
    {
      magazineSpeed = 0.15;
    }
    else if (countOfBallsInMagazine == 1)
    {
      magazineSpeed = 0.15;
    }
    else if (countOfBallsInMagazine == 2)
    {
      magazineSpeed = 0.3;
    }
    else if (countOfBallsInMagazine == 3)
    {
      magazineSpeed = 0.3;
    }
    else if (countOfBallsInMagazine == 4)
    {
      magazineSpeed = 0.3;
    }
  }

  /**
   * Starts intake mechanism to pull in power cells
   */
  private void powerCellIntake(double speed) 
  {
    ballPickupController.set(Constants.SPEED_CONTROL, speed);
  }

  /**
   * Immediately turns off magazine when called
   */
  private void killMagazine() 
  {
    magazineController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
  }

  /**
   * Turns robot right for targeting
   */
  private void turnRight() 
  {
    leftFrontSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightFrontSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    leftBackSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightBackSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);

  }

  /**
   * Turns robot left for targeting
   */
  private void turnLeft() 
  {
    leftFrontSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightFrontSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    leftBackSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightBackSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
  }

  /**
   * Stops the robot from moving for targeting
   */
  private void stopMoving() 
  {
    leftFrontSparkController.set(Constants.NO_SPEED);
    rightFrontSparkController.set(Constants.NO_SPEED);
    leftBackSparkController.set(-Constants.NO_SPEED);
    rightBackSparkController.set(Constants.NO_SPEED);
  }
}
