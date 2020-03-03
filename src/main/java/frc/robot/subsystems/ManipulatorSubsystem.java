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

import com.revrobotics.ColorSensorV3;
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
  private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry limelightX = table.getEntry("tx"); // tx
  private NetworkTableEntry limelightY = table.getEntry("ty"); // ty
  private NetworkTableEntry limelightArea = table.getEntry("ta"); // ta
  private NetworkTableEntry limelightTargetFound = table.getEntry("tv"); // tv

  /**
   * Color sensor and port detected straight fron Rio
   */
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  
  /**
   * Numbers and booleans 
   */
  private double shooterSpeed = 0.5; //Determined after targeting
  private boolean targetingOkay = false; //Checks whether or not we're okay to target
  private boolean magazineCheckForYellow = false; //Determines whether or not to check for yellow in magazine
  private boolean magazineCheckForNothing = false; //Determines whether or not to check for nothing in magazine
  private boolean magazineEmpty = true; //Determines whether or not the magazine is empty
  private int countOfBallsInMagazine = 0; //Counts how many balls we have in the magazine

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
    leftFrontSparkController.setInverted(true);
    leftBackSparkController.setInverted(true);
  }

  /**
   * Finds target and centers robot 
   */
  public void findTarget() {
    double limelightXValue = limelightX.getDouble(0.0); // tx
    double limelightYValue = limelightY.getDouble(0.0); // ty
    double limelightAreaValue = limelightArea.getDouble(0.0); // ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); // tv

    //If it's okay to target, the limelight will enable the light and move robot depending on position
    if (targetingOkay) {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
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
      else 
      {
        stopMoving();
        targetingOkay = false;
      }
    }
    else
    {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
      determineShooterSpeed(limelightAreaValue);
    }
  }

  private void determineShooterSpeed(double areaValue)
  {
    if (areaValue > 3.0)
    {
      shooterSpeed = 0.8;
    }
    else if (areaValue <= 2.9 && areaValue >= 2.3)
    {
      shooterSpeed = 0.5;
    }
    else if (areaValue > 2.3)
    {
      shooterSpeed = 0.3;
    }
  }

  private void shootPowerCell()
  {
    shooterLeftSideController.set(ControlMode.PercentOutput, shooterSpeed);
    shooterRightSideController.set(ControlMode.PercentOutput, -shooterSpeed);
  }

  public void magazineIntake() {
    Color detectedColor = colorSensor.getColor();
    double blue = detectedColor.blue * 100.0;
    double green = detectedColor.green * 100.0;

    if (magazineEmpty)
    {
      if (green >= 50.0 && blue <= 15.0)
      {
        magazineController.set(ControlMode.PercentOutput, 0.0);
        magazineCheckForYellow = false;
        magazineEmpty = false;
        countOfBallsInMagazine++;
      }
      else
      {
        magazineController.set(ControlMode.PercentOutput, 0.1);
      }
    }
    else if (!magazineEmpty)
    {
      magazineCheckForNothing = true;
      if (magazineCheckForNothing)
      {
        if (green >= 50.0 && blue <= 15.0)
        {
          magazineController.set(ControlMode.PercentOutput, 0.1);
        }
        else
        {
          magazineController.set(ControlMode.PercentOutput, 0.0);
          magazineCheckForNothing = false;
        }
      }
      else if (magazineCheckForYellow)
      {
        if (green >= 50.0 && blue <= 15.0)
        {
          magazineController.set(ControlMode.PercentOutput, 0.0);
          magazineCheckForYellow = false;
          countOfBallsInMagazine++;
        }
        else
        {
          magazineController.set(ControlMode.PercentOutput, 0.1);
        }
      }
    }
  }

  public void powerCellIntake() {
    ballPickupController.set(ControlMode.PercentOutput, 0.3);
  }

  public void controlManipulators()
  {
    if (assistantDriverController.getAButtonPressed())
    {
      magazineCheckForYellow = true;
      if (countOfBallsInMagazine == 5)
      {
        magazineOutake();
      }
      else
      {
        magazineIntake();
      }
    }

    if (assistantDriverController.getStartButtonPressed())
    {
      killMagazine();
    }

    if (assistantDriverController.getBButtonPressed())
    {
      targetingOkay = true;
      findTarget();
    }

    if (assistantDriverController.getYButtonPressed())
    {
      shootPowerCell();
    }

    if (assistantDriverController.getXButtonPressed())
    {
      powerCellIntake();
    }
  }

  public void magazineOutake() {

    magazineController.set(ControlMode.PercentOutput, 0.4);
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
