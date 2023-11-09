package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@Autonomous
public class FirstAuto extends LinearOpMode {
    Board board = new Board();
    int spikeSpot = 0;

    public void runOpMode() {
        try {
            board.getHW(hardwareMap, telemetry);
        } catch (Exception | Error e) {
            telemetry.addData("Could not access hardware because ", e);
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
            } catch (Exception | Error e) {
                telemetry.addData("Error in using camera because:", e);
            } //end of tensorFlow

            try { //April tags
                if (board.getEyes().getApril().getDetections().size() > 0) {
                    AprilTagDetection tag = board.getEyes().getApril().getDetections().get(0);

                    telemetry.addData("x", tag.ftcPose.x);
                    telemetry.addData("y", tag.ftcPose.y);
                    telemetry.addData("z", tag.ftcPose.z);
                    telemetry.addData("roll", tag.ftcPose.roll);
                    telemetry.addData("pitch", tag.ftcPose.pitch);
                    telemetry.addData("yaw", tag.ftcPose.yaw);
                }
            } catch (Exception | Error e) {
                telemetry.addData("Issue with April Tags because ", e);
            }
        }
        waitForStart();

        try {
            board.getEyes().getVisionPortal().close();
        } catch (Exception | Error e) {
            telemetry.addData("Problem ending vision portal because: ", e);
        }

        if (opModeIsActive()) {
            while (opModeIsActive()) {
            }
        }
    }
}