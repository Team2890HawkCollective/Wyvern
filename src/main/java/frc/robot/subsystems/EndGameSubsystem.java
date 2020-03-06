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

  /**
   * Talon and pidgeon sensor for balancer
   */
  private WPI_TalonSRX pidgeonTalon = new WPI_TalonSRX(Constants.PIGEON_TALON_PORT_ID);
  private PigeonIMU _pigeon = new PigeonIMU(pidgeonTalon);

  /**
   * Victor motor controller to lift the climb
   */
  private VictorSPX liftController = new VictorSPX(Constants.LIFT_VICTOR_SPX_CONTROLLER_ID);

  /**
   * Solenoids to operate pneumatics 
   */
  private DoubleSolenoid liftSolenoid = new DoubleSolenoid(0, 7); //0 and 7 are ports for the lift solenoid on the PCM
  private DoubleSolenoid brakeSolenoid = new DoubleSolenoid(1, 2); //1 and 2 are ports for the brake solenoid on the PCM

  /**
   * Array of length 3 to gather yaw, pitch, and roll from pidgeon as well as the call number for just the pitch
   */
  private double [] yawPitchRoll = new double[3];
  private int pitchID = 1;

  /**
   * Booleans to determine order of end game lift and balance
   */
  private boolean beginBalance = false; //Determines when to balance
  private boolean engageLiftPneumatic = false; //Determines when to enagae the lift/heighten the lift
  private boolean releaseLiftPneumatic = false; //Determines when to release the pneumatics to be able to pull up
  private boolean pullUpLift = false; //Determines when to engage controller to lift bot

  /**
   * Xbox Controller for Assistant driver
   */
  private XboxController assistantDriverController = new XboxController(Constants.ASSISTANT_DRIVER_CONTROLLER_PORT_ID);

  /**
   * Method to be run during teleop for end game system
   */
  public void endGame()
  {
    //If the left bumper is pressed then the bot will rise up to the bar
    if (assistantDriverController.getBumperPressed(Hand.kLeft))
    {
      engageLiftPneumatic = true;
    }
    //Engages lift
    if (engageLiftPneumatic)
    {
      firstLiftStage();
    }

    //If the right bumper is pressed, the pneumatics will release and the balancer will rest on the bar
    if (assistantDriverController.getBumperPressed(Hand.kRight))
    {
      releaseLiftPneumatic = true;
    }
    //If the right bumper is released, the bot will begin to be pulled up by the controller
    if (assistantDriverController.getBumperReleased(Hand.kRight))
    {
      pullUpLift = true;
    }
    //System to release the pneumatic
    if (releaseLiftPneumatic)
    {
      stageTwoReleasePneumatics();
    }
    //System to pull up the bot
    if (pullUpLift)
    {
      pullInClimbRope();
    }

    //If the back button is pressed, the bot will begin to balance
    if (assistantDriverController.getBackButtonPressed())
    {
      beginBalance = true;
    }
    //If beginBalance is true, the bot will begin to balance
    if (beginBalance == true)
    {
      balanceWheel();
    }

    //If the start button is pressed, all systems will be turned off and stopped
    if (assistantDriverController.getStartButtonPressed())
    {
      engageLiftPneumatic = false;
      releaseLiftPneumatic = false;
      beginBalance = false;
      pullUpLift = false;

      liftSolenoid.set(DoubleSolenoid.Value.kForward);
      brakeSolenoid.set(DoubleSolenoid.Value.kOff);

      liftController.set(Constants.SPEED_CONTROL, Constants.PIGEON_WHEEL_STATIONARY_SPEED);
    }
  }

  /**
   * Method to engage the pneumatic to raise lift
   */
  private void firstLiftStage()
  {
    liftSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  /**
   * Method to release the pneumatics to latch onto the bar
   */
  private void stageTwoReleasePneumatics()
  {
    liftSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  /**
   * Method to pull up bot 
   */
  private void pullInClimbRope()
  {
    liftController.set(Constants.SPEED_CONTROL, -1.0);
  }

  /**
   * Method to unreel rope after climb
   */
  private void releaseClimbRope()
  {
    liftController.set(Constants.SPEED_CONTROL, 0.8);
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
  private void turnWheel(double speed)
  {
    pidgeonTalon.set(speed);
  }
}
