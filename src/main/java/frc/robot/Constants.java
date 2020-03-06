/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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
    public static final int BALL_PICKUP_CONTROLLER_VICTOR_SPX_ID = 3;
    public static final int SHOOTER_CONTROLLER_LEFT_SIDE_VICTOR_SPX_ID = 1;
    public static final int SHOOTER_CONTROLLER_RIGHT_SIDE_VICTOR_SPX_ID = 2;
    public static final int MAGAZINE_CONTROLLER_VICTOR_SPX_ID = 4;

    public static int LEFT_FRONT_SPARK_CONTROLLER_ID = 2;
    public static int LEFT_BACK_SPARK_CONTROLLER_ID = 4;
    public static int RIGHT_FRONT_SPARK_CONTROLLER_ID = 1;
    public static int RIGHT_BACK_SPARK_CONTROLLER_ID = 3;

    public static final MotorType BRUSHLESS_MOTOR = MotorType.kBrushless;

    public static final double SHOOTER_TARGETING_TURNING_SPEED = 0.2;

    public static final int XBOX_ASSISTANT_DRIVER_CONTROLLER_ID = 2;

    public static final double NO_SPEED = 0.0;

    public static final int BOTTOM_MAGAZINE_SENSOR_PORT = 0;
    public static final int TOP_MAGAZINE_SENSOR_PORT = 1;

    public static final double LIMELIGHT_TARGET_FOUND = 1.0;
    public static final double LIMELIGHT_X_RANGE_MAXIMUM = 5.0;
    public static final int LIMELIGHT_ON_CODE = 3;
    public static final int LIMELIGHT_OFF_CODE = 1;
    public static final double LIMELIGHT_AREA_FOUND_MINIMUM = 1.2;

    public static final double LIMELIGHT_TARGETING_AREA_LARGE_VALUE = 3.0;
    public static final double LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE = 2.9;
    public static final double LIMELIGHT_TARGETING_AREA_SMALL_VALUE = 2.3;
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_LARGE_VALUE = 1.0;
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_MEDIUM_VALUE = 0.8;
    public static final double SHOOTER_SPEED_LIMELIGHT_TARGETING_AREA_SMALL_VALUE = 0.7;
    public static final double LIMELIGHT_TARGETING_AREA_WHITE_LINE_VALUE = 2.8; 
}
