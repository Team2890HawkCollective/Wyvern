/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.DriveTrainCommand;
import frc.robot.commands.EndGameCommand;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.AutonomousSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.EndGameSubsystem;
import frc.robot.commands.ManipulatorCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer 
{
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  //Ties DrivetrainSubsystem and DriveTrainCommand together
  private final DriveTrainSubsystem m_driveTrainSubsystem = new DriveTrainSubsystem();
  private final DriveTrainCommand m_driveTrainCommand = new DriveTrainCommand(m_driveTrainSubsystem);

  //Creates a manipulator subsystem and command
  private ManipulatorSubsystem m_manipulatorSubsystem = new ManipulatorSubsystem();
  private ManipulatorCommand m_manipulatorCommand = new ManipulatorCommand(m_manipulatorSubsystem);

  //Creates a end game subsystem and command
  private EndGameSubsystem m_endGameSubsystem = new EndGameSubsystem();
  private EndGameCommand m_endGameCommand = new EndGameCommand(m_endGameSubsystem);

  //Creates an autonomous subsystem and command
  private final AutonomousSubsystem m_autonomousSubsystem = new AutonomousSubsystem();
  private final AutonomousCommand m_autonomousCommand = new AutonomousCommand(m_autonomousSubsystem);


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() 
  {
    // Configure the button bindings
    configureButtonBindings();
    //init();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() 
  {

  }

  /**
   * Returns the command to run drive train during teleop
   */
  public Command getDriveTrainCommand()
  {
    //System.out.println("DRIVE TRAIN");
    return m_driveTrainCommand;
  }
  /**
   * Runs manipulators during teleop periodic
   */
  public Command getManipulatorCommand()
  {
    return m_manipulatorCommand;
  }

  public Command getEndGameCommand()
  {
    return m_endGameCommand;
  }

 


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autonomousCommand;
  }
}
