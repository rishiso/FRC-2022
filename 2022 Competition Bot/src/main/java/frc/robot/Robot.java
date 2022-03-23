package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;


public class Robot extends TimedRobot {
  
  //Dashboard vars
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Drive Vars
  private DifferentialDrive m_drive;

  //Control Vars
  private Joystick m_stick;

  //Sensor Vars
  private NetworkTable limelight;

  //Shooting Vars
  PWMSparkMax m_shooter;

  @Override
  public void robotInit() {

    //Dashboard Init
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    //Drive Init
    PWMSparkMax m_left = new PWMSparkMax(RobotMap.M_LEFT);
    PWMSparkMax m_right = new PWMSparkMax(RobotMap.M_RIGHT);

    m_drive = new DifferentialDrive(m_left, m_right);

    //Control Init
    m_stick = new Joystick(RobotMap.JOYSTICK);

    //Limelight Init
    limelight = NetworkTableInstance.getDefault().getTable("limelight");

    //Shoot Init
    m_shooter = new PWMSparkMax(RobotMap.M_SHOOTER);
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
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    double sensitivity = m_stick.getThrottle(); // 1 to -1
    sensitivity *= -1;
    sensitivity *= .25;
    sensitivity += .75;

    SmartDashboard.putNumber("Sensitivity: ", sensitivity);

    //Throttle allows control from .5 to 1
    m_drive.arcadeDrive(-sensitivity * m_stick.getZ(), sensitivity * m_stick.getY());

    //Shooting Control
    if (m_stick.getTrigger()) {
      m_shooter.set(-.5);
    } else if (m_stick.getRawButton(RobotMap.RELOAD)) {
      m_shooter.set(.5);
    } else {
      m_shooter.stopMotor();
    }
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
