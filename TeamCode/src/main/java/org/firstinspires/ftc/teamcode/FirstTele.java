package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FirstTele extends OpMode {
    boolean trueNorth = false;
    boolean yHeld = false;

    boolean clawOpen = false;
    boolean aHeld = false;

    int rot = 0;
    double wristRot = 0;
    Stage_Directions board = new Stage_Directions();

    @Override
    public void init() {
        board.getHW(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.y && !yHeld) trueNorth = !trueNorth;
        if (gamepad2.a && !aHeld) clawOpen = !clawOpen;

        if (trueNorth) {
            board.driveFieldRelative(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        } else {
            board.drive(-gamepad1.right_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        }

        if (gamepad2.dpad_down) rot++;
        if (gamepad2.dpad_up) rot--;
        if (rot <= 0) rot = 0;
        if (rot >= 750) rot = 750;
        board.setRot(rot);

        board.setClaw(clawOpen);

        if(gamepad2.left_bumper) wristRot++;
        if (gamepad2.right_bumper) wristRot--;
        board.setWrist(wristRot);

        yHeld = gamepad1.y;
        aHeld = gamepad2.a;
    }
}
