package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Board;
import org.firstinspires.ftc.teamcode.UpAndDownServoLift;

@TeleOp(name = "TeleOp for Kaden")
public class KadenTele extends OpMode {
    Board board = new Board();

    boolean trueNorth,
            hookServoUp,
            y1Held,
            y2held,
            a2Held,
            rightHeld,
            inDirOn = false;

    double inDir = 0;

    int dropperPos = 0;


    @Override
    public void init() {
        board.getHW(hardwareMap, telemetry);

        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.y && !y1Held) {
            trueNorth = !trueNorth;
            board.resetIMU();
        }
        if (gamepad2.a && !a2Held) ++dropperPos;

        if (gamepad2.y && !y2held) hookServoUp = !hookServoUp;

        if (trueNorth) {
            board.driveFieldRelative(
                    -gamepad1.right_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );
        } else {
            board.drive(
                    -gamepad1.right_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );
        }

        board.setDrop(dropperPos);

        try {
            board.setSlideTar(
                    board.getSlidePos() +
                            ((int) (gamepad2.right_trigger - gamepad2.left_trigger) * 1000));
        } catch (Throwable e) {
            telemetry.addData("Issue with lift because ", e);
        }

        try {
            if (board.bumpers() || hookServoUp) board.hookServo(0.0);
            else board.hookServo(1.0);
        } catch (Throwable ignored) {
        }

        if (gamepad1.right_bumper) {
            inDirOn = true;
        } else if (gamepad1.left_bumper) {
            inDirOn = false;
        }
        if (inDirOn && inDir != 1.0 && gamepad1.right_bumper && !rightHeld) inDir = 1.0;
        else if (inDirOn && inDir != -1.0 && gamepad1.right_bumper && !rightHeld) inDir = -1.0;
        else if (!inDirOn) inDir = 0.0;
        board.setIntake(inDir);

        y1Held = gamepad1.y;
        y2held = gamepad2.y;
        a2Held = gamepad2.a;
        rightHeld = gamepad1.right_bumper;

        telemetry.addData("True North Enabled?", trueNorth);

        if (gamepad2.x) board.launch();
        if (gamepad2.y) board.relatch();

        if (gamepad2.left_bumper) board.theHookBringsYouBack(1);
        else if (gamepad2.right_bumper) board.theHookBringsYouBack(-1);
        else board.theHookBringsYouBack(0);

        board.setIntakeHeight(UpAndDownServoLift.DOWN.getPos());
    }

    @Override
    public void stop() {
        board.relatch();
    }
}
