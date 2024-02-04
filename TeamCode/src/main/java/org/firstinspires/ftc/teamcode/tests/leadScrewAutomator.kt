package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(group = "Tests")
class leadScrewAutomator : OpMode() {
    private var hook1: DcMotor? = null
    private var hook2: DcMotor? = null

    private val hookLifts: Array<Servo?> = arrayOfNulls(2)

    private var start = true

    private var b2Held = false

    private var triggered = false

    private var hookServoUp = false

    private var y2Held = false
    override fun init() {
        hook1 = hardwareMap.get(DcMotor::class.java, "hook")
        hook1!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hook1!!.direction = DcMotorSimple.Direction.FORWARD
        hook1!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        hook2 = hardwareMap.get(DcMotor::class.java, "hook2")
        hook2!!.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        hook2!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hook2!!.direction = DcMotorSimple.Direction.FORWARD
        hook2!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        hookLifts[0] = hardwareMap.get(Servo::class.java, "hookServo1")
        hookLifts[1] = hardwareMap.get(Servo::class.java, "hookServo2")

        hookLifts[0]!!.direction = Servo.Direction.REVERSE
    }

    override fun loop() {
        if (start) {
            resetRuntime()
            start = !start
        }

        if (gamepad2.y && !y2Held) hookServoUp = !hookServoUp

        if (gamepad2.b && b2Held) {
            triggered = !triggered
            if (!triggered) start = !start
        }

        if (triggered && runtime < 3) {
            hook1!!.power = 1.0
            hook2!!.power = 1.0
            hookServoUp = true
        } else {
            if (gamepad2.left_bumper) theHookBringsYouBack(1.0)
            else if (gamepad2.right_bumper) theHookBringsYouBack(-1.0)
            else theHookBringsYouBack(0.0)
        }

        if (hookServoUp) {
            hookLifts[0]!!.position = 0.0
            hookLifts[1]!!.position = 1.0
        } else {
            hookLifts[0]!!.position = 1.0
            hookLifts[1]!!.position = 1.0
        }

        y2Held = gamepad2.y
    }

    private fun theHookBringsYouBack(pow: Double) {
        hook1?.power = pow
        hook2?.power = pow
    }
}