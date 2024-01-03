package org.firstinspires.ftc.teamcode.teleOps

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Board

@TeleOp(name = "New Wave TeleOp")
class TeleK : OpMode() {
    private var board = Board()
    private var trueNorth = false
    private var y1Held = false
    private var a2Held = false
    private var rightHeld = false
    private var inDirOn = false
    private var inDir = 0.0
    private var dropperPos = 0

    override fun init() {
        board.getHW(hardwareMap, telemetry)

        try {
            board.setIntakeLift(0.0)
        } catch (_: Throwable) {
            telemetry.addLine("The servo intake lifter has an issue.")
        }

        telemetry.update()
    }

    override fun loop() {
        if (gamepad1.y && !y1Held) {
            trueNorth = !trueNorth
            board.resetIMU()
        }
        if (gamepad2.a && !a2Held) ++dropperPos

        if (trueNorth) {
            board.driveFieldRelative(
                -gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )
        } else {
            board.drive(
                -gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble()
            )
        }

        board.setDrop(dropperPos)

        try {
            board.setSlideTar(
                board.getSlidePos()!!
                        + (gamepad2.right_trigger - gamepad2.left_trigger).toInt() * 1000
            )
        } catch (e: Throwable) {
            telemetry.addData("Issue with lift because ", e)
        }

        if (gamepad1.right_bumper) {
            inDirOn = true
        } else if (gamepad1.left_bumper) {
            inDirOn = false
        }

        if (inDirOn && inDir != 1.0 && gamepad1.right_bumper && !rightHeld) inDir = 1.0
        else if (inDirOn && inDir != -1.0 && gamepad1.right_bumper && !rightHeld) inDir = -1.0
        else if (!inDirOn) inDir = 0.0
        board.setIntake(inDir)

        y1Held = gamepad1.y
        a2Held = gamepad2.a
        rightHeld = gamepad1.right_bumper
        telemetry.addData("True North Enabled?", trueNorth)

        if (gamepad2.x) board.launch()
        if (gamepad2.y) board.relatch()

        if (gamepad2.left_bumper) board.theHookBringsYouBack(1.0)
        else if (gamepad2.right_bumper) board.theHookBringsYouBack(-1.0)
        else board.theHookBringsYouBack(0.0)
    }

    override fun stop() {
        board.relatch()
    }
}
