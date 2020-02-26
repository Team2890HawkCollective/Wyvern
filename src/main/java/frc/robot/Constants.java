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
}
