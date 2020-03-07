/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * IDs for Spark Max Controllers on the bot 
     */
    public static final int LEFT_FRONT_SPARK_MAX_ID = 4;
    public static final int LEFT_BACK_SPARK_MAX_ID = 2;
    public static final int RIGHT_FRONT_SPARK_MAX_ID = 3;
    public static final int RIGHT_BACK_SPARK_MAX_ID = 1;

    /**
     * Speed modifiers for Teleop
     */
    public static final double TELEOP_DRIVE_SPEED_MODIFIER = 1.0;
    public static final double SHOOTER_TARGETING_TURNING_SPEED = 0.2;
    public static final double NO_SPEED = 0.0;
    public static final double LIFT_CONTROLLER_SPEED = 0.75;
    public static final double SHOOTER_MAGAZINE_OUTTAKE_SPEED = 0.3;

    /**
     * Speed modifiers for Autonomous
     */
    public static final double AUTONOMOUS_RELEASE_INTAKE_MANIPULATOR_SPEED = 0.2;
    public static final double AUTONOMOUS_MOVEMENT_SPEED = 0.35;
    public static final double AUTONOMOUS_MOVEMENT_SPEED_SLOW = 0.2;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_ONE = 0.4;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO = 0.4;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_THREE = 0.4;

    /**
     * Motor types for the Spark Max Controller
     */
    public static final MotorType BRUSHLESS_MOTOR = MotorType.kBrushless;
    public static final MotorType BRUSHED_MOTOR = MotorType.kBrushed;

    /**
     * Port IDs for various controllers used for driving
     */
    public static final int XBOX_ASSISTANT_DRIVER_CONTROLLER_ID = 1;
    public static final int XBOX_DRIVER_CONTROLLER_PORT_ID = 4;
    public static final int DRIVER_JOYSTICK_X_PORT_ID = 1;
    public static final int DRIVER_JOYSTICK_Y_PORT_ID = 0;
    
    /**  
     * IDs for Victor and Talon motor controllers
     */
    public static final int BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID = 3;
    public static final int SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID = 1;
    public static final int SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID = 2;
    public static final int MAGAZINE_CONTROLLER_VICTOR_SPX_ID = 4;
    public static final int LIFT_VICTOR_SPX_CONTROLLER_ID = 0;
    public static final int BALANCER_TALON_ID = 5;
    public static final int PIGEON_TALON_PORT_ID = 1;

    /**
     * ControlMode used to declare Victors to set speed with .set()
     */
    public static final ControlMode SPEED_CONTROL = ControlMode.PercentOutput;

    /**
     * Distances and area calculations used for limelight targeting
     */
    public static final double LIMELIGHT_TARGET_FOUND = 1.0; //tv
    public static final double LIMELIGHT_X_RANGE_MAXIMUM = 5.0; //range for which limelight is considered centered
    public static final double LIMELIGHT_AREA_FOUND_MINIMUM = 1.2; //minimum area for target to be considered found
    public static final double LIMELIGHT_AREA_FOUND_MAXIMUM = 3.0; //maximum area where target is reachable
    public static final double LIMELIGHT_TARGETING_AREA_LARGE_VALUE = 3.0; //11-15 ft away ta value
    public static final double LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE = 2.9; //9-11 ft away ta value
    public static final double LIMELIGHT_TARGETING_AREA_SMALL_VALUE = 2.3; //7-9 ft awat ta value
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_LARGE_VALUE = 1.0; //11-15 ft shooter speed
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE = 0.8; //9-11 ft shooter speed
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_SMALL_VALUE = 0.7; //7-9 ft shooter speed

    /**
     * Codes used to turn limelight camera on and off throughout the match
     */
    public static final int LIMELIGHT_ON_CODE = 3;
    public static final int LIMELIGHT_OFF_CODE = 1;

    /**
     * Distances for rangefinder detecting power cells
     */
    public static final double RANGEFINDER_BALL_DETECTED_DISTANCE = 2.2; //2 inches is detected
    public static final double RANGEFINDER_BALL_AWAY_DISTANCE = 4.0; //More than 4 inches is away

    /**
     * Port Ids for Solenoids off of PCM
     */
    public static final int LIFT_SOLENOID_FORWARD_PORT_ID = 0;
    public static final int LIFT_SOLENOID_BACKWARD_PORT_ID = 7;
    public static final int BRAKE_SOLENOID_FORWARD_PORT_ID = 1; 
    public static final int BRAKE_SOLENOID_BACKWARD_PORT_ID = 2;

    /**
     * Speeds for pigeon balancer at end of game
     */
    public static final double PIGEON_WHEEL_FAST_TALON_SPEED = 1.0;
    public static final double PIGEON_WHEEL_SLOW_TALON_SPEED = 0.5;
    public static final double PIGEON_WHEEL_STATIONARY_SPEED = 0.0;

    /**
     * Degrees which determine whether or not the bot is close to being balanced
     */
    public static final double PIDGEON_BALANCING_FAR_FROM_BALANCED = 15.0;
    public static final double PIDGEON_BALANCING_CLOSE_TO_BALANCED = 2.0;

    /**
     * Solenoid values
     */
    public static final Value SOLENOID_OFF = Value.kOff;
    public static final Value SOLENOID_FORWARD = Value.kForward;
    public static final Value SOLENOID_REVERSE = Value.kReverse;
}
