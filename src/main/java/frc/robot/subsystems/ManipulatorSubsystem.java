/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ManipulatorSubsystem extends SubsystemBase {
  private WPI_TalonSRX liftTalon = new WPI_TalonSRX(Constants.LIFT_TALON_ID);

  private Compressor liftCompressor = new Compressor();
  private Solenoid liftSolenoid = new Solenoid(Constants.LIFE_SOLENOID_ID);

  private XboxController assistantDriverController = new XboxController(Constants.XBOX_ASSISTANT_DRIVER_CONTROLLER_PORT_ID);

  /**
   * Creates a new ManipulatorSubsystem.
   */
  public ManipulatorSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (assistantDriverController.getAButtonPressed() == true)
    {
      liftSolenoid.set(true);
    }
  }
}
