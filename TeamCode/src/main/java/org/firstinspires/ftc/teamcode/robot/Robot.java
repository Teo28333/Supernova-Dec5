package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystem.Intake_SS;
public class Robot {
    private final Telemetry telemetry;
    private final Follower follower;
     private final Intake_SS intake;
    private boolean started;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        follower = Constants.create(hardwareMap);


        intake = new Intake_SS(
                hardwareMap.get(DcMotorEx.class, "intake1"),
                hardwareMap.get(DcMotorEx.class, "intake2"),
                hardwareMap.get(Servo.class, "gate"),
                telemetry
        );
    }



    public void start() {
        follower.stop();
        intake.stop();
        intake.update();
    }



    public void start(Pose startingPose) {
        follower.setPose(startingPose);
        start();
    }

    public void update(Gamepad gamepad) {
        if (!started) {
            return;
        }

        follower.manual(ManualDrive.fieldCentric(
                -gamepad.left_stick_y,
                -gamepad.left_stick_x,
                -gamepad.right_stick_x,
                follower.pose().heading()
        ));


        if (gamepad.right_bumper) {
            intake.intakeCMD();
        } else if (gamepad.circle) {
            intake.outtakeCMD();
        } else {
            intake.stop();
        }
        follower.update();
        intake.update();

        Pose pose = follower.pose();
        telemetry.addData("Robot X", pose.x());
        telemetry.addData("Robot Y", pose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(pose.heading()));
        telemetry.update();
    }


    public void stop() {
        intake.stop();
        intake.write();
        follower.stop();
    }

    public Follower getFollower() {
        return follower;
    }

    public Pose getPose() {
        return follower.pose();
    }
}

