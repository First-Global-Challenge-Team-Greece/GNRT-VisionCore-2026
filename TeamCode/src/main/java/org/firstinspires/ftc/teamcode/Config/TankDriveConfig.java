package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class TankDriveConfig {
    public static double AUTO_TARGET_KP = 1.5;
    public static double AUTO_TARGET_KI = 0;
    public static double AUTO_TARGET_KD = 0.1;

    public static double AUTO_DRIVE_KP = 3;
    public static double AUTO_DRIVE_KI = 0;
    public static double AUTO_DRIVE_KD = 0.2;

    public static final DcMotorEx.Direction LEFT_MOTOR_DIRECTION = DcMotorEx.Direction.FORWARD;
    public static final DcMotorEx.Direction RIGHT_MOTOR_DIRECTION = DcMotorEx.Direction.REVERSE;
    public static final DcMotorEx.ZeroPowerBehavior MOTOR_ZERO_POWER_BEHAVIOR = DcMotorEx.ZeroPowerBehavior.BRAKE;
}
