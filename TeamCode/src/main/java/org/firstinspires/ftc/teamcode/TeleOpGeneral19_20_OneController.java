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
@TeleOp(name="One controller", group="Iterative Opmode")
public class TeleOpGeneral19_20_OneController extends OpMode {

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
    private Servo Claw;
    private Servo ClawTurn;
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
        //Claw = hardwareMap.get(Servo.class, "Claw");
        //ClawTurn = hardwareMap.get(Servo.class, "ClawTurn");



        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        IntakeLeft.setDirection(DcMotor.Direction.REVERSE);
        IntakeRight.setDirection(DcMotor.Direction.FORWARD);
        Treadmill.setDirection(DcMotor.Direction.FORWARD);

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
        double treadmillPower = 1.0;
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

        //If left and right are powered in the same direction then the treadmill goes that direction
        if (right == 1 && left == 1) {
            Treadmill.setPower(treadmillPower);
        } else if (right == -1 && left == -1) {
            Treadmill.setPower(-treadmillPower);
        } else {
            Treadmill.setPower(0);
        }

        if (right == 0) {
            IntakeRight.setPower(0);
        }
        if (left == 0) {
            IntakeLeft.setPower(0);
        }

        //The treadmill can also be controlled separately with the left stick on controller 2
        if(Math.abs(gamepad2.left_stick_y) > 0.2) {
            if(gamepad2.left_stick_y > 0) {
                Treadmill.setPower(treadmillPower);
            } else {
                Treadmill.setPower(-treadmillPower);
            }
        } else if(right != 1 && left != 1) {
            Treadmill.setPower(0);
        }


        //Servo Stuff

        if (gamepad1.a) {
            //Servo down
            FoundationServo.setPosition(0);
        }
        if (gamepad1.b) {
            //Servo up
            FoundationServo.setPosition(0.5);
        }


        // Show the elapsed game time
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }
}