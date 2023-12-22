package org.firstinspires.ftc.teamcode.autos

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.apache.commons.math3.util.FastMath.toRadians
import org.firstinspires.ftc.teamcode.Board

@Autonomous
class redRightAuto : OpMode() {
    private val board = Board()
    var drive: SampleMecanumDrive? = null

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

    private val slideHeight: Int = 6250

    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)
        board.getHW(hardwareMap, telemetry, true)

        drive!!.poseEstimate = Pose2d(12.0, -61.0, toRadians(90.0))

        firstTrajectory = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)
            .lineToConstantHeading(Vector2d(drive!!.poseEstimate.x, -58.5))
            .lineToConstantHeading(Vector2d(26.0, -58.5)).build()

        pixelTrajectory1 = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)
            .lineToLinearHeading(Pose2d(12.0, -35.0, toRadians(270.0))).build()

        pixelTrajectory2 = drive!!.trajectorySequenceBuilder(firstTrajectory!!.end())
            .lineToLinearHeading(Pose2d(22.0, -45.0, toRadians(270.0))).build()

        pixelTrajectory3 = drive!!.trajectorySequenceBuilder(firstTrajectory!!.end())
            .lineToLinearHeading(Pose2d(11.0, -32.0, 0.0))
            .lineToLinearHeading(Pose2d(7.0, -32.0, 0.0)).build()

        return1 = drive!!.trajectorySequenceBuilder(pixelTrajectory1!!.end())
            .lineToConstantHeading(Vector2d(20.0, -40.0))
            .lineToLinearHeading(Pose2d(12.0, -61.0, toRadians(90.0))).build()

        return2 = drive!!.trajectorySequenceBuilder(pixelTrajectory2!!.end())
            .lineToLinearHeading(Pose2d(12.0, -61.0, toRadians(90.0))).build()

        return3 = drive!!.trajectorySequenceBuilder(pixelTrajectory3!!.end())
            .lineToLinearHeading(Pose2d(22.0, pixelTrajectory3!!.end().y, 0.0))
            .lineToLinearHeading(Pose2d(18.0, -60.0, toRadians(90.0)))
            .lineToLinearHeading(Pose2d(12.0, -61.0, toRadians(90.0))).build()

        boardTrajectory = drive!!.trajectorySequenceBuilder(Pose2d(12.0, -61.0, toRadians(90.0)))
            .splineToLinearHeading(Pose2d(54.0, -36.0, 0.0), 0.0).build()

        parkTrajectory = drive!!.trajectorySequenceBuilder(boardTrajectory!!.end())
            .lineToConstantHeading(Vector2d(35.0, -46.0))
            .splineToLinearHeading(Pose2d(59.0, -53.0, 0.0), 0.0).build()
    }

    override fun init_loop() {
        try { //start of TensorFlow
            board.eyes.tfod!!.recognitions.forEach { telemetry.addLine("found $it") }
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
                step = try {
                    if (board.eyes.tfod!!.recognitions.size != 0
                        && board.eyes.tfod!!.recognitions[0].right >= 480
                    )
                        "spike1"
                    else "not1"
                } catch (_: Throwable) {
                    "not1"
                }
            }

            "not1" -> {
                drive!!.followTrajectorySequenceAsync(firstTrajectory)
                step = "*not1"
            }

            "*not1" -> {
                drive!!.update()
                if (!drive!!.isBusy) {
                    resetRuntime()
                    step = "**not1"
                }
            }

            "**not1" -> {
                if (runtime == 0.5) step = "***not1"
            }

            "***not1" -> {
                step =
                    try {
                        if (board.eyes.tfod!!.recognitions.size != 0 &&
                            board.eyes.tfod!!.recognitions[0].left >= 240
                        )
                            "spike2"
                        else "not2"
                    } catch (_: Throwable) {
                        "not2"
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
                if (runtime >= 1.0) {
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
                if (runtime >= 1.0) {
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
                if (runtime >= 1.0) {
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
                    board.setSlideTar(slideHeight)
                    step = "score"
                }
            }

            "score" -> {
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
        telemetry.addData("The current step is ", "$step.")
    }
}