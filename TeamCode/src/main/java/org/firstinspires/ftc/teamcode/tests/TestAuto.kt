package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Board

@Autonomous (group = "Tests")
class TestAuto : OpMode() {

    val board = Board()

    override fun init() {
        board.getHW(hardwareMap, telemetry)
        telemetry.addLine("Init should be good.")
    }

    override fun loop() {
        telemetry.addLine("Loop should be good")
    }
}