package org.firstinspires.ftc.teamcode.math;

public class ShooterEquations {

    public double targetSpeedLut(double distance) {
        return (((-0.00000879766 * distance
                + 0.00418244) * distance
                - 0.718524) * distance
                + 66.7453) * distance
                + 538.50741;
    }
}
