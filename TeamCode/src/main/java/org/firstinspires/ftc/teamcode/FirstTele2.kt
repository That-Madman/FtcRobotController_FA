package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class FirstTele2 : OpMode() {
    private var trueNorth = false
    private var y1Held = false
    private var clawOpen = false
    private var a2Held = false
    private var rot = 0
    private var slide = 0.0
    private var wristRot = 0.0
    private val board = Board()

    override fun init() {
        board.getHW(hardwareMap, telemetry)
    }

    override fun loop() {
        if (gamepad1.y && !y1Held) trueNorth = !trueNorth

        if (trueNorth) {
            board.driveFieldRelative(
                -gamepad1.right_stick_y.toDouble(),
                gamepad1.right_stick_x.toDouble(),
                gamepad1.left_stick_x.toDouble()
            )
        } else {
            board.drive(
                -gamepad1.right_stick_y.toDouble(),
                gamepad1.right_stick_x.toDouble(),
                gamepad1.left_stick_x.toDouble()
            )
        }

        if (gamepad2.dpad_down) rot -= 10 else if (gamepad2.dpad_up) rot += 10
        rot.coerceIn(0..1880)
        board.setRot(rot)

        if (gamepad2.a && !a2Held) clawOpen = !clawOpen
        board.setClaw(clawOpen)

        if (gamepad2.left_bumper) wristRot += 0.1
        if (gamepad2.right_bumper) wristRot -= 0.1
        wristRot.coerceIn(0.0..1.0)
        board.setWrist(wristRot)

        slide = 0.5 * (gamepad2.right_trigger - gamepad2.left_trigger)
        board.setSlide(slide)

        y1Held = gamepad1.y
        a2Held = gamepad2.a

        telemetry.addData("True North Enabled?", trueNorth)

        if (gamepad2.x) board.launch()
    }

    override fun stop() {
        board.relatch()
    }
}