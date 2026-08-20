package org.firstinspires.ftc.teamcode.Subsystems.Vision;

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

public class WildfireVisualProcessorV3 implements VisionProcessor {

    private Telemetry telemetry = null;

    private final int SCREEN_HEIGHT = 480;
    private final int SCREEN_WIDTH = 640;

    private final Rect leftLowerRectangle = new Rect(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
    private final Rect rightLowerRectangle = new Rect(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT / 2);
    private final Rect leftUpperRectangle = new Rect(0, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
    private final Rect rightUpperRectangle = new Rect(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH, SCREEN_HEIGHT / 2);

    private org.opencv.core.Rect leftLowerSection;
    private org.opencv.core.Rect rightLowerSection;
    private org.opencv.core.Rect leftUpperSection;
    private org.opencv.core.Rect rightUpperSection;

    private double leftLowerLuma = 0;
    private double rightLowerLuma = 0;
    private double leftUpperLuma = 0;
    private double rightUpperLuma = 0;

    private final Scalar lower = new Scalar(5, 128, 50);
    private final Scalar upper = new Scalar(30, 255, 255);

    private Mat hsvMat = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();

    public WildfireVisualProcessorV3(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        leftLowerSection = new org.opencv.core.Rect(0, height/2, width / 2, height/2);
        rightLowerSection = new org.opencv.core.Rect(width / 2, height/2, width / 2, height/2);
        leftUpperSection = new org.opencv.core.Rect(0, 0, width / 2, height/2);
        rightUpperSection = new org.opencv.core.Rect(width / 2, 0, width / 2, height/2);
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        telemetry.clear();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(hsvMat, lower, upper, binaryMat);

        maskedInputMat.release();

        Core.bitwise_and(frame, frame, maskedInputMat, binaryMat);

        maskedInputMat.copyTo(frame);

        leftLowerLuma = getSectionLuma(maskedInputMat, leftLowerSection);
        rightLowerLuma = getSectionLuma(maskedInputMat, rightLowerSection);
        leftUpperLuma = getSectionLuma(maskedInputMat, leftUpperSection);
        rightUpperLuma = getSectionLuma(maskedInputMat, rightUpperSection);

        telemetry.addData("left luma", leftLowerLuma);
        telemetry.addData("right luma", rightLowerLuma);

        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint leftLowerPaint = new Paint();
        leftLowerPaint.setStrokeWidth(5);
        leftLowerPaint.setStyle(Paint.Style.STROKE);
        leftLowerPaint.setARGB(255, 255, (int) (255 * leftLowerLuma * 10), 0);

        canvas.drawRect(new Rect(
                (int) (leftLowerRectangle.left * scaleBmpPxToCanvasPx),
                (int) (leftLowerRectangle.top * scaleBmpPxToCanvasPx),
                (int) (leftLowerRectangle.right * scaleBmpPxToCanvasPx),
                (int) (leftLowerRectangle.bottom * scaleBmpPxToCanvasPx)
                ), leftLowerPaint);

        Paint rightLowerPaint = new Paint();
        rightLowerPaint.setStrokeWidth(5);
        rightLowerPaint.setStyle(Paint.Style.STROKE);
        rightLowerPaint.setARGB(255, 255, (int) (255 * rightLowerLuma * 10), 0);

        canvas.drawRect(new Rect(
                (int) (rightLowerRectangle.left * scaleBmpPxToCanvasPx),
                (int) (rightLowerRectangle.top * scaleBmpPxToCanvasPx),
                (int) (rightLowerRectangle.right * scaleBmpPxToCanvasPx),
                (int) (rightLowerRectangle.bottom * scaleBmpPxToCanvasPx)
        ), rightLowerPaint);

        Paint leftUpperPaint = new Paint();
        leftUpperPaint.setStrokeWidth(5);
        leftUpperPaint.setStyle(Paint.Style.STROKE);
        leftUpperPaint.setARGB(255, 255, (int) (255 * leftLowerLuma * 10), 0);

        canvas.drawRect(new Rect(
                (int) (leftUpperRectangle.left * scaleBmpPxToCanvasPx),
                (int) (leftUpperRectangle.top * scaleBmpPxToCanvasPx),
                (int) (leftUpperRectangle.right * scaleBmpPxToCanvasPx),
                (int) (leftUpperRectangle.bottom * scaleBmpPxToCanvasPx)
                ), leftUpperPaint);

        Paint rightUpperPaint = new Paint();
        rightUpperPaint.setStrokeWidth(5);
        rightUpperPaint.setStyle(Paint.Style.STROKE);
        rightUpperPaint.setARGB(255, 255, (int) (255 * rightLowerLuma * 10), 0);

        canvas.drawRect(new Rect(
                (int) (rightUpperRectangle.left * scaleBmpPxToCanvasPx),
                (int) (rightUpperRectangle.top * scaleBmpPxToCanvasPx),
                (int) (rightUpperRectangle.right * scaleBmpPxToCanvasPx),
                (int) (rightUpperRectangle.bottom * scaleBmpPxToCanvasPx)
        ), rightUpperPaint);
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

    public double getLeftLowerLuma() {
        return leftLowerLuma;
    }
    public double getLeftUpperLuma() {
        return leftUpperLuma;
    }

    public double getRightLowerLuma() {
        return rightLowerLuma;
    }
    public double getRightUpperLuma() {
        return rightUpperLuma;
    }

}
