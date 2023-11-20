package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Board

@TeleOp(group = "Tests")
class TestLauncher : OpMode() {
    private val board = Board()

    private var held = false
    private var launched = false

    override fun init() {
        board.getHW(hardwareMap)
    }

    override fun loop() {
        if (gamepad1.a && !held && !launched) {
            board.launch()
            launched = true
        } else if (gamepad1.a && !held && launched) {
            board.relatch()
            launched = false
        }
        held = gamepad1.a
    }
}