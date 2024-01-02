package org.firstinspires.ftc.teamcode.autos

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import java.lang.Math.toRadians

class DoomedRedLeftAuto: OpMode() {
    private var drive: SampleMecanumDrive? = null
    private var park: TrajectorySequence? = null
    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)

        drive!!.poseEstimate = Pose2d(-35.0, -61.0, Math.toRadians(270.0))

        park = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)
            .lineToConstantHeading(Vector2d(-35.0, -55.0))
            .lineToConstantHeading(Vector2d(-56.5, -55.0))
            .lineToConstantHeading(Vector2d(-56.5, -10.0))
            .lineToLinearHeading(Pose2d(-20.0, -10.0, toRadians(180.0)))
            .lineToConstantHeading(Vector2d(59.0, -10.0))
            .build()
    }

    override fun loop() {
        TODO("Not yet implemented")
    }
}