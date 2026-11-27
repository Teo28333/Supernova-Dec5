package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret_SS {

    private final Servo servo1, servo2;
    private final Telemetry telemetry;


    public Turret_SS(HardwareMap hwm, Telemetry telemetry, String servo1Name, String servo2Name) {
        this.telemetry = telemetry;
        servo1 = hwm.get(Servo.class, servo1Name);
        servo2 = hwm.get(Servo.class, servo2Name);
    }
}
