package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Board

@TeleOp(group = "Tests")
class DriveTest : OpMode() {
    private val board = Board()

    private var trueNorth = false
    private var bHeld = false

    override fun init() {
        board.getHW(hardwareMap, telemetry)
    }

    override fun loop() {
        if (trueNorth) board.driveFieldRelative(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        ) else board.drive(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )

        if (gamepad1.b && !bHeld) trueNorth = !trueNorth
        bHeld = gamepad1.b
    }
}