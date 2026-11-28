package org.firstinspires.ftc.teamcode.math;

import static org.firstinspires.ftc.teamcode.subsystem.constant.ShooterConstant.kD;
import static org.firstinspires.ftc.teamcode.subsystem.constant.ShooterConstant.kF;
import static org.firstinspires.ftc.teamcode.subsystem.constant.ShooterConstant.kI;
import static org.firstinspires.ftc.teamcode.subsystem.constant.ShooterConstant.kP;
import static org.firstinspires.ftc.teamcode.subsystem.constant.ShooterConstant.kS;

public class PIDFController {

    private double target = 0;
    private double error = 0;
    private double previousError = 0;
    private double integral = 0;
    private double derivative = 0;

    private long previousTime = 0;

    public PIDFController() {
    }

    public double calculate(double target, double currentSpeed) {
        this.target = target;
        long currentTime = System.nanoTime();

        error = target - currentSpeed;

        if (previousTime != 0) {
            double dt = (currentTime - previousTime) / 1e9;

            if (dt > 0) {
                derivative = (error - previousError) / dt;

                double candidateIntegral = integral + error * dt;

                double candidateOutput =
                        kP * error
                                + kI * candidateIntegral
                                + kD * derivative
                                + kF * target
                                + kS * Math.signum(target);

                if ((candidateOutput <= 1 || error < 0)
                        && (candidateOutput >= -1 || error > 0)) {
                    integral = candidateIntegral;
                }
            }
        }

        previousError = error;
        previousTime = currentTime;

        double output = kP * error
                + kI * integral
                + kD * derivative
                + kF * target
                + kS * Math.signum(target);

        return Math.max(-1, Math.min(1, output));
    }

    public double getError() {
        return error;
    }

    public double getTarget() {
        return target;
    }

    public boolean atTarget(double tolerance) {
        return Math.abs(error) <= tolerance;
    }

    public void reset() {
        error = 0;
        previousError = 0;
        integral = 0;
        derivative = 0;
        previousTime = 0;
    }
}
