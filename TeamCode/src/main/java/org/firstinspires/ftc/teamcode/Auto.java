package org.firstinspires.ftc.teamcode;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import autoThings.roadRunner.drive.SampleMecanumDrive;
import autoThings.roadRunner.trajectorysequence.TrajectorySequence;

@Autonomous
public class Auto extends OpMode {
    Board board = new Board();

    SampleMecanumDrive drive;
    TrajectorySequence sequence;

    @Override
    public void init() {
        try {
            drive = new SampleMecanumDrive(hardwareMap);
            board.getHW(hardwareMap, telemetry);
        } catch (Throwable e) {
            telemetry.addData("Could not access hardware because ", e);
        }
        try {
            board.changeToPos();
        } catch (Throwable e) {
            telemetry.addData("Trouble with accessing wheels because ", e);
        }
        drive.setPoseEstimate(new Pose2d(-36.0, 61.0, toRadians(270.0)));

        sequence = drive.trajectorySequenceBuilder(new Pose2d(-36.0, 61.0, toRadians(270.0)))
                .lineToConstantHeading(new Vector2d(-36.0, 35.0))
                .addDisplacementMarker(() -> {
                    try {
                        if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                            board.setIntake(-1);
                            wait(1000);
                            board.setIntake(0);
                        }
                        drive.turn(90);
                        if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                            board.setIntake(-1);
                            wait(1000);
                            board.setIntake(0);
                        }
                        drive.turn(180 - 1e-6);
                        if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                            board.setIntake(-1);
                            wait(1000);
                            board.setIntake(0);
                        }
                        drive.turn(90);
                    } catch (Throwable e) {
                        telemetry.addData("Could not see because", e);
                    }
                })
                .lineToConstantHeading(new Vector2d(-36.0, 40.0))
                .splineToConstantHeading(new Vector2d(-56.0, 50.0), toRadians(135.0))
                .lineToConstantHeading(new Vector2d(-56.0, 12.0))
                .lineToConstantHeading(new Vector2d(-55.0, 12.0))
                .splineToConstantHeading(new Vector2d(-20.0, 0.0), toRadians(270.0))
                .lineToConstantHeading(new Vector2d(20.0, 0.0))
                .addDisplacementMarker(() -> {
                })
                .splineToSplineHeading(new Pose2d(50.0, 30.0, 0.0), 0.0)
                .addDisplacementMarker(() -> {
                })
                .lineToConstantHeading(new Vector2d(45.0, 30.0))
                .splineToConstantHeading(new Vector2d(62.0, 12.0), toRadians(10.0))
                .build();

        telemetry.addLine("compiled");
        telemetry.update();
        drive.followTrajectorySequence(sequence);
    }

//    @Override
//    public void init_loop() {
//        try { //start of TensorFlow
//            List<Recognition> view = board.getEyes().getTfod().getRecognitions();
//
//            view.forEach(thing -> telemetry.addData("found ", thing));
//        } catch (Throwable e) {
//            telemetry.addData("Error in using camera because:", e);
//        } //end of tensorFlow
//
//        try { //start of April tags
//            if (board.getEyes().getApril().getDetections().size() > 0) {
//                AprilTagDetection tag = board.getEyes().getApril().getDetections().get(0);
//
//                telemetry.addData("x", tag.ftcPose.x);
//                telemetry.addData("y", tag.ftcPose.y);
//                telemetry.addData("z", tag.ftcPose.z);
//                telemetry.addData("roll", tag.ftcPose.roll);
//                telemetry.addData("pitch", tag.ftcPose.pitch);
//                telemetry.addData("yaw", tag.ftcPose.yaw);
//            }
//        } catch (Throwable e) {
//            telemetry.addData("Issue with April Tags because ", e);
//        } // end of April Tags
//    }

    @Override
    public void loop() {
        drive.update();
    }
}
