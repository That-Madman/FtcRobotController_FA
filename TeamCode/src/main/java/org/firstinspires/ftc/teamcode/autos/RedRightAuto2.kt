package org.firstinspires.ftc.teamcode.autos

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import autoThings.slideHeight
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Board

@Autonomous(name = "New Red Right")
class RedRightAuto2 : OpMode() {
    private val board = Board()
    private var drive: SampleMecanumDrive? = null

    private var spike1: TrajectorySequence? = null
    private var spike2: TrajectorySequence? = null
    private var spike3: TrajectorySequence? = null

    private var board1: TrajectorySequence? = null
    private var board2: TrajectorySequence? = null
    private var board3: TrajectorySequence? = null

    private var park1: TrajectorySequence? = null
    private var park2: TrajectorySequence? = null
    private var park3: TrajectorySequence? = null

    private var step = "start"
    private var spike = 3

    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)
        board.getHW(hardwareMap, telemetry, true)

        drive!!.poseEstimate = Pose2d(12.0, 61.0, Math.toRadians(270.0))

        spike1 = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)

            .build()
    }

    override fun init_loop() {
        try { //start of TensorFlow
            board.eyes.tfod!!.recognitions.forEach {
                telemetry.addLine("found $it")
                if (it.right <= 480) spike = 1
                else if (it.right >= 480) spike = 2
            }
        } catch (e: Throwable) {
            telemetry.addData("Error in using camera because:", e)
        } //end of tensorFlow

        try { //start of April tags
            board.eyes.april!!.detections.forEach {
                //use aprilTagDetection class to find april tags/get data
                telemetry.addLine("x of tag ${it.id} is ${it.ftcPose.x}")
                telemetry.addLine("y of tag ${it.id} is ${it.ftcPose.y}")
                telemetry.addLine("z of tag ${it.id} is ${it.ftcPose.z}")
                telemetry.addLine("roll of tag ${it.id} is ${it.ftcPose.roll}")
                telemetry.addLine("pitch of ${it.id} is ${it.ftcPose.pitch}")
                telemetry.addLine("yaw of ${it.id} is ${it.ftcPose.yaw}")
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because: ", e)
        } // end of April Tags
        telemetry.update()
    }

    override fun loop() {
        when (step) {
            "start" -> {
                try {
                    if (board.eyes.tfod!!.recognitions.size != 0
                        && board.eyes.tfod!!.recognitions[0].right >= 480
                    ) spike = 2
                    else if (board.eyes.tfod!!.recognitions.size != 0
                        && board.eyes.tfod!!.recognitions[0].right <= 480
                    ) spike = 1
                } catch (_: Throwable) {
                    try {
                        if (board.eyes.tfod!!.recognitions.size != 0
                            && board.eyes.tfod!!.recognitions[0].right <= 480
                        ) spike = 1
                    } catch (_: Throwable) {
                    }
                } finally {
                    when (spike) {
                        1 -> drive!!.followTrajectorySequenceAsync(spike1)
                        2 -> drive!!.followTrajectorySequenceAsync(spike2)
                        3 -> drive!!.followTrajectorySequenceAsync(spike3)
                        else -> throw Error("We are at a point that shouldn't even exist.")
                    }
                }
                step = "spikeScore"
            }

            "spikeScore" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setIntake(-1.0)
                    resetRuntime()
                    step = "ejectPixel"
                }
            }

            "ejectPixel" -> {
                if (runtime >= 1.0) {
                    board.setIntake(0.0)
                    when (spike) {
                        1 -> drive!!.followTrajectorySequenceAsync(board1)
                        2 -> drive!!.followTrajectorySequenceAsync(board2)
                        3 -> drive!!.followTrajectorySequenceAsync(board3)
                        else -> throw Error("We are at a point that shouldn't even exist.")
                    }
                    step = "boardDrive"
                }
            }

            "boardDrive" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    board.setSlideTar(slideHeight)
                    step = "scoreboard"
                }
            }

            "scoreboard" -> {
                telemetry.addData("current lift position: ", board.getSlidePos())
                if (board.getSlidePos()!! >= slideHeight) {
                    board.setClaw(false)
                    resetRuntime()
                    step = "drop"
                }
            }

            "drop" -> {
                if (runtime >= 2) {
                    board.setClaw(true)
                    when (spike) {
                        1 -> drive!!.followTrajectorySequenceAsync(park1)
                        2 -> drive!!.followTrajectorySequenceAsync(park2)
                        3 -> drive!!.followTrajectorySequenceAsync(park3)
                        else -> throw Error("We are at a point that shouldn't even exist.")
                    }

                    board.setSlideTar(0)
                    step = "park"
                }
            }

            "park" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    step = "done"
                }
            }
        }
        telemetry.addData("The current step is ", "$step.")
    }
}
