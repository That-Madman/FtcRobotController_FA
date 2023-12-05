package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class TeleKt : OpMode() {
    private val board = Board()
    private var trueNorth = false
    private var y1Held = false
    private var a2Held = false
    private var bumperLeftHeld = false
    private var bumperRightHeld = false
    private var clawOpen = true
    private var inDir = 0.0
    override fun init() {
        board.getHW(hardwareMap, telemetry)
    }

    override fun loop() {
        if (gamepad1.y && !y1Held) trueNorth = !trueNorth
        if (gamepad2.a && !a2Held) clawOpen = !clawOpen
        if (trueNorth) {
            board.driveFieldRelative(
                -gamepad1.right_stick_y.toDouble(),
                 gamepad1.right_stick_x.toDouble(),
                 gamepad1.left_stick_x.toDouble(),
            )
        } else {
            board.drive(
                -gamepad1.right_stick_y.toDouble(),
                 gamepad1.right_stick_x.toDouble(),
                 gamepad1.left_stick_x.toDouble(),
            )
        }
        board.setClaw(clawOpen)
        board.setSlide((gamepad2.right_trigger - gamepad2.left_trigger).toDouble())
        if (gamepad1.right_bumper && !bumperRightHeld) {
            inDir = 1.0
        } else if (gamepad1.left_bumper && !bumperLeftHeld) {
            inDir = -1.0
        } else if (gamepad1.right_bumper && !bumperRightHeld && inDir == 1.0 || gamepad1.left_bumper && !bumperLeftHeld && inDir == -1.0) {
            inDir = 0.0
        }
        board.setIntake(inDir)

        y1Held = gamepad1.y
        a2Held = gamepad2.a

        telemetry.addData("True North Enabled?", trueNorth)

        if (gamepad2.x) board.launch()
        if (gamepad2.y) board.relatch()

        bumperLeftHeld = gamepad1.left_bumper
        bumperRightHeld = gamepad1.right_bumper

        if (gamepad2.left_bumper) board.theHookBringsYouBack(1.0)
        else if (gamepad2.right_bumper) board.theHookBringsYouBack(
            -1.0
        ) else board.theHookBringsYouBack(0.0)
    }

    override fun stop() {
        board.relatch()
    }
}