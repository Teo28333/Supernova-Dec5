
package org.firstinspires.ftc.teamcode.subsystem;

import com.pedropathing.follower.Follower;

public class Heading_Lock {

    private final Follower follower;

    private final double midField = 70.75;
    private final double xHive = 84;
    private final double yHive = 48.7;
    private final double fieldSize = 141.5;

    private double kP = 2.0;
    private double kI = 0.0;
    private double kD = 0.0;

    private double integral = 0.0;
    private double lastError = 0.0;
    private long lastTime = 0;

    public Heading_Lock(Follower follower) {
        this.follower = follower;
    }

    public double getTargetY() {
        return follower.pose().y() > midField
                ? fieldSize - yHive
                : yHive;
    }

    public double getHeading() {
        double deltaX = xHive - follower.pose().x();
        double deltaY = getTargetY() - follower.pose().y();

        return Math.atan2(deltaY, deltaX);
    }

    private double squidController(double target, double current) {

        long now = System.nanoTime();

        double error = Math.atan2(
                Math.sin(target - current),
                Math.cos(target - current)
        );

        double dt = lastTime == 0
                ? 0.02
                : (now - lastTime) / 1e9;

        dt = Math.max(0.001, Math.min(dt, 0.1));

        integral += error * dt;

        double derivative = (error - lastError) / dt;

        lastError = error;
        lastTime = now;

        double output = kP * error
                + kI * integral
                + kD * derivative;

        return Math.max(-1.0, Math.min(1.0, output));
    }

    public double getTurningOutput() {
        return squidController(
                getHeading(),
                follower.pose().heading()
        );
    }

    public void setGains(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        reset();
    }

    public void reset() {
        integral = 0.0;
        lastError = 0.0;
        lastTime = 0;
    }
}
