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
        } catch (Throwable e) {
            telemetry.addData("Could not access hardware because ", e);
        }
        try {
            board.changeToPos();
        } catch (Throwable e) {
            telemetry.addData("Trouble with accessing wheels because ", e);
        }

        while (!opModeIsActive()) {
            if (spikeSpot == 0) { //start of TensorFlow
                try {
                    List<Recognition> view = board.getEyes().getTfod().getRecognitions();

                    if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                        if ((view.get(0).getLeft() + view.get(0).getRight()) / 2 <= 240) {
                            spikeSpot = 1;
                        } else if ((
                                view.get(0).getLeft() + view.get(0).getRight()) / 2 > 240
                                &&
                                (view.get(0).getLeft() + view.get(0).getRight()) / 2 < 480
                        ) {
                            spikeSpot = 2;
                        } else if ((view.get(0).getLeft() + view.get(0).getRight()) / 2 >= 480) {
                            spikeSpot = 3;
                        }
                    }
                    telemetry.addData("spike found at:", (spikeSpot != 0) ? spikeSpot : "n/a");
                } catch (Throwable e) {
                    telemetry.addData("Error in using camera because:", e);
                }
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
            } catch (Throwable e) {
                telemetry.addData("Issue with April Tags because ", e);
            }
        }
        waitForStart();

        try {
            board.getEyes().getVisionPortal().stopStreaming();
        } catch (Throwable e) {
            telemetry.addData("Problem ending vision portal because: ", e);
        }

        if (opModeIsActive()) {
            try{
            if (spikeSpot == 0) {
                board.getEyes().getVisionPortal().resumeStreaming();
                board.posRun(100);
                while (board.getWheelPos(1) > 90 && board.getWheelPos(1) < 110) {
                }

                board.posRunSide(100);

                if (board.getEyes().getTfod().getRecognitions().size() != 0) spikeSpot = 3;
                else spikeSpot = 1;

                board.posRunSide(-100);
                while (board.getWheelPos(1) > 90 && board.getWheelPos(1) < 110) {
                }
                board.getEyes().getVisionPortal().stopStreaming();
            }} catch (Throwable e){
                telemetry.addLine("Trouble with camera because " + e);
            }
            if(spikeSpot == 1) {
                board.posRun(200);
                board.setIntake(-1);
                sleep(1000);
            }
        }
    }
}