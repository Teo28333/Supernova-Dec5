package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class AutoPath {
    private static final double FIELD_MIDLINE_Y = 72.0;
    private final PoseFactory poseFactory;

    public AutoPath() {
        this(false);
    }

    /** @param mirrored mirror every pose across the field's horizontal centerline */
    public AutoPath(boolean mirrored) {
        PoseFactory factory = PoseFactory.degrees();
        poseFactory = mirrored ? factory.mirrorY(FIELD_MIDLINE_Y) : factory;

        start = poseFactory.of(32.5, 9, 180);
        fleur1 = poseFactory.of(10, 47, 180);
        fleur1Control1 = poseFactory.of(35, 44, 0);
        fleur2ShotStart = poseFactory.of(10, 47, 180);
        fleur2Shot = poseFactory.of(47, 132, 90);
        fleur2ShotControl1 = poseFactory.of(35, 82, 0);
        intakePrepStart = poseFactory.of(47, 132, 90);
        intakePrep = poseFactory.of(32, 9, 180);
        intakePrepControl1 = poseFactory.of(82, 45, 0);
        intakeStart = poseFactory.of(32, 9, 180);
        intake = poseFactory.of(9, 9, 180);
        parkShootStart = poseFactory.of(9, 9, 180);
        parkShoot = poseFactory.of(9, 103, 180);
        parkShootControl1 = poseFactory.of(51, 81, 0);
    }

    public final Pose start;
    private final Pose fleur1;
    private final Pose fleur1Control1;
    private final Pose fleur2ShotStart;
    private final Pose fleur2Shot;
    private final Pose fleur2ShotControl1;
    private final Pose intakePrepStart;
    private final Pose intakePrep;
    private final Pose intakePrepControl1;
    private final Pose intakeStart;
    private final Pose intake;
    private final Pose parkShootStart;
    private final Pose parkShoot;
    private final Pose parkShootControl1;

    public Path fleur1() {
        return Paths.curve(start, fleur1Control1, fleur1).linear(start, fleur1);
    }

    public Path fleur2Shot() {
        return Paths.curve(fleur2ShotStart, fleur2ShotControl1, fleur2Shot).linear(fleur2ShotStart, fleur2Shot);
    }

    public Path intakePrep() {
        return Paths.curve(intakePrepStart, intakePrepControl1, intakePrep).linear(intakePrepStart, intakePrep);
    }

    public Path intake() {
        return Paths.line(intakeStart, intake).linear(intakeStart, intake);
    }

    public Path parkShoot() {
        return Paths.curve(parkShootStart, parkShootControl1, parkShoot).linear(parkShootStart, parkShoot);
    }
}
