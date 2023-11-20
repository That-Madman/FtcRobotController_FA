package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class USE_THIS_AUTO_IF_WE_ARE_DOOMED extends LinearOpMode {
    Board board = new Board();
    @Override
    public void runOpMode() throws InterruptedException {
        board.getHW(hardwareMap, telemetry);
        board.changeToPos();
        waitForStart();
        board.posRunSide(500);
    }
}
