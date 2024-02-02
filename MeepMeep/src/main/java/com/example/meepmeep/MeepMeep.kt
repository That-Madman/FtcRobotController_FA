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
                        Pose2d(-35.0, 32.0, Math.toRadians(90.0))
                    )
                        .lineToConstantHeading(Vector2d(-46.5, 34.0))
                        .lineToConstantHeading(Vector2d(-46.5, 10.0))
                        .lineToLinearHeading(Pose2d(-20.0, 10.0, Math.toRadians(180.0)))
                        .lineToConstantHeading(Vector2d(35.0, 10.0))
                        .lineToLinearHeading(Pose2d(44.0, 30.5, 0.0))
                        .lineToConstantHeading(Vector2d(51.0, 44.5))
                        .build()
                }
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start()
    }
}