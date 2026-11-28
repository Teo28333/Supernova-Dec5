package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.math.PIDFController;
import org.firstinspires.ftc.teamcode.math.ShooterEquations;

public class Shooter_SS {
    private final DcMotorEx motor1;
    private final Servo servo;
    private final Telemetry telemetry;
    private final ShooterEquations shooterEquations;
    private final PIDFController pidfController;

    private double currentSpeed = 0.0;
    private double targetSpeed = 0.0;
    private double distance = 0.0;
    private double motorPow = 0.0;
    private double servoPos = 0.0;
    private double TOL = 50.0;


    public Shooter_SS(HardwareMap hwm, Telemetry telemetry, String motor1Name, String servoName) {

        this.telemetry = telemetry;
        motor1 = hwm.get(DcMotorEx.class, motor1Name);
        servo = hwm.get(Servo.class, servoName);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterEquations = new ShooterEquations();
        pidfController = new PIDFController();
    }

    public void read() {
        //TODO add sensor read
        currentSpeed = motor1.getVelocity() * 60 / 28;
    }

    public void write() {
        //TODO add motor writing
        motor1.setPower(motorPow);
        servo.setPosition(servoPos);
    }

    public void update(double x, double y, double heading) {
        //TODO add teleop logic
        read();
        write();
        targetSpeed = shooterEquations.targetSpeedLut(distance);
        motorPow = pidfController.calculate(targetSpeed, currentSpeed);
    }

    public boolean isAtTarget() {
        return (targetSpeed - TOL) < currentSpeed && (targetSpeed + TOL) > currentSpeed;
    }
}
