package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx

@TeleOp(group = "Tests")
class RotLimFind : OpMode() {
    private var rot1: DcMotorImplEx? = null
    private var rot2: DcMotorImplEx? = null
    override fun init() {
        rot1 = hardwareMap.get(DcMotorImplEx::class.java, "armRotateMotor")
        rot1!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        rot1!!.power = 0.0

        rot2 = hardwareMap.get(DcMotorImplEx::class.java, "armRotateMotor2")
        rot2!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        rot2!!.power = 0.0
    }

    override fun loop() {
        telemetry.addData("Rot1 val:", rot1!!.currentPosition)
        telemetry.addData("Rot2 val:", rot2!!.currentPosition)
    }
}