package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import props

@TeleOp
class Dance : OpMode() {

    private var props: props? = null;

    override fun init() {
        props = props(hardwareMap)
    }

    override fun loop() {
        props!!.driveFieldRelative(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )
    }
}