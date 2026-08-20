package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Optional;

import global.first.IgnitingInnovationGameDatabase;

public class TagCamera {
    private WebcamName camera;
    private Telemetry telemetry;
    private VisionPortal visionPortal;
    private AprilTagProcessor visualProcessor;


    public TagCamera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        camera = hardwareMap.get(WebcamName.class, HardwareMapConfig.WEBCAM_ID);

        visualProcessor = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(IgnitingInnovationGameDatabase.getIgnitingInnovationTagLibrary())
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(visualProcessor)
                .enableLiveView(true)
                .build();

    }

    public Optional<AprilTagDetection> getClosestDetection() {

        List<AprilTagDetection> detections = visualProcessor.getDetections();

        if (detections == null) return Optional.empty();

        AprilTagDetection closest = null;
        for (AprilTagDetection detection : detections) {
            if (detection == null) continue;
            if (detection.ftcPose == null) continue;

            if (closest == null) {
                closest = detection;
                continue;
            }

            if (closest.ftcPose.range > detection.ftcPose.range)
                closest = detection;
        }

        if (closest == null) return Optional.empty();

        return Optional.of(closest);
    }

    public Optional<Double> getDistanceFromClosest() {
        Optional<AprilTagDetection> closest = getClosestDetection();
        if (closest.isPresent()) {
            if (closest.get().ftcPose == null) return Optional.empty();
            return Optional.of(closest.get().ftcPose.range);
        }
        return Optional.empty();
    }


    public void debug() {
        List<AprilTagDetection> detections = visualProcessor.getDetections();
        List<AprilTagDetection> freshDetections = visualProcessor.getFreshDetections();

        telemetry.addData("Detections", detections == null ? "none" : detections);
        telemetry.addData("Fresh Detections", freshDetections == null ? "none" : freshDetections);
    }
}
