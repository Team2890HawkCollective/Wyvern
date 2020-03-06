/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.sensors.PigeonIMU;

public class EndGameSubsystem extends SubsystemBase {

  /**
   * Talon and pidgeon sensor for balancer
   */
  private final WPI_TalonSRX pidgeonTalon = new WPI_TalonSRX(Constants.PIGEON_TALON_PORT_ID);
  private final PigeonIMU _pigeon = new PigeonIMU(pidgeonTalon);

  /**
   * Victor motor controller to lift the climb
   */
  private final VictorSPX liftController = new VictorSPX(Constants.LIFT_VICTOR_SPX_CONTROLLER_ID);

  /**
   * Solenoids to operate pneumatics 
   */
  private final DoubleSolenoid liftSolenoid = new DoubleSolenoid(Constants.LIFT_SOLENOID_FORWARD_PORT_ID, Constants.LIFT_SOLENOID_BACKWARD_PORT_ID); 
  private final DoubleSolenoid brakeSolenoid = new DoubleSolenoid(Constants.BRAKE_SOLENOID_FORWARD_PORT_ID, Constants.LIFT_SOLENOID_BACKWARD_PORT_ID); 

  /**
   * Array of length 3 to gather yaw, pitch, and roll from pidgeon as well as the call number for just the pitch
   */
  private final double [] yawPitchRoll = new double[3];
  private final int pitchID = 1;

  /**
   * Booleans to determine order of end game lift and balance
   */
  private boolean beginBalance = false; //Determines when to balance

  /**
   * Xbox Controller for Assistant driver
   */
  private final XboxController assistantDriverController = new XboxController(Constants.XBOX_ASSISTANT_DRIVER_CONTROLLER_ID);

  /**
   * Method to be run during teleop for end game system
   */
  public void endGame()
  {
    //If the left bumper is pressed, then the bot will rise up to the bar
    if (assistantDriverController.getBumperPressed(Hand.kLeft))
    {
      engageLiftPneumatic();
    }
    //If the left bumper is released, the lift pneumatic will release
    if (assistantDriverController.getBumperReleased(Hand.kLeft))
    {
      releaseLiftPneumatic();
    }

    //If the right bumper is pressed, the lift will begin to pull the bot up
    if (assistantDriverController.getBumperPressed(Hand.kRight))
    {
      pullInClimbRope();
    }
    //If the right bumper is released, the bot will brake and stay stationary while hanging
    if (assistantDriverController.getBumperReleased(Hand.kRight))
    {
      stopClimbRope();
      engageBreakPneumatic();
    }

    //If the back button is pressed, the bot will begin to balance
    if (assistantDriverController.getBackButtonPressed())
    {
      beginBalance = true;
    }
    //If beginBalance is true, the bot will begin to balance
    if (beginBalance)
    {
      balanceWheel();
    }

    //If the start button is pressed, all systems will be turned off and stopped
    if (assistantDriverController.getStartButtonPressed())
    {

      liftSolenoid.set(Constants.SOLENOID_OFF);
      brakeSolenoid.set(Constants.SOLENOID_OFF);

      liftController.set(Constants.SPEED_CONTROL, Constants.PIGEON_WHEEL_STATIONARY_SPEED);
    }
  }

  /**
   * Method to engage the pneumatic to raise lift
   */
  private void engageLiftPneumatic()
  {
    liftSolenoid.set(Constants.SOLENOID_REVERSE);
  }

  /**
   * Method to release the pneumatics to latch onto the bar
   */
  private void releaseLiftPneumatic()
  {
    liftSolenoid.set(Constants.SOLENOID_FORWARD);
  }

  /**
   * Method to enable break on climb
   */
  private void engageBreakPneumatic()
  {
    brakeSolenoid.set(Constants.SOLENOID_FORWARD);
  }

  /**
   * Method to pull up bot 
   */
  private void pullInClimbRope()
  {
    liftController.set(Constants.SPEED_CONTROL, -Constants.LIFT_CONTROLLER_SPEED);
  }

  /**
   * Method to stop rope after climb
   */
  private void stopClimbRope()
  {
    liftController.set(Constants.SPEED_CONTROL, Constants.NO_SPEED);
  }

  /**
   * Method to balance bot when on bar
   */
  private void balanceWheel()
  {
    //Periodically gathers data from the pidgeon
    _pigeon.getYawPitchRoll(yawPitchRoll);
    System.out.println(yawPitchRoll[pitchID]);

    //If sensor is balanced it will stop the balancer wheel
    if(yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED && yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      turnWheel(Constants.PIGEON_WHEEL_STATIONARY_SPEED);
      beginBalance = false; //Ends balancing process by changing to false
    }
    //If sensor is far from being balanced negatively, the balancer wheel will move to the left
    if (yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      turnWheel(-Constants.PIGEON_WHEEL_FAST_TALON_SPEED);
    }
    //If sensor is far from being balanced positivly, the balancer wheel will move to the right
    if(yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED)
    {
      turnWheel(Constants.PIGEON_WHEEL_FAST_TALON_SPEED);
    }
    //If sensor is close to being balanced negatively, the balancer wheel will move left slowly
    if (yawPitchRoll[pitchID] > -Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] < -Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      turnWheel(-Constants.PIGEON_WHEEL_SLOW_TALON_SPEED);
    }
    //If sensor is close if being balanced positively, the balancer will move right slowly
    if (yawPitchRoll[pitchID] < Constants.PIDGEON_BALANCING_FAR_FROM_BALANCED && yawPitchRoll[pitchID] > Constants.PIDGEON_BALANCING_CLOSE_TO_BALANCED)
    {
      turnWheel(Constants.PIGEON_WHEEL_SLOW_TALON_SPEED);
    }
  }

  /**
   * Method to move or stop the wheel. Can be quick or slow based on speed sent
   */
  private void turnWheel(final double speed)
  {
    pidgeonTalon.set(speed);
  }
}