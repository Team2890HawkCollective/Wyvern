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

public class ManipulatorSubsystem extends SubsystemBase {
  
  private VictorSPX ballPickupController = new VictorSPX(Constants.BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID);

  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);

  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);

  private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry limelightX = table.getEntry("tx"); //tx
  private NetworkTableEntry limelightY = table.getEntry("ty"); //ty
  private NetworkTableEntry limelightArea = table.getEntry("ta"); //ta
  private NetworkTableEntry limelightTargetFound = table.getEntry("tv"); //tv

  private double shooterSpeed = 0.0;
  private boolean targetingOkay = true;

  private CANSparkMax leftFrontSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkController = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_CONTROLLER_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, MotorType.kBrushless);
  private CANSparkMax rightBackSparkController = new CANSparkMax(Constants.LEFT_FRONT_SPARK_CONTROLLER_ID, MotorType.kBrushless);

  
  
  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {

  }

  public void findTarget()
  {
    double limelightXValue = limelightX.getDouble(0.0); //tx
    double limelightYValue = limelightY.getDouble(0.0); //ty
    double limelightAreaValue = limelightArea.getDouble(0.0); //ta
    double limelightTargetFoundValue = limelightTargetFound.getDouble(0.0); //tv

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
        shootingOkay = true;
      }
    }
  }

  public void magazineIntake()
  {
    magazineController.set(ControlMode.PercentOutput, 1.0);
  }

  public void magazineOutake()
  {
    magazineController.set(ControlMode.PercentOutput, -1.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
