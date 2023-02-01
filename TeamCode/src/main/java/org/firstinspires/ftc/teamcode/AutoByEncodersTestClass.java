package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class AutoByEncodersTestClass extends LinearOpMode {

    @Override
    public void runOpMode(){
        // Declare the motors
        DcMotorSimple Fleft =  hardwareMap.get(DcMotorSimple.class,"Fleft");
        DcMotor Bleft = hardwareMap.dcMotor.get("Bleft");
        DcMotor Fright = hardwareMap.dcMotor.get("Fright");
        DcMotor Bright = hardwareMap.dcMotor.get("Bright");
        DcMotor lift = hardwareMap.dcMotor.get("lift");
        Servo claw = hardwareMap.servo.get("Claw");

        // Reverse the motors
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Fright.setDirection(DcMotorSimple.Direction.REVERSE);
        Bright.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set the motor behavior
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(0);
        Bleft.setTargetPosition(0);
        Bright.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        claw.setPosition(1);

        waitForStart();

        if (isStopRequested()) return;
        while(opModeIsActive()){                // attempt to drive to a set motor position and mimic back motor power to the front motors in real time
            // set up telemetry to view values as the opmode executes
            telemetry.addLine()
                    .addData("FL Power", Fleft.getPower())
                    .addData("FR Power", Fright.getPower());
            telemetry.addLine()
                    .addData("BL Power", Bleft.getPower())
                    .addData("BR Power", Bright.getPower());
            telemetry.addLine()
                    .addData("BR Enc value",Bright.getCurrentPosition())
                    .addData("BL Enc value",Bleft.getCurrentPosition());
            telemetry.update();

            // simply move straight (hopefully) forward to encoder position 1000
            Bleft.setTargetPosition(1000);              // set back motors and power in RTP mode
            Bright.setTargetPosition(1000);
            Bleft.setPower(.5);
            Bright.setPower(.5);
            while(Bleft.isBusy()){                       // loop to send rear motor power values to front motors and mimic movement
                Fleft.setPower(Bleft.getPower());
                if (Bright.isBusy()){
                    Fright.setPower(Bright.getPower());
                    telemetry.update();                 // update values on display
                } else {
                    break;                              // end loop when movement has stopped
                }
            }
            // pause before movement sequence 2
            sleep(1000);

            // second movement is attempting to spin to the right
            Bleft.setTargetPosition(1000);              // set new target positions and reduce motor power
            Bright.setTargetPosition(0);
            Bleft.setPower(.3);
            Bright.setPower(.3);
            while(Bleft.isBusy()){                       // loop to set rear motor power values to front motors and mimic movement
                Fleft.setPower(Bleft.getPower());
                if (Bright.isBusy()){
                    Fright.setPower(Bright.getPower());
                    telemetry.update();                 // update values on display
                } else {
                    break;                              // end loop when movement has stopped
                }
            }
            break;                                       // end opmode when sequence of movements has stopped
        }
    }
}

