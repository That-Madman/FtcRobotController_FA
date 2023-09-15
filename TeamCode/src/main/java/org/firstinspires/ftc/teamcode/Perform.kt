package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class Perform : OpMode() {
    var dir = Stage_Directions()
    override fun init() {
        dir.drumRoll(hardwareMap)
    }

    override fun loop() {
        dir.driveFieldRelative(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )
    }
}