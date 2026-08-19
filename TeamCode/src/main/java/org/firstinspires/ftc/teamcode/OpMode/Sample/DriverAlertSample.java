package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Config.DriverAlertConfig;
import org.firstinspires.ftc.teamcode.Subsystems.TankDriveLight;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.TagCamera;
import org.firstinspires.ftc.teamcode.Util.DriverNotifier;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Optional;

@TeleOp()
public class DriverAlertSample extends OpMode {

    private TagCamera tagCamera;
    private TankDriveLight tankDrive;
    private DriverNotifier driverNotifier;

    @Override
    public void init() {
        tankDrive = new TankDriveLight(hardwareMap, telemetry);

        tagCamera = new TagCamera(hardwareMap, telemetry);

        driverNotifier = new DriverNotifier(telemetry, gamepad1);
    }

    @Override
    public void loop() {
        Optional<AprilTagDetection> closestTag = tagCamera.getClosestDetection();
        boolean closestTagExists = closestTag.isPresent();
        int id = closestTagExists ? closestTag.get().id : -1;
        double distance = closestTagExists ? closestTag.get().ftcPose.range : -1;
        double bearing = closestTagExists ? closestTag.get().ftcPose.bearing : 0;

        tankDrive.driveRobotCentric(gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (closestTagExists && distance < DriverAlertConfig.MAXIMUM_DISTANCE
                && (-DriverAlertConfig.BEARING_MARGIN < bearing && bearing < DriverAlertConfig.BEARING_MARGIN)) {
            driverNotifier.sendMessage(DriverNotifier.MessageLevel.WARNING);
        }

        telemetry.addData("Closest Tag", id);
        telemetry.addData("Distance From Tag", distance);
        telemetry.addData("Tag Bearing", bearing);
    }
}
