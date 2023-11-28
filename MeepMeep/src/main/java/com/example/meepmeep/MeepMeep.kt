package com.example.meepmeep

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.core.toRadians
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.DriveShim

object MeepMeep {
    @JvmStatic
    fun main(args: Array<String>) {
        val meepMeep = MeepMeep(800)
        val myBot =
            DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(
                    60.0,
                    60.0,
                    Math.toRadians(180.0),
                    Math.toRadians(180.0),
                    15.0)
                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(Pose2d(
                        -36.0, 61.0, 270.0.toRadians()))
                        .lineToConstantHeading(Vector2d(-36.0, 35.0))
                        .addDisplacementMarker{}
                        .lineToConstantHeading(Vector2d(-36.0, 40.0))
                        .splineToConstantHeading(Vector2d(-56.0, 50.0), 135.0.toRadians())
                        .lineToConstantHeading(Vector2d(-56.0, 12.0))
                        .lineToConstantHeading(Vector2d(-55.0, 12.0))
                        .splineTo(Vector2d(-20.0, 0.0), 270.0.toRadians())
                        .lineToConstantHeading(Vector2d(20.0, 0.0))
                        .addDisplacementMarker{}
                        .splineToSplineHeading(Pose2d(50.0, 30.0, 0.0), 0.0)
                        .addDisplacementMarker{}
                        .lineToConstantHeading(Vector2d(45.0, 30.0))
                        .splineToConstantHeading(Vector2d(62.0, 12.0), 10.0.toRadians())
                        .build()
                }
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start()
    }
}