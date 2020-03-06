/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static int LEFT_FRONT_TALON_ID = 1;
    public static int LEFT_BACK_TALON_ID = 3;
    public static int RIGHT_FRONT_TALON_ID = 2;
    public static int RIGHT_BACK_TALON_ID = 4;

    public static int LEFT_FRONT_SPARK_CONTROLLER_ID = 1;
    public static int LEFT_BACK_SPARK_CONTROLLER_ID = 2;
    public static int RIGHT_FRONT_SPARK_CONTROLLER_ID = 3;
    public static int RIGHT_BACK_SPARK_CONTROLLER_ID = 4;

    public static MotorType BRUSHLESS_MOTOR = MotorType.kBrushless;
    public static MotorType BRUSHED_MOTOR = MotorType.kBrushed;

    public static final double AUTONOMOUS_MOVEMENT_SPEED = 0.35;
    public static final double AUTONOMOUS_MOVEMENT_SPEED_SLOW = 0.2;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_ONE = 0.4;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_TWO = 0.4;
    public static final double AUTONOMOUS_SHOOTER_SPEED_POSITION_THREE = 0.4;

    public static final double NO_SPEED = 0.0;

    public static final double LIMELIGHT_TARGET_FOUND = 1.0;
    public static final double LIMELIGHT_X_VALUE_RANGE = 3.0;
    public static final double LIMELIGHT_AREA_MAXIMUM_VALUE = 3.0;
    public static final double LIMELIGHT_AREA_MINIMUM_VALUE = 1.5;

    public static final int SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID = 1;
    public static final int SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID = 2;
    public static final int MAGAZINE_CONTROLLER_VICTOR_SPX_ID = 4;

    /**
     * Distances for rangefinder detecting power cells
     */
    public static final double RANGEFINDER_BALL_DETECTED_DISTANCE = 2.2; //2 inches is detected
    public static final double RANGEFINDER_BALL_AWAY_DISTANCE = 4.0; //More than 4 inches is away

    public static final ControlMode SPEED_CONTROL = ControlMode.PercentOutput;

    public static final double SHOOTER_MAGAZINE_OUTTAKE_SPEED = 0.3;
}
