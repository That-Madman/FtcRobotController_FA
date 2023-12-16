package org.firstinspires.ftc.teamcode;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import autoThings.roadRunner.drive.SampleMecanumDrive;
import autoThings.roadRunner.trajectorysequence.TrajectorySequence;

//@Disabled
@Autonomous
public class Auto extends OpMode {
    Board board = new Board();

    SampleMecanumDrive drive;
    TrajectorySequence sequence;

    boolean Harvey;
    private AprilTagDetection desiredTag = null;

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

        sequence = drive.trajectorySequenceBuilder(new Pose2d(-36.0, 61.0, toRadians(270.0)))
                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(
                        40,
                        1.5,
                        14.97))
                .lineToLinearHeading(new Pose2d(-36.0, 35.0, toRadians(270)))
                .addDisplacementMarker(() -> {
                    try {
                        if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                            board.setIntake(-1);
                            wait(1000);
                        }
                    } catch (Throwable e) {
                        telemetry.addData("Could not see because", e);
                    } finally {
                        board.setIntake(0);
                    }
                })
                .turn(toRadians(-90))
                .addDisplacementMarker(() -> {
                    try {
                        if (board.getEyes().getTfod().getRecognitions().size() != 0) {
                            board.setIntake(-1);
                            wait(1000);
                        }
                    } catch (Throwable e) {
                        telemetry.addData("Could not see because", e);
                    } finally {
                        board.setIntake(0);
                    }
                })
                .turn(toRadians(180) + 1e-6)
                .addDisplacementMarker(() -> {
                    try {
                        board.setIntake(-1);
                        wait(1000);
                    } catch (Throwable e) {
                        telemetry.addData("Could not see because", e);
                    } finally {
                        board.setIntake(0);
                    }
                })
                .turn(toRadians(-90))
                .lineToLinearHeading(new Pose2d(-36.0, 40.0, toRadians(270)))
                .splineToConstantHeading(new Vector2d(-53.0, 50.0), toRadians(135.0))
                .lineToLinearHeading(new Pose2d(-53.0, 12.0, toRadians(270)))
                .splineToLinearHeading(new Pose2d(-20.0, 0.0, 0), toRadians(270.0))
                .lineToLinearHeading(new Pose2d(20.0, 0.0, 0))
//                .addDisplacementMarker(() -> {
//                })
                .splineToSplineHeading(new Pose2d(44.0, 30.0, 0.0), 0.0)
                .addDisplacementMarker(() -> {
                    // TODO implement April Tags
                })
                .splineToConstantHeading(new Vector2d(49.0, 50.0), 0.0)
                .addDisplacementMarker(() -> {
                            board.setSlideTar(2000);
                            Harvey = true;
                        }
                )
                .addDisplacementMarker(() -> board.setClaw(true))
                .lineToLinearHeading(new Pose2d(40.0, 40.0, 0))
                .splineToLinearHeading(new Pose2d(60.0, 25.0, 0), toRadians(10.0))
                .build();

        telemetry.addLine("compiled");
        telemetry.update();
        drive.followTrajectorySequenceAsync(sequence);
    }

    @Override
    public void init_loop() {
        try { //start of TensorFlow
            board.getEyes().getTfod().getRecognitions()
                    .forEach(thing -> telemetry.addLine("found " + thing));
        } catch (Throwable e) {
            telemetry.addData("Error in using camera because:", e);
        } //end of tensorFlow

        try { //start of April tags
            if (board.getEyes().getApril().getDetections().size() > 0) {
                AprilTagDetection tag = board.getEyes().getApril().getDetections().get(0);

                //use aprilTagDetection class to find april tags/get data
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
        try {
            if (!Harvey || board.getSlidePos() > 1000) drive.update();
        } catch (Throwable ignored) {
            drive.update();
        }
    }
}
