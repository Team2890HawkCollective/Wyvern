/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANEncoder;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.AutonomousSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;




/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private WaitCommand m_wait = new WaitCommand(2.0);

  private RobotContainer m_robotContainer;

  private UsbCamera liftCamera = CameraServer.getInstance().startAutomaticCapture();
  private UsbCamera intakeCamera = CameraServer.getInstance().startAutomaticCapture();

  //Bottom
  public static DigitalInput bottomEcho = new DigitalInput(1);
  public static DigitalOutput bottomPing = new DigitalOutput(0);
  public static Ultrasonic bottomRangeFinder = new Ultrasonic(bottomPing, bottomEcho);
  //Top
  public static DigitalInput topEcho = new DigitalInput(3);
  public static DigitalOutput topPing = new DigitalOutput(2);
  public static Ultrasonic topRangeFinder = new Ultrasonic(topPing, topEcho);

  public static DigitalInput liftEcho = new DigitalInput(5);
  public static DigitalOutput liftPing = new DigitalOutput(4);
  public static Ultrasonic liftRangeFinder = new Ultrasonic(liftPing, liftEcho);

  public static SendableChooser<String> startingPositionChooser = new SendableChooser<>();

  public static Servo hookServo = new Servo(0);


  /*private final I2C.Port i2cPort = I2C.Port.kOnboard;

  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);*/
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() 
  {

    CommandScheduler.getInstance().cancelAll();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    startingPositionChooser.addOption("Move Off Line", "Move");
    startingPositionChooser.addOption("Target", "Target");
    startingPositionChooser.addOption("Test", "Test");

    Shuffleboard.getTab("Configuration").add("Robot Starting Position", startingPositionChooser);
    Shuffleboard.getTab("Main").add("Lift Camera", liftCamera);
    Shuffleboard.getTab("Main").add("Intake Camera", intakeCamera);
    Shuffleboard.getTab("Main").add("Count of Balls in Magazine", 0);

    //joystickDriveCommand.execute();

  }

  public RobotContainer getRobotContainer()
  {
    return m_robotContainer;
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    System.out.println(liftRangeFinder.getRangeInches());
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    //CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
   //m_autonomousCommand = m_robotContainer.getAutonomousCommand();
   NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }
  

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() 
  {
    m_robotContainer.getAutonomousCommand().execute();
  }

  @Override
  public void teleopInit() 
  {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) 
    {
      m_autonomousCommand.cancel();
    }

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1); //Turns off limelight
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() 
  {
    m_robotContainer.getManipulatorCommand().execute(); //runs manipulators
    m_robotContainer.getDriveTrainCommand().execute(); //runs drive train
    m_robotContainer.getEndGameCommand().execute();
  }

  @Override
  public void testInit() 
  {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() 
  {
  }
}
