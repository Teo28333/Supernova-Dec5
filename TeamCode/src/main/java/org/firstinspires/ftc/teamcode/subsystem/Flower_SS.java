package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Flower_SS {
    private Servo flowerServoR;
    private Servo flowerServoL;
    private double upServoRPos = 0.4;
    private double upServoLPos = 0.6;
    private double downServoRPos = 0.5;
    private double downServoLPos = 0.5;
    private Telemetry telemetry;

    public void downFlower() {
        flowerServoR.setPosition(downServoRPos);
        flowerServoL.setPosition(downServoLPos);
    }

    public void upFlower() {
        flowerServoR.setPosition(upServoRPos);
        flowerServoL.setPosition(upServoLPos);
    }

    public Flower_SS(Servo flowerServoR, Servo flowerServoL){
        this.flowerServoR = flowerServoR;
        this.flowerServoL = flowerServoL;
    }
}
