package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Board
import org.firstinspires.ftc.teamcode.HookDir

@TeleOp(group = "Tests")
class TestHook : OpMode() {
    private val board = Board()

    private var dir: HookDir = HookDir.STATIONARY

    private var held = false

    override fun init() {
        board.getHW(hardwareMap)
    }

    override fun loop() {
        if(gamepad1.a && !held && dir == HookDir.STATIONARY) dir = HookDir.UP
        else if(gamepad1.a && !held && dir == HookDir.UP) dir = HookDir.DOWN
        else if (gamepad1.a && !held && dir == HookDir.DOWN) dir = HookDir.STATIONARY

        board.theHookBringsYouBack(dir.pow)
        telemetry.addData("hook direction", dir)
    }
}