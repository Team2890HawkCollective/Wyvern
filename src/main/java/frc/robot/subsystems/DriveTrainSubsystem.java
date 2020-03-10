/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {
  
  /**
   * Spark Max Controllers
   */
  private CANSparkMax leftFrontSparkMax = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax leftBackSparkMax = new CANSparkMax(Constants.LEFT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightFrontSparkMax = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
  private CANSparkMax rightBackSparkMax = new CANSparkMax(Constants.RIGHT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
   
  private CANEncoder leftFrontEncoder = new CANEncoder(leftFrontSparkMax);
  /**
   * Xbox controller object used in the case the driver drives with an Xbox controller
   */
  private XboxController driverController = new XboxController(Constants.XBOX_DRIVER_CONTROLLER_PORT_ID);

  /**
   * Joystick objects used in the case the driver drives with joysticks
   */
  private Joystick leftJoystick = new Joystick(Constants.DRIVER_JOYSTICK_Y_PORT_ID);
  private Joystick rightJoystick = new Joystick(Constants.DRIVER_JOYSTICK_X_PORT_ID);

  /**
   * Speeds used for arcade drive. Y for forwards and backwards. X for turning left and right
   */
  private double yDriveSpeed = 0.0;
  private double xDriveSpeed = 0.0;

  /**
   * Speeds used for tank drive. Left for left side of bot. Right for right side of bot
   */
  private double leftDriveSpeed = 0.0;
  private double rightDriveSpeed = 0.0;

  /**
   * Creates a new driveTrainSubsystem.
   */
  public DriveTrainSubsystem() 
  {

    //Sets left side of Spark Maxs inverted for proper functioning
    leftFrontSparkMax.setInverted(true);
    leftBackSparkMax.setInverted(true);
  }

  /**
   * Method for using Xbox Controllers for arcade drive
   */
  public void xboxArcadeDrive()
  {
    //Sets forwards and backwards speed (y) to the y-axis of the left joystick. Sets turning speed (x) tp x-axis of right joystick
    yDriveSpeed = driverController.getY(Hand.kLeft) * Constants.TELEOP_DRIVE_SPEED_MODIFIER;
    xDriveSpeed = driverController.getX(Hand.kRight) * Constants.TELEOP_DRIVE_SPEED_MODIFIER;

    //Calls arcade drive method and sends speeds
    arcadeDrive(yDriveSpeed, xDriveSpeed);
  }
  
  /**
   * Method for using Joysticks for arcade drive
   */
  public void joystickArcadeDrive()
  {
    //Sets forwards and backwards speed (y) to the y-axis of the left joystick. Sets turning speed (x) tp x-axis of right joystick
    yDriveSpeed = leftJoystick.getX() * Constants.TELEOP_DRIVE_SPEED_MODIFIER;
    xDriveSpeed = rightJoystick.getY() * Constants.TELEOP_DRIVE_SPEED_MODIFIER;

    //Calls arcade drive method and sends speeds
    arcadeDrive(-yDriveSpeed, -xDriveSpeed);
  }

  /**
   * Method for using an xbox controller for tank drive
   */
  public void xboxTankDrive()
  {
    //Sets left side of bot speed to the y-axis of the left joystick. Sets right side of bot speed to y-axis of the right joystick
    leftDriveSpeed = driverController.getY(Hand.kLeft) * Constants.TELEOP_DRIVE_SPEED_MODIFIER;
    rightDriveSpeed = driverController.getY(Hand.kRight) * Constants.TELEOP_DRIVE_SPEED_MODIFIER;

    //Calls tank drive method and sends speeds
    tankDrive(leftDriveSpeed, rightDriveSpeed);
  }

  /**
   * Method for using Joystick controllers for tank drive
   */
  public void joystickTankDrive()
  {
    //Sets left side of bot speed to the y-axis of the left joystick. Sets right side of bot speed to y-axis of the right joystick
    leftDriveSpeed = leftJoystick.getX() * Constants.TELEOP_DRIVE_SPEED_MODIFIER;
    rightDriveSpeed = rightJoystick.getX() * Constants.TELEOP_DRIVE_SPEED_MODIFIER;

    //Calls tank drive method and sends speeds
    tankDrive(leftDriveSpeed, rightDriveSpeed);
  }

  /**
   * Method that enables movement via arcade style drive
   */
  public void arcadeDrive(double ySpeed, double xSpeed)
  {
    //Assigns motor to forwards/backwards speed if no turning is detected
    drive(ySpeed, ySpeed);
    //If turning is detected, it will be added to one speed side and subtracted from the other speed side to generate the effect of turning
    //whilst moving forwards/backwards at the same time
    if (xSpeed >= 0.05 || xSpeed <= -0.05)
    {
      drive(-xSpeed + ySpeed, xSpeed + ySpeed);
    }
  }

  /**
   * Method that enables movement via tank style drive
   */
  public void tankDrive(double leftJoySpeed, double rightJoySpeed)
  {
    drive(leftJoySpeed, rightJoySpeed);
  }

  /**
   * Assigns speeds to left and right controllers on bot
   */
  public void drive(double leftSpeed, double rightSpeed)
  {
    leftFrontSparkMax.set(leftSpeed);
    rightFrontSparkMax.set(rightSpeed);
    leftBackSparkMax.set(leftSpeed);
    rightBackSparkMax.set(rightSpeed);
  }
}
