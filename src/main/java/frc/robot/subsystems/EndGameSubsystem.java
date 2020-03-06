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
    if (assistantDriverController.getBumperPressed(Hand.kLeft))
    {
      engageLiftPneumatic = true;
    }
    if (engageLiftPneumatic)
    {
      firstLiftStage();
    }
    if (assistantDriverController.getBumperPressed(Hand.kRight))
    {
      releaseLiftPneumatic = true;
    }
    if (assistantDriverController.getBumperReleased(Hand.kRight))
    {
      releaseLiftPneumatic = false;
      pullUpLift = true;
    }
    if (releaseLiftPneumatic)
    {
      stageTwoReleasePneumatics();
    }
    if (pullUpLift)
    {
      stageTwoPullUp();
    }

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

    if (assistantDriverController.getStartButtonPressed())
    {
      engageLiftPneumatic = false;
      releaseLiftPneumatic = false;
      beginBalance = false;

      liftSolenoid.set(DoubleSolenoid.Value.kForward);
      brakeSolenoid.set(DoubleSolenoid.Value.kOff);
    }
  }

  private void firstLiftStage()
  {
    liftSolenoid.set(DoubleSolenoid.Value.kReverse);
    //releaseClimbRope();
  }

  private void stageTwoReleasePneumatics()
  {
    liftSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  private void stageTwoPullUp()
  {
    liftController.set(ControlMode.PercentOutput, -0.6);
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
    liftController.set(ControlMode.PercentOutput, 0.8);
  }

  private void pullInClimbRope()
  {
    liftController.set(ControlMode.PercentOutput, -0.5);
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
