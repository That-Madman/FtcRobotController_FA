package org.firstinspires.ftc.teamcode.autos.doomed

import autoThings.roadRunner.drive.SampleMecanumDrive
import autoThings.roadRunner.trajectorysequence.TrajectorySequence
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import java.lang.Math.toRadians

@Autonomous(name = "WE ARE DOOMED (BLUE RIGHT)", group = "Doomed")
class DoomedBlueRightAuto : OpMode() {

    private var drive: SampleMecanumDrive? = null
    private var park: TrajectorySequence? = null



    override fun init() {
        drive = SampleMecanumDrive(hardwareMap)

        drive!!.poseEstimate = Pose2d(-35.0, 61.0, toRadians(270.0))

        park = drive!!.trajectorySequenceBuilder(drive!!.poseEstimate)
            .lineToConstantHeading(Vector2d(-35.0, 55.0))
            .lineToConstantHeading(Vector2d(-56.5, 55.0))
            .lineToConstantHeading(Vector2d(-56.5, 8.0))
            .lineToLinearHeading(Pose2d(-20.0, 8.0, toRadians(180.0)))
            .lineToConstantHeading(Vector2d(48.0, 8.0))
            .build()

        drive!!.followTrajectorySequenceAsync(park)

        telemetry.addLine("Hello World")
    }


    override fun loop() {
        drive!!.update()
    }
}