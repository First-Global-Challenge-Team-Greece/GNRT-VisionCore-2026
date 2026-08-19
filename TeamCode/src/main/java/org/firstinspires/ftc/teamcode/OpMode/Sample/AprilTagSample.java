package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.TagCamera;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Optional;

@TeleOp()
public class AprilTagSample extends OpMode {

    private TagCamera tagCamera;

    @Override
    public void init() {

        tagCamera = new TagCamera(hardwareMap, telemetry);

        telemetry.addLine("A sample for the April Tag detection.");
        telemetry.addLine("For live feed of the camera press the three dots on the right-hand corner of the driver station and select Camera Stream.");
        telemetry.addLine("Otherwise start the OpMode and monitor the April Tag detection metadata in the telemetry.");
    }

    @Override
    public void loop() {
        Optional<AprilTagDetection> closestTag = tagCamera.getClosestDetection();

        boolean closestTagExists = closestTag.isPresent() && closestTag.get().metadata != null;
        int id = closestTagExists ? closestTag.get().id : -1;

        double distance = closestTagExists ? closestTag.get().ftcPose.range : -1;
        double bearing = closestTagExists ? closestTag.get().ftcPose.bearing : 0;

        telemetry.addData("Closest Tag", id);
        telemetry.addData("Distance From Tag", distance);
        telemetry.addData("Tag Bearing", bearing);
    }
}
