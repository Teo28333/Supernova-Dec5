package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake_SS {

    //naming
    private final DcMotorEx intakeMotor1;
    private final DcMotorEx intakeMotor2;
    private final Servo gate;
    private final Telemetry telemetry;

    //Intake Speed
   private double fullIntakeSpeed = 1.0;
   private double partialIntakeSpeed = 0.5;
   private double reverseIntakeSpeed = -0.7;


   //Gate Pose
    private double closeGatePos = 1.0;
    private double openGatePose = 0.5;

    private double gatePos = closeGatePos;

    //constructor
    public Intake_SS(DcMotorEx intakeMotor1, DcMotorEx intakeMotor2, Servo gate, Telemetry telemetry) {
        this.intakeMotor1 = intakeMotor1;
        this.intakeMotor2 = intakeMotor2;
        this.gate = gate;
        this.telemetry = telemetry;
    }

    public void Intake() {
    intakeMotor1.setPower(fullIntakeSpeed);
    intakeMotor2.setPower(partialIntakeSpeed);
    gate.setPosition(closeGatePos);
    }

    public void Transfer() {
        intakeMotor1.setPower(fullIntakeSpeed);
        intakeMotor2.setPower(fullIntakeSpeed);
        gate.setPosition(openGatePose);
    }

    public void Outtake() {
        intakeMotor1.setPower(reverseIntakeSpeed);
        intakeMotor2.setPower(reverseIntakeSpeed);
        gate.setPosition(closeGatePos);
    }

}
