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

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.sensors.PigeonIMU;

public class EndGameSubsystem extends SubsystemBase {

  private WPI_TalonSRX pidgeonTalon = new WPI_TalonSRX(Constants.PIGEON_TALON_PORT_ID);
  private PigeonIMU _pigeon = new PigeonIMU(pidgeonTalon);

  private double [] yawPitchRoll = new double[3];

  //Call number for yawPitchRoll array to gather yaw data, pitch data, and roll data
  private int pitchID = 1;

  private boolean beginBalance = false;
  
  private VictorSPX liftController = new VictorSPX(Constants.LIFT_VICTOR_SPX_CONTROLLER_ID);

  private DoubleSolenoid liftSolenoid = new DoubleSolenoid(Constants.LIFT_SOLENOID_FORWARD_PORT_ID, Constants.LIFT_SOLENOID_BACKWARD_PORT_ID);
  
  private WPI_TalonSRX balancerTalon = new WPI_TalonSRX(Constants.BALANCER_TALON_ID);

  private XboxController assistantDriverController = new XboxController(Constants.ASSISTANT_DRIVER_CONTROLLER_PORT_ID);
  /**
   * Creates a new EndGameSubsytem.
   */

  public void endGame()
  {
    /*if (assistantDriverController.getBumperPressed(Hand.kLeft))
    {
      liftSolenoid.set(DoubleSolenoid.Value.kForward);
      releaseClimbRope();
    }
    if (assistantDriverController.getBumperPressed(Hand.kRight))
    {
      liftSolenoid.set(DoubleSolenoid.Value.kReverse);
      pullInClimbRope();
    }*/

    if (assistantDriverController.getBackButtonPressed())
    {
      System.out.println("Made it");
      beginBalance = true;
    }
    //If beginBalance is true, the bot will begin to balance
    if (beginBalance == true)
    {
      balanceWheel();
    }
  }

  private void balanceWheel()
  {
    System.out.println("Running");
    _pigeon.getYawPitchRoll(yawPitchRoll);
    System.out.println(yawPitchRoll[pitchID]);

    //If sensor is balanced it will stop the wheel
    if(yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED && yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      stopWheel();
      beginBalance = false; //Ends balancing process by changing to false
    }
    //If sensor is far from being balanced negatively, it will move to the left
    if (yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      turnWheelLeftFast();
    }
    //If sensor is far from being balanced positivly, it will move to the right
    if(yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      turnWheelRightFast();
    }
    //If sensor is close to being balanced negatively, it will move left slowly
    if (yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      turnWheelLeftSlow();
    }
    //If sensor is close if being balanced positively, it will move right slowly
    if (yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      turnWheelRightSlow();
    }
  }

  private void releaseClimbRope()
  {
    liftController.set(ControlMode.PercentOutput, 0.2);
  }

  private void pullInClimbRope()
  {
    liftController.set(ControlMode.PercentOutput, -0.2);
  }

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
}
