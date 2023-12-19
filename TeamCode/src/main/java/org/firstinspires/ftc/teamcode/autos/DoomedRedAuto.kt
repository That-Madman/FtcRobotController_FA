package org.firstinspires.ftc.teamcode.autos

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import java.lang.Math.toRadians

@Autonomous(name = "WE ARE DOOMED (RED)")
class DoomedRedAuto : OpMode() {
    var drive: SampleMecanumDrive? = null
    private var park: TrajectorySequence? = null

    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)

        drive!!.poseEstimate = Pose2d(12.0, -61.0, toRadians(90.0))
        park = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)
            .lineToConstantHeading(Vector2d(12.0, -59.0))
            .lineTo(Vector2d(59.0, -59.0))
            .build()
        drive!!.followTrajectorySequenceAsync(park)
    }

    override fun loop() {
        drive!!.update()
    }
}