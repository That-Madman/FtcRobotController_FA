package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FirstTele extends OpMode {
    boolean trueNorth = false;
    boolean buttonHeld = false;
    Stage_Directions board = new Stage_Directions();

    @Override
    public void init() {
        board.getHW(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.y && !buttonHeld)
            trueNorth = !trueNorth;

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
        buttonHeld = gamepad1.y;
    }
}
