/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.util.Color;

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

  private DigitalInput bottomEcho = new DigitalInput(1);
  private DigitalOutput bottomPing = new DigitalOutput(0);
  private Ultrasonic bottomRangeFinder = new Ultrasonic(bottomPing, bottomEcho);

  private DigitalInput topEcho = new DigitalInput(3);
  private DigitalOutput topPing = new DigitalOutput(4);
  private Ultrasonic topRangeFinder = new Ultrasonic(topPing, topEcho);

  
  /**
   * Booleans for determining process of operations within manipulators methods
   */
  private boolean magazineCheckForYellow = false; //Determines whether or not to check for power cell in beginning of magazine
  private boolean magazineCheckForNothing = false; //Determines whether or not to check for nothing in beginning of magazine
  private boolean magazineEmpty = true; //Determines whether or not the magazine is empty
  private boolean shooterCheckForYellow = false; //Determines whether or not to check for power cell in end of magazine while shooting
  private boolean shooterCheckForNothing = false; //Determines whether or not to check for nothing in end of magazine while shooting
  private boolean findTargetOkay = false; //Determines whether or not to find the target while targeting 
  private boolean determineDistanceOkay = false;
  /**
   * Booleans for determining whether or not to run a manipulator method
   */
  private boolean magazineOkay = false; //Determines whether or not to run the magazine intake method
  private boolean targetingOkay = false; //Determines whether or not to target 
  private boolean shootingOkay = false; //Determines whether or not to shoot and empty the magazine

  /**
   * Numbers
   */
  private double magazineSpeed = 0.2;
  private double shooterSpeed = 0.8; //Speed for the shooter determined after targeting
  private int countOfBallsInMagazine = 0; //Keeps track of how many balls are in the magazine at a given time

  /**
   * Inputs to determine when a ball enters and exits the magazine based on the top and bottom exits and entrances
   */
  private AnalogInput topMagazineSensor = new AnalogInput(Constants.TOP_MAGAZINE_SENSOR_PORT); //Sensor at end of magazine
  private AnalogInput bottomMagazineSensor = new AnalogInput(Constants.BOTTOM_MAGAZINE_SENSOR_PORT); //Sensor at beginning of magazine

  /**
   * Spark Max's used for drive train
   */
  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_BACK_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.RIGHT_BACK_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);

  /**
   * Xbox controller used by the assistant driver
   */
  private XboxController assistantDriverController = new XboxController(Constants.XBOX_ASSISTANT_DRIVER_CONTROLLER_ID);

  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {
    rightFrontSparkController.setInverted(true);
    rightBackSparkController.setInverted(true);
    bottomRangeFinder.setAutomaticMode(true);
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

    //If the start button is pressed, the magazine intake/outake will immediately be stopped
    if (assistantDriverController.getStartButtonPressed())
    {
      System.out.println("Killed");
      killMagazine();
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
      System.out.println("Shooting");
      shooterCheckForYellow = true; 
      shootingOkay = true;
    }
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
      System.out.println("Intake");
      powerCellIntake(0.275);
    }

    if (assistantDriverController.getXButtonReleased())
    {
      powerCellIntake(0.0);
    }
  }

  /**
   * Finds target and centers robot 
   */
  private void findTarget() 
  {
    //Data gathered from limelight network table
    double limelightXValue = limelightX.getDouble(0.0); // tx
    double limelightYValue = limelightY.getDouble(0.0); // ty
    double limelightAreaValue = limelightArea.getDouble(0.0); // ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); // tv

    System.out.println("tv: " + limelightTargetFoundValue);
    System.out.println("tx: " + limelightXValue);
    System.out.println("ta: " + limelightAreaValue);


    //If it's okay to target, the limelight will enable the light and move robot depending on position
    if (findTargetOkay)
    {
      System.out.println("in");
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Constants.LIMELIGHT_ON_CODE);
      //Turns on the limelight LED
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
    System.out.println("Here");
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
   * 
   */
  private void shootPowerCell()
  {
    if (countOfBallsInMagazine != 0)
    {
      shooterLeftSideController.set(ControlMode.PercentOutput, shooterSpeed);
      shooterRightSideController.set(ControlMode.PercentOutput, -shooterSpeed);
      magazineController.set(ControlMode.PercentOutput, 0.3);

      if (shooterCheckForYellow)
      {
        if (topRangeFinder.getRangeInches() <= 2.0)
        {
          countOfBallsInMagazine--;
          shooterCheckForNothing = true;
        }
      }
      if (shooterCheckForNothing = true)
      {
        if (topRangeFinder.getRangeInches() >= 4.0)
        {
          shooterCheckForYellow = true;
          shooterCheckForNothing = false;
        }
      }
    }
    else
    {
      shooterLeftSideController.set(ControlMode.PercentOutput, 0.0);
      shooterRightSideController.set(ControlMode.PercentOutput, 0.0);
      magazineController.set(ControlMode.PercentOutput, 0.0);

      shootingOkay = false;
    }

    if (shootingOkay)
    {
      shooterLeftSideController.set(ControlMode.PercentOutput, shooterSpeed);
      shooterRightSideController.set(ControlMode.PercentOutput, -shooterSpeed);
      magazineController.set(ControlMode.PercentOutput, 0.4);
    }
    else
    {
      shooterLeftSideController.set(ControlMode.PercentOutput, 0.0);
      shooterRightSideController.set(ControlMode.PercentOutput, 0.0);
      magazineController.set(ControlMode.PercentOutput, 0.0);
    }

  }

  public void magazineIntake() {
    System.out.println("Range: " + bottomRangeFinder.getRangeInches());
    System.out.println("Count of Balls: " + countOfBallsInMagazine);
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

    if (magazineCheckForYellow)
    {
      if (bottomRangeFinder.getRangeInches() <= 2.1) //bottomMagazineSensor.getValue() >= 100.0
      {
        magazineController.set(ControlMode.PercentOutput, 0.0);
        magazineCheckForYellow = false;
        magazineCheckForNothing = true;
        countOfBallsInMagazine++;
      }
      else
      {
        magazineController.set(ControlMode.PercentOutput, magazineSpeed);
      }
    }
    if (magazineCheckForNothing)
    {
      if (bottomRangeFinder.getRangeInches() >= 3.0) //bottomMagazineSensor.getValue() >= 100.0
      {
        magazineController.set(ControlMode.PercentOutput, 0.0);
        magazineCheckForNothing = false;
        magazineOkay = false;
      }
      else
      {
        magazineController.set(ControlMode.PercentOutput, magazineSpeed);
      }
    }
  }

  public void powerCellIntake(double speed) {
    ballPickupController.set(ControlMode.PercentOutput, speed);
  }

  private void killMagazine() {
    magazineController.set(ControlMode.PercentOutput, 0.0);
  }

  // Turns robot right
  private void turnRight() {

    // Spark Maxs' to replace talons when chassis is ready
    leftFrontSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightFrontSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    leftBackSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightBackSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);

  }

  // Turns robot left
  private void turnLeft() {
    // Spark Max
    leftFrontSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightFrontSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
    leftBackSparkController.set(-Constants.SHOOTER_TARGETING_TURNING_SPEED);
    rightBackSparkController.set(Constants.SHOOTER_TARGETING_TURNING_SPEED);
  }

  // Stops the robot from moving
  private void stopMoving() {
    // Spark Max
    leftFrontSparkController.set(Constants.NO_SPEED);
    rightFrontSparkController.set(Constants.NO_SPEED);
    leftBackSparkController.set(-Constants.NO_SPEED);
    rightBackSparkController.set(Constants.NO_SPEED);
  }
}
