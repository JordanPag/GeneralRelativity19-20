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

@Autonomous(name = "Skystone (Red, Pass through)", group = "Sensor")
public class SkystoneRedPassThrough extends LinearOpMode{

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor BackLeft;
    private DcMotor BackRight;
    private DcMotor IntakeLeft;
    private DcMotor IntakeRight;
    private DcMotor PassThroughLeft;
    private DcMotor PassThroughRight;
    private Servo FoundationServo1;
    private Servo FoundationServo2;
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

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
        PassThroughLeft = hardwareMap.get(DcMotor.class, "PassThroughLeft");
        PassThroughRight = hardwareMap.get(DcMotor.class, "PassThroughRight");
        FoundationServo1 = hardwareMap.get(Servo.class, "FoundationServo1");
        FoundationServo2 = hardwareMap.get(Servo.class, "FoundationServo2");

        sensorColor = hardwareMap.get(ColorSensor.class, "ColorSensor");
        sensorDistance = hardwareMap.get(DistanceSensor.class, "ColorSensor");
        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        IntakeLeft.setDirection(DcMotor.Direction.REVERSE);
        IntakeRight.setDirection(DcMotor.Direction.FORWARD);
        PassThroughLeft.setDirection(DcMotor.Direction.FORWARD);
        PassThroughRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            // Start button is pressed

            //Go to the block
            strafeLeftWithEncoders(0.5, 1650);
            moveBackwardWithEncoders(0.5,200);
            delay(300);

            //Test if stone 3 is a skystone
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);
            boolean isSkystone = false;
            int position = 0;
            if(hsvValues[0] >= 60.0) {
                isSkystone = true;
                position = 3;
            }
            telemetry.addData("Skystone", isSkystone);
            telemetry.update();

            if(!isSkystone) {
                //If stone 3 isn't a skystone, see if stone 2 is
                moveBackwardWithEncoders(0.5,200);
                delay(300);

                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                        (int) (sensorColor.green() * SCALE_FACTOR),
                        (int) (sensorColor.blue() * SCALE_FACTOR),
                        hsvValues);

                telemetry.addData("Distance (cm)",
                        String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
                telemetry.addData("Alpha", sensorColor.alpha());
                telemetry.addData("Red  ", sensorColor.red());
                telemetry.addData("Green", sensorColor.green());
                telemetry.addData("Blue ", sensorColor.blue());
                telemetry.addData("Hue", hsvValues[0]);
                isSkystone = false;
                if(hsvValues[0] >= 60.0) {
                    isSkystone = true;
                    position = 2;
                } else {
                    //If stone 3 and 2 aren't skystones, then stone 1 has to be a skystone
                    position = 1;
                }
            }

            telemetry.addData("Skystone position", position);
            telemetry.update();
            delay(1500);


            //Get the stone by moving forwards/backwards a different amount based on the position of the skystone
            if (position == 3) {
                moveForwardWithEncoders(0.5, 300);
            } if (position == 2) {
                moveForwardWithEncoders(0.5, 400);
            } else {
                moveForwardWithEncoders(0.5,50);
            }
            //strafeLeftWithEncoders(0.5, 725);
            turnLeftWithEncoders(0.5,350);
            IntakeLeft.setPower(0.9);
            IntakeRight.setPower(0.9);
            PassThroughLeft.setPower(0.2);
            PassThroughRight.setPower(0.2);
            if(position == 1) {
                moveForwardWithEncoders(0.5, 400);
            } else {
                moveForwardWithEncoders(0.5, 700);
            }
            delay(500);
            IntakeLeft.setPower(0);
            IntakeRight.setPower(0);
            PassThroughLeft.setPower(0);
            PassThroughRight.setPower(0);

            //Bring the block to the foundation (not moved yet)
            FoundationServo1.setPosition(0.5);
            FoundationServo2.setPosition(0.5);
            turnRightWithEncoders(0.5,340);
            if(position == 1) {
                strafeRightWithEncoders(0.5, 850);
            } else {
                strafeRightWithEncoders(0.5,700);
            }
            if (position == 1) {
                moveForwardWithEncoders(0.8, 2800);
            } else if (position == 2) {
                moveForwardWithEncoders(0.8,2500);
            } else {
                moveForwardWithEncoders(0.8,2000);
            }
            turnLeftWithEncoders(0.5,1);
            moveForwardWithEncoders(0.8, 2000);
            turnRightWithEncoders(0.5, 1200);
            moveBackwardWithEncoders(0.5, 500);

            //Put the block onto the foundation
            PassThroughLeft.setPower(0.6);
            PassThroughRight.setPower(0.6);
            IntakeLeft.setPower(0.9);
            IntakeRight.setPower(0.9);
            delay(800);
            IntakeLeft.setPower(0);
            IntakeRight.setPower(0);
            PassThroughLeft.setPower(0);
            PassThroughRight.setPower(0);

            //Pull the foundation into the building site
            turnRightWithEncoders(0.5,1050);
            strafeRightWithEncoders(0.5, 250);
            FoundationServo1.setPosition(0);
            FoundationServo2.setPosition(0);
            delay(500);
            strafeLeftWithEncoders(0.8,3300);
            FoundationServo1.setPosition(0.5);
            FoundationServo2.setPosition(0.5);

            /*//Go get another block
            moveForwardWithEncoders(0.8, 800);
            strafeRightWithEncoders(0.8,100);
            turnRightWithEncoders(0.8,20);
            moveForwardWithEncoders(0.8, 1200);
            strafeRightWithEncoders(0.8,2000);
            if (position == 2) {
                moveForwardWithEncoders(0.8, 2500);
            } else if (position == 1) {
                moveForwardWithEncoders(0.8,2900);
            } else {
                moveForwardWithEncoders(0.8, 500);
            }
            if (position != 3) {
                strafeRightWithEncoders(0.5, 1000);
            } else {
                turnRightWithEncoders(0.5, 700);
                moveForwardWithEncoders(0.5, 400);
            }

            //Grab the block
            IntakeLeft.setPower(0.9);
            IntakeRight.setPower(0.9);
            Treadmill.setPower(0.9);
            moveForwardWithEncoders(0.8,300);
            delay(1000);
            IntakeLeft.setPower(0);
            IntakeRight.setPower(0);
            Treadmill.setPower(0);*/

            //Navigate under the skybridge
            moveForwardWithEncoders(0.5,1800);
            strafeRightWithEncoders(0.5,1300);
            moveForwardWithEncoders(0.5,500);

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
