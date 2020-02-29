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

import edu.wpi.first.wpilibj.XboxController;

public class ManipulatorSubsystem extends SubsystemBase {

  private VictorSPX ballPickupController = new VictorSPX(Constants.BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID);

  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);

  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);

  private Timer magazineTimer = new Timer();

  private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry limelightX = table.getEntry("tx"); // tx
  private NetworkTableEntry limelightY = table.getEntry("ty"); // ty
  private NetworkTableEntry limelightArea = table.getEntry("ta"); // ta
  private NetworkTableEntry limelightTargetFound = table.getEntry("tv"); // tv

  private double shooterSpeed = 0.0;
  private boolean targetingOkay = false;
  private boolean magazineCheck = false;

  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID,
      Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_CONTROLLER_ID,
      Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID,
      MotorType.kBrushless);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID,
      MotorType.kBrushless);

  private XboxController assistantDriverController = new XboxController(1);

  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {

  }

  public void findTarget() {
    double limelightXValue = limelightX.getDouble(0.0); // tx
    double limelightYValue = limelightY.getDouble(0.0); // ty
    double limelightAreaValue = limelightArea.getDouble(0.0); // ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); // tv

    if (targetingOkay) {
      if (limelightTargetFoundValue != 1.0) {
        turnRight();
      } else if (limelightXValue < -2.0) {
        turnLeft();
      } else if (limelightXValue > 2.0) {
        turnRight();
      } else {
        stopMoving();
        targetingOkay = false;
      }
    }
    else
    {
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
    if (magazineTimer.get() <= 0.5)
    {
      magazineController.set(ControlMode.PercentOutput, 0.4);
    }
    else if (magazineTimer.get() >= 0.5)
    {
      magazineController.set(ControlMode.PercentOutput, 0.0);
      magazineTimer.stop();
      magazineTimer.reset();
      magazineCheck = false;
    }
  }

  public void controlManipulators()
  {
    if (assistantDriverController.getBButtonPressed())
    {
      magazineCheck = true;
      magazineTimer.start();

      if (magazineCheck)
      {
        magazineIntake();
      }
    }
    
  }

  public void magazineOutake() {
    magazineController.set(ControlMode.PercentOutput, -0.4);
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
