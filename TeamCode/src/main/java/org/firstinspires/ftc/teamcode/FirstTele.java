package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FirstTele extends OpMode {
    Board board = new Board();

    boolean trueNorth,
            y1Held,
            a2Held,
            bumperLeftHeld,
            bumperRightHeld = false;
    boolean clawOpen = true;
    double inDir = 0;


    @Override
    public void init() {
        board.getHW(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.y && !y1Held) trueNorth = !trueNorth;
        if (gamepad2.a && !a2Held) clawOpen = !clawOpen;

        if (trueNorth) {
            board.driveFieldRelative(
                    -gamepad1.right_stick_y,
                    gamepad1.right_stick_x,
                    gamepad1.left_stick_x
            );
        } else {
            board.drive(
                    -gamepad1.right_stick_y,
                    gamepad1.right_stick_x,
                    gamepad1.left_stick_x
            );
        }

        board.setClaw(clawOpen);

        try {
            board.setSlideTar(
                    board.getSlidePos() +
                            ((int) (gamepad2.right_trigger - gamepad2.left_trigger) * 1000));
        } catch (Throwable e) {
            telemetry.addData("Issue with lift because ", e);
        }

        if (gamepad1.right_bumper && !bumperRightHeld) {
            inDir = 1.0;
        } else if (gamepad1.left_bumper && !bumperLeftHeld) {
            inDir = -1.0;
        } else if ((gamepad1.right_bumper && !bumperRightHeld && inDir == 1.0)
                || (gamepad1.left_bumper && !bumperLeftHeld && inDir == -1.0)) {
            inDir = 0.0;
        }
        board.setIntake(inDir);

        y1Held = gamepad1.y;
        a2Held = gamepad2.a;

        telemetry.addData("True North Enabled?", trueNorth);

        if (gamepad2.x) board.launch();
        if (gamepad2.y) board.relatch();

        bumperLeftHeld = gamepad1.left_bumper;
        bumperRightHeld = gamepad1.right_bumper;

        if (gamepad2.left_bumper) board.theHookBringsYouBack(1);
        else if (gamepad2.right_bumper) board.theHookBringsYouBack(-1);
        else board.theHookBringsYouBack(0);
    }

    @Override
    public void stop() {
        board.relatch();
    }
}
