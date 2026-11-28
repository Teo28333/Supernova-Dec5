package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.Intake_SS;
import org.firstinspires.ftc.teamcode.subsystem.Shooter_SS;

@Autonomous(name = "AutoDaniel", group = "Examples")
public class AutoDaniel extends OpMode {
    private static final int START = 0;
    private static final int PICKUP_FIRST = 1;
    private static final int MOVE_TO_SHOT = 2;
    private static final int PREPARE_INTAKE = 3;
    private static final int PICKUP_SECOND = 4;
    private static final int PARK_AND_SHOOT = 5;
    private static final int DONE = 6;

    private Follower follower;
    private Intake_SS intake;
    private Shooter_SS shooter;
    private AutoPath paths;
    private int pathState;
    private boolean stateStarted;

    @Override
    public void init() {
        paths = new AutoPath();
        follower = Constants.create(hardwareMap);
        intake = new Intake_SS(
                hardwareMap.get(DcMotorEx.class, "intake1"),
                hardwareMap.get(DcMotorEx.class, "intake2"),
                hardwareMap.get(Servo.class, "gate"), telemetry);
        shooter = new Shooter_SS(hardwareMap, telemetry, "sMotor", "sServo");
        follower.setPose(paths.start);
        follower.update();
        setPathState(START);
    }

    @Override
    public void start() {
        setPathState(PICKUP_FIRST);
    }

    private void setPathState(int newState) {
        pathState = newState;
        stateStarted = false;
    }

    private void follow(Path path) {
        follower.follow(path);
        stateStarted = true;
    }


    private void autonomousPathUpdate() {
        switch (pathState) {
            case PICKUP_FIRST:
                if (!stateStarted) {
                    intake.intakeCMD();
                    follow(paths.fleur1());
                } else if (!follower.isBusy()) {
                    intake.stop();
                    setPathState(MOVE_TO_SHOT);
                }
                break;

            case MOVE_TO_SHOT:
                if (!stateStarted) {
                    intake.transferCMD();
                    follow(paths.fleur2Shot());
                } else if (!follower.isBusy()) {
                    intake.stop();
                    setPathState(PREPARE_INTAKE);
                }
                break;

            case PREPARE_INTAKE:
                if (!stateStarted) {
                    follow(paths.intakePrep());
                } else if (!follower.isBusy()) {
                    setPathState(PICKUP_SECOND);
                }
                break;

            case PICKUP_SECOND:
                if (!stateStarted) {
                    intake.intakeCMD();
                    follow(paths.intake());
                } else if (!follower.isBusy()) {
                    intake.stop();
                    setPathState(PARK_AND_SHOOT);
                }
                break;

            case PARK_AND_SHOOT:
                if (!stateStarted) {
                    intake.transferCMD();
                    follow(paths.parkShoot());
                } else if (!follower.isBusy()) {
                    intake.stop();
                    setPathState(DONE);
                }
                break;

            case DONE:
                intake.stop();
                follower.stop();
                break;

            default:
                // START is intentionally idle until the driver presses Start.
                break;
        }
    }

    @Override
    public void loop() {
        follower.update();
        Pose pose = follower.pose();
        shooter.update(pose.x(), pose.y(), pose.heading());
        intake.update();
        autonomousPathUpdate();

        telemetry.addData("Auto state", pathState);
        telemetry.addData("Follower busy", follower.isBusy());
        telemetry.addData("X", pose.x());
        telemetry.addData("Y", pose.y());
        telemetry.addData("Heading (deg)", Math.toDegrees(pose.heading()));
        telemetry.update();
    }

    @Override
    public void stop() {
        intake.stop();
        intake.update();
        follower.stop();
    }
}
