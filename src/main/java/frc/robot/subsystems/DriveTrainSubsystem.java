/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {
  
  //Talons used for testing driveTrain on Quetzquatl -> Going to need to be commented out for NEO Sparks

  public WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(Constants.LEFT_FRONT_TALON_ID);
  public WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(Constants.RIGHT_FRONT_TALON_ID);
  public WPI_TalonSRX rightBackTalon = new WPI_TalonSRX(Constants.RIGHT_BACK_TALON_ID);
  public WPI_TalonSRX leftBackTalon = new WPI_TalonSRX(Constants.LEFT_BACK_TALON_ID);

  /** NEO Spark Controllers -> Needs to be uncommented when ready for testing*/
   public CANSparkMax leftFrontSparkMax = new CANSparkMax(Constants.LEFT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
   public CANSparkMax leftBackSparkMax = new CANSparkMax(Constants.LEFT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
   public CANSparkMax rightFrontSparkMax = new CANSparkMax(Constants.RIGHT_FRONT_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
   public CANSparkMax rightBackSparkMax = new CANSparkMax(Constants.RIGHT_BACK_SPARK_MAX_ID, Constants.BRUSHLESS_MOTOR);
   

  public XboxController driverController = new XboxController(Constants.DRIVER_CONTROLLER_PORT_ID);

  public Joystick yJoystick = new Joystick(Constants.DRIVER_JOYSTICK_Y_PORT_ID);
  public Joystick xJoystick = new Joystick(Constants.DRIVER_JOYSTICK_X_PORT_ID);


  
  /**
   * Creates a new driveTrainSubsystem.
   */
  public DriveTrainSubsystem() 
  {
    leftBackTalon.setInverted(true);
    leftFrontTalon.setInverted(true);

    /* Inverting Spark Max Controller
    rightFrontSparkMax.setInverted(true);
    rightBackSparkMax.setInverted(true);*/
  }

  private void moveForward()
  {
    /*
    leftBackSparkMax.set(Constants.TELEOP_FOWARD_SPEED_MODIFIER);
    leftFrontSparkMax.set(Constants.TELEOP_FOWARD_SPEED_MODIFIER);
    rightFrontSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightBackSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    */
    leftBackTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightBackTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    
  }

  private void moveBackwards()
  {
    /*
    leftBackSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    leftFrontSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightBackSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightFrontSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    */
    leftBackTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightBackTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
  }

  private void moveLeft()
  {
    /*

    leftBackSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    leftFrontSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightBackSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightFrontSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    */
    leftBackTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightBackTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
  }

  private void moveRight()
  {
    /*

    leftBackSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    leftFrontSparkMax.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightBackSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightFrontSparkMax.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    */
    leftBackTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.TELEOP_FORWARD_SPEED_MODIFIER);
    rightBackTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.TELEOP_BACKWARDS_SPEED_MODIFIER);
  }

  public void stopMove()
  {
    /*
    leftBackSparkMax.set(Constants.NOT_MOVING);
    leftFrontSparkMax.set(Constants.NOT_MOVING);
    rightBackSparkMax.set(Constants.NOT_MOVING);
    rightFrontSparkMax.set(Constants.NOT_MOVING);
    */
    leftBackTalon.set(Constants.NOT_MOVING);
    leftFrontTalon.set(Constants.NOT_MOVING);
    rightBackTalon.set(Constants.NOT_MOVING);
    rightFrontTalon.set(Constants.NOT_MOVING);
  }

  public void moveY()
  {

    leftBackTalon.set(Constants.NOT_MOVING);
    leftFrontTalon.set(Constants.NOT_MOVING);
    rightBackTalon.set(Constants.NOT_MOVING);
    rightFrontTalon.set(Constants.NOT_MOVING);
  }



  
  public void xboxArcadeDrive()
  {
    
    //System.out.println("Hello");
    if(driverController.getY(Hand.kRight) > 0.05)
    {
      moveForward();
    }
    else if(driverController.getY(Hand.kRight) < -0.05)
    {
      moveBackwards();
    }
    else if(driverController.getX(Hand.kLeft) > 0.05)
    {
      moveLeft();
    }
    else if(driverController.getX(Hand.kLeft) < -0.05)
    {
      moveRight();
    }
    else
    {
      stopMove();
    }
  }
  public double yDriveSpeed = 0.0;
  public double xDriveSpeed = 0.0;

  public double intervalSpeedX(double speedX)
  {
    if(xJoystick.getX() < 0.1 || xJoystick.getX() > -0.1)
    {
      stopMove();
    }
    else if(xJoystick.getX() > 0.3)
    {
      return speedX = 0.1;
    }
    else if(xJoystick.getY() < -0.3)
    {
      return speedX = -0.1;
    }
    else if(xJoystick.getX() > 0.6)
    {
      return speedX = 0.5;
    }
    else if(xJoystick.getX() < -0.6)
    {
      return speedX = -0.5;
    }
    else if(xJoystick.getX() > 0.9 || xJoystick.getX()> 0.9)
    {
      return speedX = 1.0;
    }
    else if(xJoystick.getX() < -0.9)
    {
      return speedX = -1.0;
    }
    return 0;
  }

  public double intervalSpeedY(double speedY)
  {
    if(xJoystick.getY() < 0.1 || xJoystick.getY() > -0.1)
    {
      stopMove();
    }
    else if(yJoystick.getY() > 0.3 )
    {
      return speedY = 0.1;
    }
    else if(yJoystick.getY() < -0.3)
    {
      return speedY = -0.1;
    }
    else if(yJoystick.getY() > 0.6 )
    {
      return speedY = 0.5;
    }
    else if(yJoystick.getY() < -0.6)
    {
      return speedY = -0.5;
    }
    else if(yJoystick.getY() > 0.9)
    {
      return speedY = 1.0;
    }
    else if(yJoystick.getY() < -0.9)
    {
      return speedY = -1.0;
    }
    return 0;
  }
  
  public void arcadeDrive(double xSpeed, double ySpeed)
  {
    
    tankDrive(forwardsSpeed, forwardsSpeed);
    if (turningSpeed >= 0.05 || turningSpeed <= -0.05)
      tankDrive(-turningSpeed + forwardsSpeed, turningSpeed + forwardsSpeed);
  }
  public void joystickArcadeDrive()
  {
    yDriveSpeed = yJoystick.getY() * 1;
    xDriveSpeed = xJoystick.getX() * 1;

    
    /*if(xJoystick.getX() > 0.1)
    {
      moveLeft();
    }
    if(xJoystick.getX() < -0.1)
    {
      moveRight();
    }
    if(yJoystick.getY() > 0.1)
    {
      moveForward();
    }
    if(yJoystick.getY() < -0.1)
    {
      moveBackwards();
    }
    else
    {
      stopMove();
    }*/

  }

  public void drive(double leftSpeed, double rightSpeed)
  {
    leftFrontTalon.set(leftSpeed);
    rightFrontTalon.set(rightSpeed);
    leftBackTalon.set(leftSpeed);
    rightBackTalon.set(rightSpeed);
    /*
    tankDrive(forwardsSpeed, forwardsSpeed, strafeSpeed);
    if (turningSpeed >= 0.05 || turningSpeed <= -0.05)
      tankDrive(-turningSpeed + forwardsSpeed, turningSpeed + forwardsSpeed, strafeSpeed);
      */
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
