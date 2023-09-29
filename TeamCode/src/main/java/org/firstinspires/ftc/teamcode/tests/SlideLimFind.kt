package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(group = "Tests")
class SlideLimFind : OpMode() {
    private var slideMotor: DcMotorImplEx? = null
    override fun init() {
        slideMotor = hardwareMap.get(DcMotorImplEx::class.java, "slideMotor")
        slideMotor?.direction = DcMotorSimple.Direction.FORWARD
        slideMotor?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        slideMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
        slideMotor!!.power = (gamepad2.right_trigger - gamepad2.left_trigger).toDouble()
        telemetry.addData("Slide Pos:", slideMotor!!.currentPosition)
    }
}