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
    int slide = 0;
    //todo: find the actual limits
    int rotMin = 0;
    int rotMax = 750;
    int slideMin = 0;
    int slideMax = 750;

    double wristRot = 0;
    Stage_Directions board = new Stage_Directions();

    @Override
    public void init() {
        board.getHW(hardwareMap);
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

        if (gamepad2.dpad_down) rot++;
        if (gamepad2.dpad_up) rot--;
        if (rot <= rotMin) rot = rotMin;
        if (rot >= rotMax) rot = rotMax;
        board.setRot(rot);

        board.setClaw(clawOpen);

        if (gamepad2.left_bumper) wristRot++;
        if (gamepad2.right_bumper) wristRot--;
        board.setWrist(wristRot);

        slide = (int) (gamepad2.right_trigger - gamepad2.left_trigger);
        if (slide <= slideMin) slide = slideMin;
        if (slide >= slideMax) slide = slideMax;
        board.setSlide(slide);

        y1Held = gamepad1.y;
        a2Held = gamepad2.a;
    }
}
