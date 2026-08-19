package org.firstinspires.ftc.teamcode.Subsystems.Vision.Legacy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@Deprecated
public class WildfireVisualProcessorV2 implements VisionProcessor {

    private Telemetry telemetry = null;

    private final int SCREEN_HEIGHT = 480;
    private final int SCREEN_WIDTH = 640;

    private final Rect leftRectangle = new Rect(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
    private final Rect rightRectangle = new Rect(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT);

    private org.opencv.core.Rect leftSection;
    private org.opencv.core.Rect rightSection;

    private double leftLuma = 0;
    private double rightLuma = 0;

    private final Scalar lower = new Scalar(5, 128, 50);
    private final Scalar upper = new Scalar(30, 255, 255);

    private Mat hsvMat = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();

    public WildfireVisualProcessorV2(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        leftSection = new org.opencv.core.Rect(0, height / 2, width / 2, height / 2);
        rightSection = new org.opencv.core.Rect(width / 2, height / 2, width / 2, height / 2);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        telemetry.clear();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(hsvMat, lower, upper, binaryMat);

        maskedInputMat.release();

        Core.bitwise_and(frame, frame, maskedInputMat, binaryMat);

        maskedInputMat.copyTo(frame);

        leftLuma = getSectionLuma(maskedInputMat, leftSection);
        rightLuma = getSectionLuma(maskedInputMat, rightSection);

        telemetry.addData("left luma", leftLuma);
        telemetry.addData("right luma", rightLuma);

        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint leftPaint = new Paint();
        leftPaint.setStrokeWidth(5);
        leftPaint.setStyle(Paint.Style.STROKE);
        leftPaint.setARGB(255, 255, (int) (255 * leftLuma), 0);

        canvas.drawRect(new Rect(
                (int) (leftRectangle.left * scaleBmpPxToCanvasPx),
                (int) (leftRectangle.top * scaleBmpPxToCanvasPx),
                (int) (leftRectangle.right * scaleBmpPxToCanvasPx),
                (int) (leftRectangle.bottom * scaleBmpPxToCanvasPx)
                ), leftPaint);

        Paint rightPaint = new Paint();
        rightPaint.setStrokeWidth(5);
        rightPaint.setStyle(Paint.Style.STROKE);
        rightPaint.setARGB(255, 255, (int) (255 * rightLuma), 0);

        canvas.drawRect(new Rect(
                (int) (rightRectangle.left * scaleBmpPxToCanvasPx),
                (int) (rightRectangle.top * scaleBmpPxToCanvasPx),
                (int) (rightRectangle.right * scaleBmpPxToCanvasPx),
                (int) (rightRectangle.bottom * scaleBmpPxToCanvasPx)
        ), rightPaint);
    }

    private double getSectionLuma(Mat mat, org.opencv.core.Rect section) {
        try {
            Mat submat = new Mat(mat, section);
            Scalar hsv = Core.mean(submat);
            return hsv.val[2] / 255;
        } catch (CvException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public double getLeftLuma() {
        return leftLuma;
    }

    public double getRightLuma() {
        return rightLuma;
    }

}
