package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Servo

@Autonomous(group = "Tests")
class TestHookLiftServos : OpMode() {
    private val hookLifts = arrayOfNulls<Servo>(2)
    override fun init() {
        hookLifts[0] = hardwareMap.get(Servo::class.java, "hookServo1")
        hookLifts[1] = hardwareMap.get(Servo::class.java, "hookServo2")

        hookLifts[0]!!.direction = Servo.Direction.REVERSE

        hookLifts[0]!!.position = 0.0
        hookLifts[1]!!.position = 0.0
    }

    override fun loop() {}
}