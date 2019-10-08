package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Jordan Paglione and Alec Chen on 10/1/19.
 */
//@Disabled
@TeleOp(name="General_TeleOp_2019-20", group="Iterative Opmode")
public class TeleOpGeneral19_20 extends OpMode {

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
    double startTime = runtime.milliseconds();

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

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
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        IntakeLeft.setDirection(DcMotor.Direction.REVERSE);
        IntakeRight.setDirection(DcMotor.Direction.FORWARD);
        Treadmill.setDirection(DcMotor.Direction.REVERSE);

        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        IntakeRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Treadmill.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }


    @Override
    public void init_loop() {
        //Servo1.setPosition(0.3);
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */

    public void loop() {
        double threshold = 0.2;

        if (gamepad1.right_stick_x < -threshold || gamepad1.right_stick_x > threshold) {
            //Strafing with right stick
            if(gamepad1.left_bumper){
                FrontLeft.setPower(-gamepad1.right_stick_x/2);
                FrontRight.setPower(gamepad1.right_stick_x/2);
                BackLeft.setPower(gamepad1.right_stick_x/2);
                BackRight.setPower(-gamepad1.right_stick_x/2);
            }
            else{
                FrontLeft.setPower(-gamepad1.right_stick_x);
                FrontRight.setPower(gamepad1.right_stick_x);
                BackLeft.setPower(gamepad1.right_stick_x);
                BackRight.setPower(-gamepad1.right_stick_x);
            }

        } else if (gamepad1.left_stick_y < -threshold || gamepad1.left_stick_y > threshold || gamepad1.left_stick_x < -threshold || gamepad1.left_stick_x > threshold) {
            //Forward/backward and turning with left stick

            double drive = gamepad1.left_stick_y;
            double turn = gamepad1.left_stick_x;
            //make sure left and right power are outside threshold
            double leftPower = Range.clip(drive - turn, -1.0, 1.0) * 0.8;
            double rightPower = Range.clip(drive + turn, -1.0, 1.0) * 0.8;

            if (leftPower > threshold || leftPower < -threshold || rightPower < -threshold || rightPower > threshold) {
                if (gamepad1.left_bumper) {
                    FrontLeft.setPower(leftPower / 2);
                    BackLeft.setPower(leftPower / 2);
                    FrontRight.setPower(rightPower / 2);
                    BackRight.setPower(rightPower / 2);
                } else {
                    FrontLeft.setPower(leftPower);
                    BackLeft.setPower(leftPower);
                    FrontRight.setPower(rightPower);
                    BackRight.setPower(rightPower);
                }

            } else {
                FrontRight.setPower(0);
                FrontLeft.setPower(0);
                BackLeft.setPower(0);
                BackRight.setPower(0);
                //   double leftPower;
                //   double rightPower;
            }

        } else {
            FrontRight.setPower(0);
            FrontLeft.setPower(0);
            BackLeft.setPower(0);
            BackRight.setPower(0);
            //   double leftPower;
            //   double rightPower;
        }

        //intake motors
        double intakePower = .9;
        if (gamepad1.right_trigger > .2) {
            IntakeRight.setPower(intakePower);
            IntakeLeft.setPower(intakePower);
            Treadmill.setPower(intakePower);
        } else{
            IntakeRight.setPower(0);
            IntakeLeft.setPower(0);
            Treadmill.setPower(0);
        }


        //Servo Stuff

        if (gamepad1.a) {
            if (FoundationServo.getPosition() < 0.6 && FoundationServo.getPosition() > 0.4) {
                FoundationServo.setPosition(0);
            } else {
                FoundationServo.setPosition(0.5);
            }
        }


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
        /*
         * Code to run ONCE after the driver hits STOP
         */
    }
}