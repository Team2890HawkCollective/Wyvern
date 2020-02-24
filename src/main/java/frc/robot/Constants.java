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
    public static final int PIGEON_TALON_PORT_ID = 1;

    public static final double PIGEON_TALON_SPEED_RIGHT = 0.01;

    public static final double PIGEON_SLOW_TALON_SPEED_RIGHT = 0.001;

    public static final double PIGEON_TALON_SPEED_LEFT = -0.01;

    public static final double PIGEON_SLOW_TALON_SPEED_LEFT = -0.001;

    public static final double PIGEON_STATIONARY_SPEED = 0.0;
}
