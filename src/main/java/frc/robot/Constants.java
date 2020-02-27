/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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
    //Talon IDs
    public static final int PIGEON_TALON_PORT_ID = 5;

    //Controller Ports
    public static final int ASSISTANT_DRIVER_CONTROLLER_PORT = 2;

    //Pidgeon speeds for moving the balancer wheel
    public static final double PIGEON_WHEEL_FAST_TALON_SPEED_RIGHT = 0.1;
    public static final double PIGEON_WHEEL_SLOW_TALON_SPEED_RIGHT = 0.05;
    public static final double PIGEON_WHEEL_FAST_TALON_SPEED_LEFT = -0.1;
    public static final double PIGEON_WHEEL_SLOW_TALON_SPEED_LEFT = -0.05;
    public static final double PIGEON_WHEEL_STATIONARY_SPEED = 0.0;

    //Degrees upon wihch pidgeon measures distance from album
    public static final double PIDGEON_BALANCING_FAR_FROM_BALANCED = 20.0;
    public static final double PIDGEON_BALANCING_CLOSE_TO_BALANCED = 5.0;
}
