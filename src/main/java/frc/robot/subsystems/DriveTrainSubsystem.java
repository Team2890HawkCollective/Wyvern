/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {
  
public WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(Constants.LEFT_FRONT_TALON_ID);

public WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(Constants.RIGHT_FRONT_TALON_ID);

public WPI_TalonSRX rightBackTalon = new WPI_TalonSRX(Constants.RIGHT_BACK_TALON_ID);

public WPI_TalonSRX leftBackTalon = new WPI_TalonSRX(Constants.LEFT_BACK_TALON_ID);

public XboxController driverController = new XboxController(Constants.DRIVER_CONTROLLER_PORT_ID);

public Joystick yJoystick = new Joystick(Constants.DRIVER_CONTROLLER_PORT_ID);

public Joystick xJoystick = new Joystick(Constants.DRIVER_CONTROLLER_PORT_ID);


  
  /**
   * Creates a new driveTrainSubsystem.
   */
  public DriveTrainSubsystem() 
  {
    rightBackTalon.setInverted(true);
    rightFrontTalon.setInverted(true);
  }

  private void moveForward()
  {
    leftBackTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    rightBackTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    
  }

  private void moveBackwards()
  {
    leftBackTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    rightBackTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
  }

  private void moveLeft()
  {
    leftBackTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    rightBackTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.FORWARD_SPEED_MODIFIER);
  }

  private void moveRight()
  {
    leftBackTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    leftFrontTalon.set(Constants.FORWARD_SPEED_MODIFIER);
    rightBackTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
    rightFrontTalon.set(Constants.BACKWARDS_SPEED_MODIFIER);
  }

  public void stopMove()
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

  public void joystickArcadeDrive()
  {
    if(xJoystick.getX() > 0.05)
    {
      moveLeft();
    }
    else if(xJoystick.getX() < -0.05)
    {
      moveRight();
    }
    else if(yJoystick.getY() > 0.05)
    {
      moveForward();
    }
    else if(yJoystick.getY() < -0.05)
    {
      moveBackwards();
    }
    else
    {
      stopMove();
    }
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
