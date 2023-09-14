package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Perform extends OpMode {
    Stage_Directions dir = new Stage_Directions();
    @Override
    public void init() {
        dir.drumRoll(hardwareMap);
    }

    @Override
    public void loop() {
        dir.driveFieldRelative(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x);
    }
}
