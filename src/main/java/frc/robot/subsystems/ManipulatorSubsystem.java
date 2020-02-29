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

public class ManipulatorSubsystem extends SubsystemBase {
  
  private VictorSPX ballPickupController = new VictorSPX(Constants.BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID);

  private VictorSPX shooterLeftSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID);
  private VictorSPX shooterRightSideController = new VictorSPX(Constants.SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID);

  private VictorSPX magazineController = new VictorSPX(Constants.MAGAZINE_CONTROLLER_VICTOR_SPX_ID);
  
  
  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {

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
