package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

import autoThings.roadRunner.drive.SampleMecanumDrive;

public class AprilDrivingTest extends OpMode {
    final int DESIRED_TAG_ID = -1; //TODO change this to different tags for testing
    final double
            DESIRED_DISTANCE = 8, // inches
            speedGain = 0.02,
            strafeGain = 0.015,
            turnGain = 0.01,
            maxAutoSpeed = 0.5,
            maxAutoStrafe = 0.5,
            maxAutoTurn = 0.3;
    boolean targetFound = false;
    double
            driveAprilTag,
            strafeAprilTag,
            turnAprilTag = 0;
    private AprilTagDetection desiredTag = null;
    Board board = new Board();
    SampleMecanumDrive drive;
    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        board.getEyes();
    }

    @Override
    public void init_loop() {
        try { //start of TensorFlow
            board.getEyes().getTfod().getRecognitions().forEach(
                    thing -> telemetry.addLine("found " + thing)
            );
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
        telemetry.update();
    }

    @Override
    public void loop() {
        List<AprilTagDetection> currentDetections = board.getEyes().getApril().getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;
                }
            }
        }

        if (targetFound) {
            // calculate range, heading, yaw error to figure out what the speed of the bot should be
            double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
            double headError = desiredTag.ftcPose.bearing - 0;
            double yawError = desiredTag.ftcPose.yaw - 0;

            driveAprilTag = Range.clip(
                    rangeError * speedGain,
                    -maxAutoSpeed,
                    maxAutoSpeed);
            strafeAprilTag = Range.clip(
                    -yawError * strafeGain,
                    -maxAutoStrafe,
                    maxAutoStrafe);
            turnAprilTag = Range.clip(
                    headError * turnGain,
                    -maxAutoTurn,
                    maxAutoTurn);

//            board.drive(driveAprilTag, strafeAprilTag, turnAprilTag);
            drive.setDrivePower(new Pose2d(driveAprilTag, strafeAprilTag, turnAprilTag));
            //april tag done
        }
    }
}
