package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Config.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Config.WildfireDetectionConfig;
import org.firstinspires.ftc.vision.VisionPortal;

public class WildfireCamera {
    private WebcamName camera;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private VisionPortal visionPortal;
    private WildfireVisualProcessorV3 visualProcessor;

    public static final int LEFT_LOWER_SECTION_ID = 0;
    public static final int RIGHT_LOWER_SECTION_ID = 1;
    public static final int LEFT_UPPER_SECTION_ID = 2;
    public static final int RIGHT_UPPER_SECTION_ID = 3;

    public WildfireCamera(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        camera = hardwareMap.get(WebcamName.class, HardwareMapConfig.WEBCAM_ID);

        visualProcessor = new WildfireVisualProcessorV3(telemetry);

        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(visualProcessor)
                .enableLiveView(true)
                .build();

        telemetry.addData("Camera Status", "Initialized");
    }

    public double[] getLumaValues() {
        return new double[]{
                visualProcessor.getLeftLowerLuma(),
                visualProcessor.getRightLowerLuma(),
                visualProcessor.getLeftUpperLuma(),
                visualProcessor.getRightUpperLuma()
        };
    }

    public double[] getScaledLumaValues() {
        return new double[]{
                visualProcessor.getLeftLowerLuma() * WildfireDetectionConfig.LOWER_LUMA_GAIN,
                visualProcessor.getRightLowerLuma() * WildfireDetectionConfig.LOWER_LUMA_GAIN,
                visualProcessor.getLeftUpperLuma() * WildfireDetectionConfig.UPPER_LUMA_GAIN,
                visualProcessor.getRightUpperLuma() * WildfireDetectionConfig.UPPER_LUMA_GAIN
        };
    }

}
