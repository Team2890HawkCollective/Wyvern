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
public final class Constants 
{
    public static final int RIGHT_BACK_TALON_ID = 4;
    public static final int RIGHT_FRONT_TALON_ID = 2;
    public static final int LEFT_FRONT_TALON_ID = 1;
    public static final int LEFT_BACK_TALON_ID = 3;

    public static final int LEFT_FRONT_SPARK_MAX_ID = 1;
    public static final int LEFT_BACK_SPARK_MAX_ID = 2;
    public static final int RIGHT_FRONT_SPARK_MAX_ID = 3;
    public static final int RIGHT_BACK_SPARK_MAX_ID = 4;

    public static final MotorType BRUSHLESS_MOTOR = MotorType.kBrushless;
    public static final MotorType BRUSHED_MOTOR = MotorType.kBrushed;


    public static final int DRIVER_CONTROLLER_PORT_ID = 2;
    public static final int DRIVER_JOYSTICK_X_PORT_ID = 1;
    public static final int DRIVER_JOYSTICK_Y_PORT_ID = 0;
    public static final double TELEOP_FORWARD_SPEED_MODIFIER = 0.4;

    public static final double TELEOP_BACKWARDS_SPEED_MODIFIER = -0.4;

    public static final double NOT_MOVING = 0.0;


    
}
