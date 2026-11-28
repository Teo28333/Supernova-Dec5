package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret_SS {

    private static final double MID_FIELD = 70.75;
    private static final double X_HIVE = 84.0;
    private static final double Y_HIVE = 48.7;
    private static final double FIELD_SIZE = 141.5;

    private final ServoImplEx servo1, servo2;
    private final Telemetry telemetry;
    private double minAngle, maxAngle;
    private double servo1AtMin, servo1AtMax, servo2AtMin, servo2AtMax;
    private boolean calibrated;
    private double lastCommandedAngle = 0.0;

    public Turret_SS(HardwareMap hwm, Telemetry telemetry, String servo1Name, String servo2Name) {
        this.telemetry = telemetry;
        servo1 = hwm.get(ServoImplEx.class, servo1Name);
        servo2 = hwm.get(ServoImplEx.class, servo2Name);
        servo1.setPwmRange(new PwmControl.PwmRange(500, 2500));
        servo2.setPwmRange(new PwmControl.PwmRange(500, 2500));
        // Servo shaft travel is +/-160 degrees; gearing gives turret travel of +/-270.
        // Assumes both servos increase counterclockwise and 0.5 points forward.
        configureTravel(Math.toRadians(-270), Math.toRadians(270), 0, 1, 0, 1);
    }

    /**
     * Calibrate the mechanical travel in radians relative to the robot: zero is
     * forward and positive angles are counterclockwise. Angles must be within
     * [-2*PI, 2*PI]. Supply each servo's safe position at BOTH angular endpoints;
     * descending positions support a mirrored servo. This does not move the turret.
     */
    public void configureTravel(double minAngle, double maxAngle,
                                double servo1AtMin, double servo1AtMax,
                                double servo2AtMin, double servo2AtMax) {
        if (!Double.isFinite(minAngle) || !Double.isFinite(maxAngle)
                || minAngle < -2 * Math.PI || maxAngle > 2 * Math.PI || minAngle >= maxAngle) {
            throw new IllegalArgumentException("Turret angles must satisfy -2*PI <= min < max <= 2*PI");
        }
        validatePosition(servo1AtMin);
        validatePosition(servo1AtMax);
        validatePosition(servo2AtMin);
        validatePosition(servo2AtMax);
        if (servo1AtMin == servo1AtMax || servo2AtMin == servo2AtMax) {
            throw new IllegalArgumentException("Each turret servo needs distinct endpoint positions");
        }
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.servo1AtMin = servo1AtMin;
        this.servo1AtMax = servo1AtMax;
        this.servo2AtMin = servo2AtMin;
        this.servo2AtMax = servo2AtMax;
        lastCommandedAngle = Math.max(minAngle, Math.min(maxAngle, lastCommandedAngle));
        calibrated = true;
    }

    /**
     * Update using the robot's field x/y in inches and heading in radians.
     * The target is mirrored across midfield according to the robot's y position.
     * Returns whether the requested angle is within travel, NOT measured alignment.
     * Targets outside travel are clamped to the nearest mechanical endpoint.
     */
    public boolean update(double x, double y, double heading) {
        if (!calibrated) {
            throw new IllegalStateException("Configure turret travel before aiming");
        }
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(heading)) {
            telemetry.addData("Turret", "Invalid pose; holding last position");
            return false;
        }
        double targetY = y > MID_FIELD ? FIELD_SIZE - Y_HIVE : Y_HIVE;
        double targetHeading = Math.atan2(targetY - y, X_HIVE - x);

        double relativeAngle = Math.atan2(Math.sin(targetHeading - heading),
                Math.cos(targetHeading - heading));
        double fullTurn = 2.0 * Math.PI;
        double minTurns = Math.ceil((minAngle - relativeAngle) / fullTurn);
        double maxTurns = Math.floor((maxAngle - relativeAngle) / fullTurn);
        boolean reachable = minTurns <= maxTurns;
        double commandedAngle;
        if (reachable) {
            // Select the equivalent angle closest to the previous command, so
            // crossing +/-180 degrees does not make a 540-degree turret reverse.
            double turns = Math.rint((lastCommandedAngle - relativeAngle) / fullTurn);
            turns = Math.max(minTurns, Math.min(maxTurns, turns));
            commandedAngle = relativeAngle + turns * fullTurn;
        } else {
            // Compare circular distances, including targets across the +/-PI seam.
            double distanceToMin = angularDistance(relativeAngle, minAngle);
            double distanceToMax = angularDistance(relativeAngle, maxAngle);
            commandedAngle = distanceToMin <= distanceToMax ? minAngle : maxAngle;
        }
        double fraction = (commandedAngle - minAngle) / (maxAngle - minAngle);
        servo1.setPosition(servo1AtMin + fraction * (servo1AtMax - servo1AtMin));
        servo2.setPosition(servo2AtMin + fraction * (servo2AtMax - servo2AtMin));
        lastCommandedAngle = commandedAngle;

        telemetry.addData("Turret target (deg)", Math.toDegrees(relativeAngle));
        telemetry.addData("Turret command (deg)", Math.toDegrees(commandedAngle));
        telemetry.addData("Turret target within travel", reachable);
        return reachable;
    }

    private static double angularDistance(double a, double b) {
        return Math.abs(Math.atan2(Math.sin(a - b), Math.cos(a - b)));
    }

    private static void validatePosition(double position) {
        if (!Double.isFinite(position) || position < 0.0 || position > 1.0) {
            throw new IllegalArgumentException("Turret servo positions must be finite and within [0, 1]");
        }
    }
}
