package org.firstinspires.ftc.teamcode.Util;

public class TelemetryDivider {
    public static String generate(String dividerText, int size) {
        StringBuilder divider = new StringBuilder();
        divider.append('|');

        for (int i = 0; i < size*2; i++) {
            divider.append('-');

            if (i == size - 1) {
                divider.append(' ');
                divider.append(dividerText);
                divider.append(' ');
            }
        }

        divider.append('|');

        return divider.toString();
    }
}
