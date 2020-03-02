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

public class EndGameSubsytem extends SubsystemBase {
  
  private VictorSPX liftController = new VictorSPX(Constants.LIFT_VICTOR_SPX_CONTROLLER_ID);

  private DoubleSolenoid liftSolenoid = new DoubleSolenoid(Constants.LIFT_SOLENOID_FORWARD_PORT_ID, Constants.LIFT_SOLENOID_BACKWARD_PORT_ID);
  
  private WPI_TalonSRX balancerTalon = new WPI_TalonSRX(Constants.BALANCER_TALON_ID);

  private XboxController assistantDriverController = new XboxController(Constants.ASSISTANT_DRIVER_CONTROLLER_PORT_ID);
  /**
   * Creates a new EndGameSubsytem.
   */

  public void endGame()
  {
    if (assistantDriverController.getBumperPressed(Hand.kLeft))
    {
      liftSolenoid.set(DoubleSolenoid.Value.kForward);
      releaseClimbRope();
    }
    if (assistantDriverController.getBumperPressed(Hand.kRight))
    {
      liftSolenoid.set(DoubleSolenoid.Value.kReverse);
      pullInClimbRope();
    }

    if (assistantDriverController.getBackButton())
    {
      balance();
    }
  }

  private void balance()
  {
    
  }

  private void releaseClimbRope()
  {
    liftController.set(ControlMode.PercentOutput, 0.2);
  }

  private void pullInClimbRope()
  {
    liftController.set(ControlMode.PercentOutput, -0.2);
  }
  public EndGameSubsytem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
