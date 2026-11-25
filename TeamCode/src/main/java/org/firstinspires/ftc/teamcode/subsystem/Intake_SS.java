package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake_SS {

    private boolean isIntaking = false;
    private boolean isTransferring = false;
    private boolean isOuttaking = false;

    //naming
    private final DcMotorEx intake1;
    private final DcMotorEx intake2;
    private final Servo gate;
    private final Telemetry telemetry;

    //Intake Speed
    private double firstMotorPow;
    private double secondMotorPow;


   //Gate Pose
    private double closeGatePos = 1.0;
    private double openGatePose = 0.5;

    private double gatePos = closeGatePos;

    //constructor
    public Intake_SS(DcMotorEx intake1, DcMotorEx intake2, Servo gate, Telemetry telemetry) {
        this.intake1 = intake1;
        this.intake2 = intake2;
        intake1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake2.setDirection(DcMotorSimple.Direction.FORWARD);
        this.gate = gate;
        this.telemetry = telemetry;
    }



    public void read() {
        //TODO add sensor
    }

    public void write() {
        intake1.setPower(firstMotorPow);
        intake2.setPower(secondMotorPow);
        gate.setPosition(gatePos);
    }

    public void telemetry() {
        telemetry.addData("Gate position", gatePos);
        telemetry.addData("is Intaking",isIntaking);
        telemetry.addData("is Transfering",isTransferring);
    }

    public void update() {
        read();
        write();
        telemetry();
    }

    public void intakeCMD() {
        isIntaking = true;
        isTransferring = false;
        isOuttaking = false;
        gate.setPosition(closeGatePos);
        firstMotorPow = 1.0;
        secondMotorPow = 0.5;
    }

    public void outtakeCMD() {
        isIntaking = false;
        isTransferring = false;
        isOuttaking = true;
        gate.setPosition(closeGatePos);
        firstMotorPow = -1.0;
        secondMotorPow = -1.0;
    }

    public void transferCMD() {
        isIntaking = true;
        isTransferring = true;
        isOuttaking = false;
        gate.setPosition(openGatePose);
        firstMotorPow = 1.0;
        secondMotorPow = 1.0;
    }

    public void stop() {
        isIntaking = false;
        isTransferring = false;
        isOuttaking = false;
        gate.setPosition(closeGatePos);
        firstMotorPow = 0.0;
        secondMotorPow = 0.0;
    }


}

