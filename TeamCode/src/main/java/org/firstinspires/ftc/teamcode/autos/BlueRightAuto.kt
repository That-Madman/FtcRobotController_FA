package org.firstinspires.ftc.teamcode.autos

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Board

@Disabled
@Autonomous
class BlueRightAuto : OpMode() {
    private val board = Board()
    private var drive: SampleMecanumDrive? = null

    private var step = "start"

    private var firstTrajectory: TrajectorySequence? = null

    private var pixelTrajectory1: TrajectorySequence? = null
    private var pixelTrajectory2: TrajectorySequence? = null
    private var pixelTrajectory3: TrajectorySequence? = null

    private var return1: TrajectorySequence? = null
    private var return2: TrajectorySequence? = null
    private var return3: TrajectorySequence? = null

    private var boardTrajectory: TrajectorySequence? = null
    private var parkTrajectory: TrajectorySequence? = null

    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)
        board.getHW(hardwareMap, telemetry, true)

        drive!!.poseEstimate = Pose2d(
            12.0, 61.0, Math.toRadians(270.0)
        )
        firstTrajectory = drive!!.trajectorySequenceBuilder(
            Pose2d(12.0, 61.0, Math.toRadians(270.0))
        ).lineToConstantHeading(Vector2d(20.0, 61.0)).build()
    }

    override fun init_loop() {
        try { //start of TensorFlow
            board.eyes.tfod!!.recognitions.forEach { telemetry.addLine("found $it") }
        } catch (e: Throwable) {
            telemetry.addData("Error in using camera because:", e)
        } //end of tensorFlow
        try { //start of April tags
            if (board.eyes.april!!.detections.size > 0) {
                val tag = board.eyes.april!!.detections[0]

                //use aprilTagDetection class to find april tags/get data
                telemetry.addData("x", tag.ftcPose.x)
                telemetry.addData("y", tag.ftcPose.y)
                telemetry.addData("z", tag.ftcPose.z)
                telemetry.addData("roll", tag.ftcPose.roll)
                telemetry.addData("pitch", tag.ftcPose.pitch)
                telemetry.addData("yaw", tag.ftcPose.yaw)
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because ", e)
        } // end of April Tags
    }

    override fun loop() {
        when (step) {
            "start" -> {
                step = if (board.eyes.tfod!!.recognitions.size != 0) "spike1"
                else "not1"
            }

            "not1" -> {
                drive!!.followTrajectorySequenceAsync(firstTrajectory)
                step = "*not1"
            }

            "*not1" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    step = if (board.eyes.tfod!!.recognitions.size != 0) "spike2"
                    else "not2"
                }
            }

            "spike1" -> {
                drive!!.followTrajectorySequenceAsync(pixelTrajectory1)
                step = "*spike1"
            }

            "*spike1" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setIntake(-1.0)
                    resetRuntime()
                    step = "**spike1"
                }
            }

            "**spike1" -> {
                if (runtime >= 1000.0) {
                    board.setIntake(0.0)
                    drive!!.followTrajectorySequenceAsync(return1)
                    step = "board"
                }
            }

            "spike2" -> {
                drive!!.followTrajectorySequenceAsync(pixelTrajectory2)
                step = "*spike2"
            }

            "*spike2" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setIntake(-1.0)
                    resetRuntime()
                    step = "**spike2"
                }
            }

            "**spike2" -> {
                if (runtime >= 1000.0) {
                    board.setIntake(0.0)
                    drive!!.followTrajectorySequenceAsync(return2)
                    step = "board"
                }
            }

            "not2" -> {
                drive!!.followTrajectorySequenceAsync(pixelTrajectory3)
                step = "spike3"
            }

            "spike3" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setIntake(-1.0)
                    resetRuntime()
                    step = "*spike3"
                }
            }

            "*spike3" -> {
                if (runtime >= 1000.0) {
                    board.setIntake(0.0)
                    drive!!.followTrajectorySequenceAsync(return3)
                    step = "board"
                }
            }

            "board" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    drive!!.followTrajectorySequenceAsync(boardTrajectory)
                    step = "toBoard"
                }
            }

            "toBoard" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setSlideTar(1000)
                    step = "score"
                }
            }

            "score" -> {
                if (board.getSlidePos()!! >= 1000) {
                    board.setClaw(true)
                    resetRuntime()
                    step = "drop"
                }
            }

            "drop" -> {
                if (runtime >= 500.0) {
                    board.setClaw(false)
                    drive!!.followTrajectorySequenceAsync(parkTrajectory)
                    step = "park"
                }
            }

            "park" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setSlideTar(0)
                    step = "done"
                }
            }
        }
        telemetry.addData("current step = ", step)
    }
}