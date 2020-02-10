package org.firstinspires.ftc.teamcode.Autonomous;

/**
 * Created by Jordan Paglione on 10/7/19
 */

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

@Autonomous(name = "Move at end", group = "Sensor")
public class MoveAtEnd extends LinearOpMode{

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor BackLeft;
    private DcMotor BackRight;
    private DcMotor IntakeLeft;
    private DcMotor IntakeRight;
    private DcMotor Treadmill;
    private Servo FoundationServo;

    @Override
    public void runOpMode() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BackRight = hardwareMap.get(DcMotor.class, "BackRight");
        IntakeLeft = hardwareMap.get(DcMotor.class, "IntakeLeft");
        IntakeRight = hardwareMap.get(DcMotor.class, "IntakeRight");
        Treadmill = hardwareMap.get(DcMotor.class, "Treadmill");
        FoundationServo = hardwareMap.get(Servo.class, "FoundationServo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        IntakeLeft.setDirection(DcMotor.Direction.REVERSE);
        IntakeRight.setDirection(DcMotor.Direction.FORWARD);
        Treadmill.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        while (opModeIsActive()) {
            // Start button is pressed

            delay(25000);
            moveForwardWithEncoders(0.5, 200);

            // End of auto
            break;
        }
    }

    public void delay(int time) {
        double startTime = runtime.milliseconds();
        while (runtime.milliseconds() - startTime < time) {
        }
    }

    public void strafeRightWithEncoders(double power, int count){
        int start = FrontLeft.getCurrentPosition();
        FrontLeft.setPower(power);
        FrontRight.setPower(-power);
        BackLeft.setPower(-power);
        BackRight.setPower(power);
        while(FrontLeft.getCurrentPosition() < start + count) {
            telemetry.addData("Left motor position", FrontLeft.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

    public void strafeLeftWithEncoders(double power, int count){
        int start = FrontRight.getCurrentPosition();
        FrontLeft.setPower(-power);
        FrontRight.setPower(power);
        BackLeft.setPower(power);
        BackRight.setPower(-power);
        while(FrontRight.getCurrentPosition() < start + count) {
            telemetry.addData("Right motor position", FrontRight.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

    public void moveBackwardWithEncoders(double power, int count){
        int start = FrontLeft.getCurrentPosition();
        FrontLeft.setPower(-power);
        FrontRight.setPower(-power);
        BackLeft.setPower(-power);
        BackRight.setPower(-power);
        while(FrontLeft.getCurrentPosition() > start - count) {
            telemetry.addData("Left motor position", FrontLeft.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

    public void moveForwardWithEncoders(double power, int count){
        int start = FrontLeft.getCurrentPosition();
        FrontLeft.setPower(power);
        FrontRight.setPower(power);
        BackLeft.setPower(power);
        BackRight.setPower(power);
        while(FrontLeft.getCurrentPosition() < start + count) {
            telemetry.addData("Left motor position", FrontLeft.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

    public void turnLeftWithEncoders(double power, int count){
        int start = FrontRight.getCurrentPosition();
        FrontLeft.setPower(-power);
        FrontRight.setPower(power);
        BackLeft.setPower(-power);
        BackRight.setPower(power);
        while(FrontRight.getCurrentPosition() < start + count) {
            telemetry.addData("Right motor position", FrontRight.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

    public void turnRightWithEncoders(double power, int count){
        int start = FrontLeft.getCurrentPosition();
        FrontLeft.setPower(power);
        FrontRight.setPower(-power);
        BackLeft.setPower(power);
        BackRight.setPower(-power);
        while(FrontLeft.getCurrentPosition() < start + count) {
            telemetry.addData("Left motor position", FrontLeft.getCurrentPosition());
            telemetry.update();
            idle();
        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
}
