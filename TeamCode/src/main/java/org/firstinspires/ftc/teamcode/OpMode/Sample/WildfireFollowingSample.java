package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.github.bouyio.cyancore.util.PIDCoefficients;
import com.github.bouyio.cyancore.util.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.TankDriveConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.WildfireCamera;

@TeleOp()
public class WildfireFollowingSample extends OpMode {

    private WildfireCamera wildfireCamera;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;

    private PIDController autotargetPID;
    private PIDCoefficients autoTargetCoefficients;
    private PIDController autoDrivePID;
    private PIDCoefficients autoDriveCoefficients;

    @Override
    public void init() {
        wildfireCamera = new WildfireCamera(hardwareMap, telemetry);

        leftMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.LEFT_DRIVE_MOTOR_ID);
        rightMotor = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.RIGHT_DRIVE_MOTOR_ID);

        leftMotor.setDirection(TankDriveConfig.LEFT_MOTOR_DIRECTION);
        rightMotor.setDirection(TankDriveConfig.RIGHT_MOTOR_DIRECTION);

        autoTargetCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_TARGET_KP, TankDriveConfig.AUTO_TARGET_KI, TankDriveConfig.AUTO_TARGET_KD);
        autotargetPID = new PIDController(autoTargetCoefficients);

        autoDriveCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_DRIVE_KP, TankDriveConfig.AUTO_DRIVE_KI, TankDriveConfig.AUTO_DRIVE_KD);
        autoDrivePID = new PIDController(autoDriveCoefficients);
    }

    @Override
    public void loop() {
        double[] frame = wildfireCamera.getScaledLumaValues();

        autoTargetCoefficients.kP = TankDriveConfig.AUTO_TARGET_KP;
        autoTargetCoefficients.kI = TankDriveConfig.AUTO_TARGET_KI;
        autoTargetCoefficients.kD = TankDriveConfig.AUTO_TARGET_KD;

        autoDriveCoefficients.kP = TankDriveConfig.AUTO_DRIVE_KP;
        autoDriveCoefficients.kI = TankDriveConfig.AUTO_DRIVE_KI;
        autoDriveCoefficients.kD = TankDriveConfig.AUTO_DRIVE_KD;

        double turnError =
                (frame[WildfireCamera.RIGHT_UPPER_SECTION_ID] + frame[WildfireCamera.RIGHT_LOWER_SECTION_ID]) / 2
                        - (frame[WildfireCamera.LEFT_LOWER_SECTION_ID] + frame[WildfireCamera.LEFT_UPPER_SECTION_ID]) / 2;

        double driveError =
                (frame[WildfireCamera.LEFT_LOWER_SECTION_ID] + frame[WildfireCamera.RIGHT_LOWER_SECTION_ID]) / 2
                        - (frame[WildfireCamera.LEFT_UPPER_SECTION_ID] + frame[WildfireCamera.RIGHT_UPPER_SECTION_ID]) / 2;

        double turnPower = autotargetPID.update(turnError);
        double drivePower = autoDrivePID.update(driveError);

        leftMotor.setPower(drivePower + turnPower);
        rightMotor.setPower(drivePower - turnPower);

        telemetry.addData("Left Lower Luma", frame[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", frame[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", frame[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", frame[WildfireCamera.RIGHT_UPPER_SECTION_ID]);
    }
}
