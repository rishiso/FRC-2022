package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;


public class Robot extends TimedRobot {
  
  //Dashboard vars
  private static final int kDefaultAuto = 1;
  private static final int kSensorAuto = 2;
  private int m_autoSelected;
  private final SendableChooser<Integer> m_chooser = new SendableChooser<>();

  //Drive Vars
  private DifferentialDrive m_drive;

  //Control Vars
  private Joystick m_stick;

  //Sensor Vars
  private NetworkTable limelight;

  //Shooting Vars
  private PWMSparkMax m_shooter;

  //Autonomous Vars
  private Timer tim;

  @Override
  public void robotInit() {

    //Dashboard Init
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Sensor Auto", kSensorAuto);
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
    tim.start();
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kDefaultAuto:
        //Drives forward and shoots

        double curTim = tim.get();

        if (curTim <= 2) {
          m_drive.arcadeDrive(0, .3);
        } else if (curTim <= 3.5) {
          m_shooter.set(.8);
        } else {
          m_drive.arcadeDrive(0, 0);
          m_shooter.stopMotor();
        }

        break;
      
      case kSensorAuto:
        // Sensor based
        /*
        if (!corrrect distance) {
          m_drive.arcadeDrive(0, .3);
          tim.reset();
        } else {
          m_drive.arcadeDrive(0, 0);
          if (tim.get() <= 1.5) {
            m_shooter.set(.8);
          } else {
            m_shooter.stopMotor();
          }
        }
        */

        break;
      
      default:
        // Do Nothing
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

    //Throttle allows control from .5 to 1
    m_drive.arcadeDrive(-sensitivity * m_stick.getZ(), sensitivity * m_stick.getY());

    //Shooting Control
    if (m_stick.getRawButton(RobotMap.SHOOT)) {
      m_shooter.set(.8);
    } else if (m_stick.getRawButton(RobotMap.RELOAD)) {
      m_shooter.set(-.8);
    } else {
      m_shooter.stopMotor();
    }

    SmartDashboard.putNumber("Sensitivity: ", sensitivity);
    SmartDashboard.putNumber("Y-Input: ", m_stick.getY());
    SmartDashboard.putNumber("Z-Input: ", m_stick.getZ());
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
