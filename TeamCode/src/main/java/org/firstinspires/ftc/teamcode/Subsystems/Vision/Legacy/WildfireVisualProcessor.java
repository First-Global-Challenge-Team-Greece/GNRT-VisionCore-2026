package org.firstinspires.ftc.teamcode.Subsystems.Vision.Legacy;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;

@Deprecated
public class WildfireVisualProcessor implements VisionProcessor {

    private SimpleBlobDetector detector;
    private SimpleBlobDetector_Params detectorParams;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        detectorParams = new SimpleBlobDetector_Params();
        detectorParams.set_minThreshold(10);
        detectorParams.set_maxThreshold(1000);
//        detectorParams.set_filterByColor(true);
        detectorParams.set_filterByArea(true);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        detector = SimpleBlobDetector.create(detectorParams);

        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        detector.detect(frame, keyPoints);
        Features2d.drawKeypoints(frame, keyPoints, frame, new Scalar(0, 255, 0));

        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
