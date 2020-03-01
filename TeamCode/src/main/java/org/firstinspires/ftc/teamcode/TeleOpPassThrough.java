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
 * Created by Jordan Paglione on 2/10/20.
 */
//@Disabled
@TeleOp(name="Pass Through TeleOp", group="Iterative Opmode")
public class TeleOpPassThrough extends OpMode {

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
        PassThroughLeft = hardwareMap.get(DcMotor.class, "PassThroughLeft");
        PassThroughRight = hardwareMap.get(DcMotor.class, "PassThroughRight");
        FoundationServo1 = hardwareMap.get(Servo.class, "FoundationServo1");
        FoundationServo2 = hardwareMap.get(Servo.class, "FoundationServo2");



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

        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        IntakeRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        PassThroughLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PassThroughRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        if (gamepad1.right_stick_x < -threshold || gamepad1.right_stick_x > threshold || gamepad1.left_stick_y < -threshold || gamepad1.left_stick_y > threshold || gamepad1.left_stick_x < -threshold || gamepad1.left_stick_x > threshold) {
            //Forward/backward and strafing with the left stick, turning with the right

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;
            //make sure left and right power are outside threshold
            double frontLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0) * 0.8;
            double frontRightPower = Range.clip(drive - turn - strafe, -1.0, 1.0) * 0.8;
            double backLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0) * 0.8;
            double backRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0) * 0.8;

            if (Math.abs(frontLeftPower) > threshold || Math.abs(backLeftPower) > threshold || Math.abs(frontRightPower) > threshold || Math.abs(backRightPower) > threshold) {
                FrontLeft.setPower(frontLeftPower);
                BackLeft.setPower(backLeftPower);
                FrontRight.setPower(frontRightPower);
                BackRight.setPower(backRightPower);
            } else {
                FrontRight.setPower(0);
                FrontLeft.setPower(0);
                BackLeft.setPower(0);
                BackRight.setPower(0);
            }

        } else {
            FrontRight.setPower(0);
            FrontLeft.setPower(0);
            BackLeft.setPower(0);
            BackRight.setPower(0);
        }

        //intake motors (2nd controller)
        double intakePower = .9;
        double passThroughPower = 1.0;
        int right = 0;
        int left = 0;

        //Move right intake motor with the right bumper and trigger, and left with left bumper and trigger
        if (gamepad1.right_trigger > .2) {
            IntakeRight.setPower(intakePower);
            right = 1;
        } else if (gamepad1.right_bumper) {
            IntakeRight.setPower(-intakePower);
            right = -1;
        }

        if (gamepad1.left_trigger > .2) {
            IntakeLeft.setPower(intakePower);
            left = 1;
        } else if (gamepad1.left_bumper) {
            IntakeLeft.setPower(-intakePower);
            left = -1;
        }

        //If left and right are powered in the same direction then the pass through goes that direction
        if (right == 1 && left == 1) {
            PassThroughLeft.setPower(passThroughPower);
            PassThroughRight.setPower(passThroughPower);
        } else if (right == -1 && left == -1) {
            PassThroughLeft.setPower(-passThroughPower);
            PassThroughRight.setPower(-passThroughPower);
        } else {
            PassThroughLeft.setPower(0);
            PassThroughRight.setPower(0);
        }

        if (right == 0) {
            IntakeRight.setPower(0);
        }
        if (left == 0) {
            IntakeLeft.setPower(0);
        }

        //The pass through can also be controlled separately with the left stick on controller 2
        if(Math.abs(gamepad2.left_stick_y) > 0.2) {
            if(gamepad2.left_stick_y > 0) {
                PassThroughLeft.setPower(passThroughPower);
                PassThroughRight.setPower(passThroughPower);
            } else {
                PassThroughLeft.setPower(-passThroughPower);
                PassThroughRight.setPower(-passThroughPower);
            }
        } else if(right != 1 && left != 1) {
            PassThroughLeft.setPower(0);
            PassThroughRight.setPower(0);
        }


        //Servo Stuff

        if (gamepad1.a) {
            //Servos down
            FoundationServo1.setPosition(0);
            FoundationServo2.setPosition(0);
        } else if (gamepad1.b) {
            //Servos up
            FoundationServo1.setPosition(0.5);
            FoundationServo2.setPosition(0.5);
        }

        //Capstone servo
        /*
        if (gamepad1.x) {
            //Servo down
            telemetry.addData("Servo action", "Down");
            CapstoneServo.setPosition(1);
        } else if (gamepad1.y) {
            //Servo up
            telemetry.addData("Servo action", "Up");
            CapstoneServo.setPosition(0);
        }
        */


        // Show the elapsed game time
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }
}