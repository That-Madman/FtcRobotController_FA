package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class USE_THIS_AUTO_IF_WE_ARE_DOOMED extends LinearOpMode {
    Board board = new Board();
    int pos = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        board.getHW(hardwareMap, telemetry);
        board.changeToPos();
        waitForStart();
        board.resetWheels();
        board.posRunSide(pos);
        while ((board.getWheelPos(0) <= pos)
                && (board.getWheelPos(1) <= pos)
                && (board.getWheelPos(2) <= pos)
                && (board.getWheelPos(3) <= pos)
                && !isStopRequested()) {
        }
    }
}
