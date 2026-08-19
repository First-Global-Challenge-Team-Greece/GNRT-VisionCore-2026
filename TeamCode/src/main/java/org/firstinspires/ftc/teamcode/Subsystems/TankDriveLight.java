package org.firstinspires.ftc.teamcode.Subsystems;



import com.github.bouyio.cyancore.util.*;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.TankDriveConfig;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.WildfireCamera;

/**
 * <p>A partial implementation of <a href="https://github.com/bouyio">tank drive</a></p>
 * */
public class TankDriveLight {

    private PIDController autotargetPID;
    private PIDCoefficients autoTargetCoefficients;
    private PIDController autoDrivePID;
    private PIDCoefficients autoDriveCoefficients;



    private final DcMotorEx leftDrive;
    private final DcMotorEx rightDrive;

    public TankDriveLight(HardwareMap hardwareMap, Telemetry telemetry) {
        leftDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.left_drive_motor_id);
        rightDrive = hardwareMap.get(DcMotorEx.class, HardwareMapConfig.right_drive_motor_id);

        leftDrive.setDirection(TankDriveConfig.LEFT_MOTOR_DIRECTION);
        rightDrive.setDirection(TankDriveConfig.RIGHT_MOTOR_DIRECTION);

        leftDrive.setZeroPowerBehavior(TankDriveConfig.MOTOR_ZERO_POWER_BEHAVIOR);
        rightDrive.setZeroPowerBehavior(TankDriveConfig.MOTOR_ZERO_POWER_BEHAVIOR);


        autoTargetCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_TARGET_KP, TankDriveConfig.AUTO_TARGET_KI, TankDriveConfig.AUTO_TARGET_KD);
        autotargetPID = new PIDController(autoTargetCoefficients);

        autoDriveCoefficients = new PIDCoefficients(TankDriveConfig.AUTO_DRIVE_KP, TankDriveConfig.AUTO_DRIVE_KI, TankDriveConfig.AUTO_DRIVE_KD);
        autoDrivePID = new PIDController(autoDriveCoefficients);


        telemetry.addData("Drive Train", "INITIALIZED");
    }

    public void driveRobotCentric(double forward, double turn) {
        double denominator = Math.max(Math.abs(forward) + Math.abs(turn), 1);
        double leftPower = (forward + turn) / denominator;
        double rightPower = (forward - turn) / denominator;

        setPowers(leftPower, rightPower);
    }

    private void setPowers(double leftPower, double rightPower) {
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public void driveToWildfire(double[] lumaValues) {
        autoTargetCoefficients.kP = TankDriveConfig.AUTO_TARGET_KP;
        autoTargetCoefficients.kI = TankDriveConfig.AUTO_TARGET_KI;
        autoTargetCoefficients.kD = TankDriveConfig.AUTO_TARGET_KD;

        autoDriveCoefficients.kP = TankDriveConfig.AUTO_DRIVE_KP;
        autoDriveCoefficients.kI = TankDriveConfig.AUTO_DRIVE_KI;
        autoDriveCoefficients.kD = TankDriveConfig.AUTO_DRIVE_KD;

        double leftLowerLuma = lumaValues[WildfireCamera.LEFT_LOWER_SECTION_ID];
        double rightLowerLuma = lumaValues[WildfireCamera.RIGHT_LOWER_SECTION_ID];
        double leftUpperLuma = lumaValues[WildfireCamera.LEFT_UPPER_SECTION_ID];
        double rightUpperLuma = lumaValues[WildfireCamera.RIGHT_UPPER_SECTION_ID];

        double turnError = (rightUpperLuma + rightLowerLuma) / 2 - (leftLowerLuma + leftUpperLuma) / 2;
        double driveError = (leftLowerLuma + rightLowerLuma) / 2 - (leftUpperLuma + rightUpperLuma)  / 2;

        driveRobotCentric(autoDrivePID.update(driveError), autotargetPID.update(turnError));
    }


}
