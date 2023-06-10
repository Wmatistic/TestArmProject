package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.*;

@TeleOp
public class TestArm extends OpMode {

    private Servo firstArm, secondArm;

    // FIXME: alter the length of the arms to fit the real world length
    private double firstArmLength = 10;
    private double secondArmLength = 10;

    private double a1, a2;
    private double q1, q2;

    private double x,y;

    private GamepadEx driver;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        firstArm = hardwareMap.servo.get("First Arm");
        secondArm = hardwareMap.servo.get("Second Arm");

        firstArm.setDirection(Servo.Direction.REVERSE);
        secondArm.setDirection(Servo.Direction.REVERSE);

        driver = new GamepadEx(gamepad1);

        a1 = firstArmLength;
        a2 = secondArmLength;

        x = 0.0;
        y = 20.0;
    }

    @Override
    public void loop() {

        driver.readButtons();

        // Alter the x and y input to the arm
        if (driver.wasJustPressed(Button.Y)){
            y += 1.0;
        } else if (driver.wasJustPressed(Button.A)){
            y -= 1.0;
        } else if (driver.wasJustPressed(Button.B)){
            x += 1.0;
        } else if (driver.wasJustPressed(Button.X)){
            x -= 1.0;
        }

        // Calculate angle of the arms using x and y input
        calculateArmAngles(x, y);

        // Only include "/ 4.7" if using servo
        firstArm.setPosition((q1 / (2 * Math.PI)) / 4.7);
        secondArm.setPosition(q2 / (2 * Math.PI));

        telemetry.addData("X : ", x);
        telemetry.addData("Y : ", y);
        telemetry.addData("Angle 1 : ", ((q1 / Math.PI) / 2));
        telemetry.addData("Angle 2 : ", ((q2 / Math.PI) / 2));
        telemetry.update();
    }

    public void calculateArmAngles(double x, double y){
        //We derive the First Arm angle from the Second Arm so calculate the Second Arm first
        calculateSecondArmAngle(x, y);
        calculateFirstArmAngle(x, y);
    }

    public void calculateSecondArmAngle(double x, double y){
        q2 = Math.acos(  (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(a1, 2) - Math.pow(a2, 2))   /   (2 * a1 * a2)  );
    }

    public void calculateFirstArmAngle(double x, double y){
        q1 = Math.atan( (y / x) ) - Math.atan( ( a2 * Math.sin( (q2 / Math.PI) * 360 ) / (a1 + ( a2 * Math.cos( (q2 / Math.PI) * 360 ) )) ));
    }
}
