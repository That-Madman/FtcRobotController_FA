package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous
public class FirstAuto extends LinearOpMode {
    Board board = new Board();
    int spikeSpot = 0;


    public void runOpMode() {
        try {
            board.getHW(hardwareMap, telemetry);
        } catch (Exception ignored) {
        }
        while (!opModeIsActive()) {
            try { //start of TensorFlow
                List<Recognition> view = board.getEyes().getTfod().getRecognitions();
                if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                    if ((view.get(0).getLeft() + view.get(0).getRight()) / 2 <= 240) {
                        spikeSpot = 1;
                    } else if ((view.get(0).getLeft() + view.get(0).getRight()) / 2 > 240 && (view.get(0).getLeft() + view.get(0).getRight()) / 2 < 480) {
                        spikeSpot = 2;
                    } else if ((view.get(0).getLeft() + view.get(0).getRight()) / 2 >= 480) {
                        spikeSpot = 3;
                    }
                }
                telemetry.addData("spike found at:", (spikeSpot != 0) ? spikeSpot : "n/a");
            } catch (Exception e) {
                telemetry.addData("Error in using camera because:", e);
            } //end of tensorFlow
        }
        waitForStart();

        try {
            try {
                board.getEyes().getVisionPortal().close();
            } catch (Exception e) {
                telemetry.addData("Problem ending vision portal because: ", e);
            }
        } catch (Error e) {
            telemetry.addData("Problem ending vision portal because: ", e);
        }

        if (opModeIsActive()) {
            while (opModeIsActive()) {
            }
        }
    }
}