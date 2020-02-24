/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class SensorSubsystem extends SubsystemBase 
{
  private WPI_TalonSRX pidgeonTalon = new WPI_TalonSRX(Constants.PIGEON_TALON_PORT_ID);

  private double [] ypr = new double[3];

  private PigeonIMU _pigeon = new PigeonIMU(pidgeonTalon);

  private int pitchId = 1;

  public PigeonIMU getPigeon()
  {
    return _pigeon;
  }

  public double[] getPidgeonYpr()
  {
    _pigeon.getYawPitchRoll(ypr);
    return ypr;
  }
  
  /**
   * Creates a new SensorSubsystem.
   */
  public SensorSubsystem() 
  {

  }

  private void stopPidgeon()
  {
    //pidgeonTalon.set(Constants.PIGEON_STATIONARY_SPEED);
  }

  private void turnPidgeonRight()
  {
    //pidgeonTalon.set(Constants.PIGEON_TALON_SPEED_RIGHT);
  }

  private void turnPidgeonRightSlow()
  {
    //pidgeonTalon.set(Constants.PIGEON_SLOW_TALON_SPEED_RIGHT);
  }

  private void turnPidgeonLeft()
  {
    //pidgeonTalon.set(Constants.PIGEON_TALON_SPEED_LEFT);
  }

  private void turnPidgeonLeftSlow()
  {
    //pidgeonTalon.set(Constants.PIGEON_SLOW_TALON_SPEED_LEFT);
  }

  public void balancePidgeon()
  {
    /*if(ypr[pitchId] > -0.5 && ypr[pitchId] < 0.5)
    {
      //stopPidgeon();
      System.out.println("Stop pidgeon");
    }
    else if(ypr[pitchId] < -1)
    {
      //turnPidgeonLeft();
      System.out.println("Move left");
    }
    else if(ypr[pitchId] > 1)
    {
      //turnPidgeonRight();
      System.out.println("Move right");
    }*/
  }




  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

