package org.firstinspires.ftc.teamcode;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import autoThings.roadRunner.drive.SampleMecanumDrive;
import autoThings.roadRunner.trajectorysequence.TrajectorySequence;

public class SplitAuto extends OpMode {
    Board board = new Board();

    SampleMecanumDrive drive;
    TrajectorySequence sequence1, sequence2;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        try {
            board.getHW(hardwareMap, telemetry, true);
        } catch (Throwable e) {
            telemetry.addData("Could not access hardware because ", e);
        }
        try {
            board.changeToPos();
        } catch (Throwable e) {
            telemetry.addData("Trouble with accessing wheels because ", e);
        }
        drive.setPoseEstimate(new Pose2d(-36.0, 61.0, toRadians(270.0)));

        sequence1 = drive.trajectorySequenceBuilder(new Pose2d(-36.0, 61.0, toRadians(270.0)))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(
                        40,
                        1.5,
                        14.97))
                .lineToConstantHeading(new Vector2d(-36.0, 35.0))
                .addDisplacementMarker(() -> {
                    // TODO figure out how to make this work
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
                    } finally {
                        drive.followTrajectorySequence(sequence2);
                    }
                })
                .build();

        sequence2 = drive.trajectorySequenceBuilder(sequence1.end())
                .addDisplacementMarker(() -> drive.updatePoseEstimate())
                .lineToConstantHeading(new Vector2d(-36.0, 40.0))
                .splineToConstantHeading(new Vector2d(-53.0, 50.0), toRadians(135.0))
                .lineToConstantHeading(new Vector2d(-53.0, 12.0))
                .splineToConstantHeading(new Vector2d(-20.0, 0.0), toRadians(270.0))
                .lineToConstantHeading(new Vector2d(20.0, 0.0))
                .addDisplacementMarker(() -> {
                    //TODO what was this for? I need to figure that out
                })
                .splineToSplineHeading(new Pose2d(44.0, 30.0, 0.0), 0.0)
                .addDisplacementMarker(() -> {
                    // TODO implement April Tags
                })
                .splineToSplineHeading(new Pose2d(49.0, 30.0, 0.0), 0.0)
                .addDisplacementMarker(() -> {
                    // TODO implement lift
                }).lineToConstantHeading(new Vector2d(45.0, 25.0))
                .splineToConstantHeading(new Vector2d(60.0, 12.0), toRadians(10.0))
                .build();

        telemetry.addLine("compiled");
        telemetry.update();
        drive.followTrajectorySequenceAsync(sequence1);
    }

    @Override
    public void init_loop() {
        try { //start of TensorFlow
            board.getEyes().getTfod().getRecognitions().forEach(thing -> telemetry.addLine("found " + thing));
        } catch (Throwable e) {
            telemetry.addData("Error in using camera because:", e);
        } //end of tensorFlow

        try { //start of April tags
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
        } // end of April Tags
    }

    @Override
    public void loop() {
        drive.update();
    }

}
