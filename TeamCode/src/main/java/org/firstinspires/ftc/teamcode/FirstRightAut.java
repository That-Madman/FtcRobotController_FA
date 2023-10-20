package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class FirstRightAut extends OpMode {
    Board board = new Board();
    @Override
    public void init() {
        board.getHW(hardwareMap);
        board.changeToPos();
    }

    @Override
    public void loop() {
        board.setRot(100);
        board.posRunSide(-1528 * 2);
    }
}
