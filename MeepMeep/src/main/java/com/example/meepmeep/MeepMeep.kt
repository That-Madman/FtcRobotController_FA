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
        val meepMeep = MeepMeep(600)
        val myBot =
            DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(
                    45.762138431627456,
                    52.48291908330528,
                    2.4920900344848635,
                    185.04921600000003.toRadians(),
                    14.97
                )
                .followTrajectorySequence { drive: DriveShim ->
                    drive.trajectorySequenceBuilder(
                        Pose2d(12.0, -61.0, Math.toRadians(90.0))
                    )
                        .splineToConstantHeading(Vector2d(12.0, -39.0), Math.toRadians(90.0))
                        .splineToLinearHeading(Pose2d(12.0, -36.0, 0.0), Math.toRadians(90.0))
                        .splineToConstantHeading(Vector2d(10.0, -28.0), Math.toRadians(135.0))
                        .lineToConstantHeading(Vector2d(7.0, -28.0))
                        .build()
                }
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start()
    }
}