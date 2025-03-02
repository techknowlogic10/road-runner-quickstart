package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//@Disabled
@TeleOp
@Config

public class DriverOperator extends OpMode {

    // Declaring the DC Motors
   private DcMotor frontLeft = null;
   private DcMotor frontRight = null;
   private DcMotor backLeft = null;
   private DcMotor backRight = null;
   private DcMotor elevator = null;
   private DcMotor slider = null;
   private DcMotor lift = null;
   private AutoElevator autoElevator = null;
    public static int elevatorSpecimenPickPos = 112;//116, 100;
    public static int elevatorSpecimenHookPos = 92;//202;

   private DcMotorEx encoderElevator = null;

   private Servo elbow = null;
   private Servo intake = null;

   private double drivePower = 1.5;

    public static double intake_close = 0; //0.85;
    public static double intake_specimen_open = 0.4;
    public static double intake_sample_open = 0.5;
    public static double intake_wide_open = 1;

    public static double elbow_down = 0.25;
    public static double elbow_up = 0.7;
    public static double elbow_middle = 0.53;
    public static double elbow_lower = 0;
    public static double elbow_top = 1;


    @Override
    public void init(){


        // Initializing the DC Motors
        frontLeft = hardwareMap.dcMotor.get("frontleft"); // Port #0
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        frontRight = hardwareMap.dcMotor.get("frontright"); // Port #1
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeft = hardwareMap.dcMotor.get("backleft"); //Port#2
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        backRight = hardwareMap.dcMotor.get("backright"); //Port #3
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        elevator = hardwareMap.dcMotor.get("elevator"); //Port #?
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //elevator.setDirection(DcMotorSimple.Direction.REVERSE);

        slider = hardwareMap.dcMotor.get("slider");
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //slider.setDirection(DcMotorSimple.Direction.REVERSE);

        lift = hardwareMap.dcMotor.get("lift");
       // lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        elbow = hardwareMap.get(Servo.class, "elbow");
        elbow.scaleRange(0,0.1);
        elbow.setPosition(elbow_top);

        intake = hardwareMap.get(Servo.class, "intake");
        intake.setDirection(Servo.Direction.REVERSE);
        intake.scaleRange(0,0.06);
        intake.setPosition(intake_close);

        encoderElevator = hardwareMap.get(DcMotorEx.class, "elevator");
        encoderElevator.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        autoElevator = new AutoElevator(elevator, encoderElevator,telemetry);

    }
    @Override
    public void loop(){

        // Taking the values from the Gamepad/Joysticks
        double y = -gamepad1.left_stick_y;// * 4.1;
        double x = gamepad1.left_stick_x * 1.1; // * 5.1;
        double rx = gamepad1.right_stick_x;

        telemetry.addLine("y: "+ y +" , x: "+ x + " , rx: +"+ rx);

        // Normalizing the Power
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx),drivePower);
        double frontLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        telemetry.addLine("denominator: "+ denominator +" , frontLeftPower: "+ frontLeftPower + " , backLeftPower: +"+ backLeftPower +", frontRightPower: "+frontRightPower +" , backRightPower: "+backRightPower);

        //Setting the Power for the DC Motors
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        telemetry.addLine("elevator encoder: "+ elevator.getCurrentPosition());
        telemetry.addLine("encoderElevator encoder: "+ encoderElevator.getCurrentPosition());

        if(gamepad2.dpad_up) {
            telemetry.addLine("gamepad2.dpad_up 0.32");
            elbow.setPosition(elbow_up); // elbow up/ elbow_end
        } else if(gamepad2.dpad_down){
            telemetry.addLine("gamepad2.dpad_down 0");
            elbow.setPosition(elbow_down); //elbow down/ elbow_start //0.475, 0.29
        } else if(gamepad2.dpad_left){
            telemetry.addLine("gamepad2.dpad_left 0.3");
            elbow.setPosition(elbow_middle); //elbow 90-parallal/ elbow_middle
        } else if(gamepad2.x){
            telemetry.addLine("gamepad2.x top 0:");
            elbow.setPosition(elbow_top); //elbow top
        }

        if(gamepad2.left_bumper){
            telemetry.addLine("gamepad2.right_bumper intake_close:"+intake_close);
            intake.setPosition(intake_close); //intake close
        } else if(gamepad2.right_bumper){
            telemetry.addLine("gamepad2.left_bumper intake_specimen_open:" +intake_specimen_open);
            intake.setPosition(intake_specimen_open); //intake specimen open
        } else  if(gamepad2.y){
            telemetry.addLine("gamepad2.right_bumper intake_sample_open:"+intake_sample_open);
            intake.setPosition(intake_sample_open); //intake sample open
        }
        else if(gamepad2.dpad_right){
            telemetry.addLine("gamepad2.dpad_right intake_wide_open:"+intake_wide_open);
            intake.setPosition(intake_wide_open); //intake wide open
        }

        double sliderPower = gamepad2.left_stick_y * 1.5;
        telemetry.addLine("sliderPower: "+ sliderPower);
        slider.setPower((sliderPower));

        double elevatorPower = gamepad2.right_stick_y;
        telemetry.addLine("elevatorPower: "+ elevatorPower);
        elevator.setPower(elevatorPower);

        //double liftPower = gamepad1.right_stick_y * 5;

        if (gamepad1.left_trigger > 0 ) {
            double liftPower = gamepad1.left_trigger * 5;
            //double liftPower = gamepad1.left_trigger * 2;
            telemetry.addLine("liftPower: " + liftPower);
            lift.setPower(liftPower);
        }
        if (gamepad1.right_trigger > 0 ) {
            double liftPower = gamepad1.right_trigger * -5;
            //double liftPower = gamepad1.left_trigger * 2;
            telemetry.addLine("liftPower: " + liftPower);
            lift.setPower(liftPower);
        }

        if (gamepad2.a){
            telemetry.addLine("gamepad2.a: ");

            intake.setPosition(intake_specimen_open);
            elbow.setPosition(elbow_middle);
            autoElevator.elevatorUp(elevatorSpecimenPickPos);
            //new Thread(2000);
            elevator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            elevator.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        if (gamepad2.b){

            telemetry.addLine("gamepad2.b: ");
            intake.setPosition(intake_close);
            autoElevator.elevatorUp(elevatorSpecimenHookPos);
            //new Thread(2000);
            elevator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            elevator.setDirection(DcMotorSimple.Direction.FORWARD);

            elbow.setPosition(elbow_top); //elbow 90-parallal/ elbow_middle

        }

        /*if (gamepad1.a){
            telemetry.addLine("gamepad1.a: smart control for lift");
            Lift liftSmart = new Lift(lift);
            liftSmart.liftUp();
        }*/


        elevator.setDirection(DcMotorSimple.Direction.FORWARD);

/*

        if (gamepad2.x){
            telemetry.addLine("gamepad2.x: ");

        }*/

        if (gamepad1.left_trigger > 0 && gamepad1.right_trigger > 0){
            telemetry.addLine("gamepad1.left_trigger  && gamepad2.right_trigger >0");
            while(lift.isBusy()){
                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }

        telemetry.update();


    }
}
