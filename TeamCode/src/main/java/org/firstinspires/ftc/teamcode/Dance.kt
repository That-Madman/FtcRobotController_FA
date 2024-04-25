package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import props

@TeleOp
class Dance : OpMode() {

    private var props: props? = null

    private var trueNorth: Boolean = false
    private var xHeld: Boolean = false

    override fun init() {
        props = props(hardwareMap)
    }

    override fun loop() {
        if (gamepad1.x && xHeld) trueNorth = !trueNorth

        if (trueNorth)
            props!!.driveFieldRelative(
                gamepad1.left_stick_x.toDouble(),
                gamepad1.left_stick_y.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )
        else
            props!!.drive(
                gamepad1.left_stick_x.toDouble(),
                gamepad1.left_stick_y.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )

        telemetry.addData("True north is ", if (trueNorth) "on." else "off.")
        xHeld = gamepad1.x
    }
}