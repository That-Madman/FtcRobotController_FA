package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FirstTele extends OpMode {
    boolean trueNorth = false;
    boolean y1Held = false;

    boolean clawOpen = false;
    boolean a2Held = false;

    int rot = 0;
    double slide = 0;
    double wristRot = 0;
    Board board = new Board();

    @Override
    public void init() {
        board.getHW(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.y && !y1Held) trueNorth = !trueNorth;
        if (gamepad2.a && !a2Held) clawOpen = !clawOpen;

        if (trueNorth) {
            board.driveFieldRelative(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        } else {
            board.drive(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        }

        if (gamepad2.dpad_down) rot -= 10;
        else if (gamepad2.dpad_up) rot += 10;
        if (rot >= 1880) rot = 1880;
        if (rot <= 0) rot = 0;
        board.setRot(rot);

        board.setClaw(clawOpen);

        if (gamepad2.left_bumper) wristRot += 0.1;
        if (gamepad2.right_bumper) wristRot -= 0.1;
        if (wristRot >= 1) wristRot = 1;
        if (wristRot <= 0) wristRot = 0;
        board.setWrist(wristRot);

        slide = 0.5 * (gamepad2.right_trigger - gamepad2.left_trigger);
        board.setSlide(slide);

        y1Held = gamepad1.y;
        a2Held = gamepad2.a;

        telemetry.addData("True North Enabled?", trueNorth);

        if (gamepad2.x) board.launch();
    }

    @Override
    public void stop() {
        board.relatch();
    }
}
