package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter_SS {
    private final DcMotorEx motor1;
    private final Servo servo;
    private final Telemetry telemetry;

    private double currentSpeed = 0.0;

    public Shooter_SS(HardwareMap hwm, Telemetry telemetry, String motor1Name, String servoName) {

        this.telemetry = telemetry;
        motor1 = hwm.get(DcMotorEx.class, motor1Name);
        servo = hwm.get(Servo.class, servoName);
    }

    public void read() {
        //TODO add sensor read
        currentSpeed = (motor1.getVelocity() / 28) * 60;
    }

    public void write() {
        //TODO add motor writing
    }

    public void update() {
        //TODO add teleop logic
    }

    public double getTargetSpeed() {
        return;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }
}
