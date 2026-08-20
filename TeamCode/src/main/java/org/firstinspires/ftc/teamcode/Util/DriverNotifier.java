package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriverNotifier {

    public enum Channel {
        LEFT, RIGHT
    }

    public enum MessageLevel {
        WARNING(new Gamepad.RumbleEffect.Builder().addStep(1, 0, 300).addStep(0, 1, 300).build()),
        ERROR(new Gamepad.RumbleEffect.Builder().addStep(0, 1, 600).addStep(1, 0, 600).build()),
        CRITICAL(new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 300)
                .addStep(0, 1, 300)
                .addStep(1, 0, 300)
                .addStep(0, 1, 300)
                .build());

        MessageLevel(Gamepad.RumbleEffect pattern) {
            this.pattern = pattern;
        }

        public final Gamepad.RumbleEffect pattern;
    }

    private final Telemetry telemetry;
    private final Gamepad gamepad;

    public double notificationVolume = 0.5;
    public int rumblePaddingMS = 300;

    private long previousNotificationTime = 0;
    private int minimumDeltaTime = rumblePaddingMS;


    public DriverNotifier(Telemetry telemetry, Gamepad gamepad) {
        this.telemetry = telemetry;
        this.gamepad = gamepad;
    }

    public void createStereoNotification(int durationMS) {
        if ((long) (System.currentTimeMillis() - previousNotificationTime) < minimumDeltaTime) {
            return;
        }

        minimumDeltaTime = rumblePaddingMS + durationMS;
        previousNotificationTime = System.currentTimeMillis();

        gamepad.rumble(notificationVolume, notificationVolume, durationMS);
    }

    public void createMonoNotification(Channel notificationChannel, int durationMS) {
        if (System.currentTimeMillis() - previousNotificationTime < minimumDeltaTime)
            return;

        minimumDeltaTime = rumblePaddingMS + durationMS;
        previousNotificationTime = System.currentTimeMillis();

        gamepad.rumble(
                notificationChannel == Channel.LEFT ? notificationVolume : 0,
                notificationChannel == Channel.RIGHT ? notificationVolume : 0,
                durationMS
        );
    }

    public void createPatternNotification(Gamepad.RumbleEffect pattern) {
        if (System.currentTimeMillis() - previousNotificationTime < minimumDeltaTime)
            return;

        minimumDeltaTime = rumblePaddingMS;

        for (Gamepad.RumbleEffect.Step patternStep : pattern.steps) {
            minimumDeltaTime += patternStep.duration;
        }

        previousNotificationTime = System.currentTimeMillis();

        gamepad.runRumbleEffect(pattern);
    }

    public void sendMessage(MessageLevel messageLevel) {
        createPatternNotification(messageLevel.pattern);
    }

}
