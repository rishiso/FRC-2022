package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  
  //Dashboard vars
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Drive Vars
  private DifferentialDrive m_drive;
  private double speedFactor;

  //Control Vars
  private Joystick m_stick;

  @Override
  public void robotInit() {

    //Dashboard Init
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //Drive Init
    Talon m_left = new Talon(RobotMap.M_LEFT);
    Talon m_right = new Talon(RobotMap.M_RIGHT);

    m_drive = new DifferentialDrive(m_left, m_right);

    //Control Init
    m_stick = new Joystick(RobotMap.JOYSTICK);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    speedFactor = m_stick.getThrottle();
    speedFactor *= -1;
    speedFactor += 1;
    speedFactor = .25 * speedFactor + .5;
    //Ranges from 50% to 100%

    m_drive.arcadeDrive(-speedFactor * m_stick.getY(), speedFactor * m_stick.getZ());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
