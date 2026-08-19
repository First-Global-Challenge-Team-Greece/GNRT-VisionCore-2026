package org.firstinspires.ftc.teamcode.OpMode.Sample;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.TankDriveLight;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.WildfireCamera;

@TeleOp()
public class WildfireFollowingSample extends OpMode {

    private WildfireCamera wildfireCamera;
    private TankDriveLight tankDrive;

    @Override
    public void init() {
        wildfireCamera = new WildfireCamera(hardwareMap, telemetry);
        tankDrive = new TankDriveLight(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        tankDrive.driveToWildfire(wildfireCamera.getScaledLumaValues());

        telemetry.addData("Left Lower Luma", wildfireCamera.getScaledLumaValues()[WildfireCamera.LEFT_LOWER_SECTION_ID]);
        telemetry.addData("Left Upper Luma", wildfireCamera.getScaledLumaValues()[WildfireCamera.LEFT_UPPER_SECTION_ID]);
        telemetry.addData("Right Lower Luma", wildfireCamera.getScaledLumaValues()[WildfireCamera.RIGHT_LOWER_SECTION_ID]);
        telemetry.addData("Right Upper Luma", wildfireCamera.getScaledLumaValues()[WildfireCamera.RIGHT_UPPER_SECTION_ID]);
    }
}
