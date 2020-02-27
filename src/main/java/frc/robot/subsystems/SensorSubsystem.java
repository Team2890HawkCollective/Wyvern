/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class SensorSubsystem extends SubsystemBase 
{
  //The Talon the pidgeon is connected too and the sensor itself
  private WPI_TalonSRX pidgeonTalon = new WPI_TalonSRX(Constants.PIGEON_TALON_PORT_ID);
  private PigeonIMU _pigeon = new PigeonIMU(pidgeonTalon);

  //Array to gather yaw, pitch, and roll from pidgeon sensor
  private double [] yawPitchRoll = new double[3];

  //Call number for yawPitchRoll array to gather yaw data, pitch data, and roll data
  private int yawID = 0;
  private int pitchID = 1;
  private int rollID = 2;

  //Assistant driver controller
  private XboxController assistantDriverController = new XboxController(Constants.ASSISTANT_DRIVER_CONTROLLER_PORT);
  
  //Boolean to determine when to balance based on Y Button 
  private boolean beginBalance = false;

  /**
   * Creates a new SensorSubsystem.
   */
  public SensorSubsystem() 
  {

  }

  //Stops wheel
  private void stopWheel()
  {
    pidgeonTalon.set(Constants.PIGEON_WHEEL_STATIONARY_SPEED);
  }

  //Moves wheel to the right quickly
  private void turnWheelRightFast()
  {
    pidgeonTalon.set(Constants.PIGEON_WHEEL_FAST_TALON_SPEED_RIGHT);
  }

  //Moves wheel to the right slowly
  private void turnWheelRightSlow()
  {
    pidgeonTalon.set(Constants.PIGEON_WHEEL_SLOW_TALON_SPEED_RIGHT);
  }

  //Moves wheel to the left quickly
  private void turnWheelLeftFast()
  {
    pidgeonTalon.set(Constants.PIGEON_WHEEL_FAST_TALON_SPEED_LEFT);
  }

  //Moves wheel to the left slowly
  private void turnWheelLeftSlow()
  {
    pidgeonTalon.set(Constants.PIGEON_WHEEL_SLOW_TALON_SPEED_LEFT);
  }

  //Method to balance the pidgeon
  public void balancePidgeon()
  {
    _pigeon.getYawPitchRoll(yawPitchRoll);

    //If sensor is balanced it will stop the wheel
    if(yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED && yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      //stopWheel();
      System.out.println("Stop pidgeon");
      beginBalance = false; //Ends balancing process by changing to false
    }
    //If sensor is far from being balanced negatively, it will move to the left
    if (yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      //turnWheelLeft();
      System.out.println("Move left");
    }
    //If sensor is far from being balanced positivly, it will move to the right
    if(yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      //turnWheelRight();
      System.out.println("Move right");
    }
    //If sensor is close to being balanced negatively, it will move left slowly
    if (yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      //turnWheelLeftSlowly();
      System.out.println("Move left slowly");
    }
    //If sensor is close if being balanced positively, it will move right slowly
    if (yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      //turnWheelRightSlowly();
      System.out.println("Move right slowly");
    }
  }

  //Method called within Robotcontainer
  @Override
  public void periodic() {
    //Checks if the XboxController YButton has been pressed and changes begin balanced to be true
    if (assistantDriverController.getYButtonPressed() == true)
    {
      beginBalance = true;
    }
    //If beginBalance is true, the bot will begin to balance
    if (beginBalance == true)
    {
      balancePidgeon();
    }
  }
}

