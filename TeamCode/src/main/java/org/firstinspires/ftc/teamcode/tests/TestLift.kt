package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(group = "Tests")
class TestLift : OpMode() {
    private var slideMotor: DcMotorImplEx? = null;
    override fun init() {
        slideMotor = hardwareMap.get(DcMotorImplEx::class.java, "slideMotor")
        slideMotor?.direction = DcMotorSimple.Direction.FORWARD
        slideMotor?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideMotor?.power = 1.0
        slideMotor?.targetPosition = 0
        slideMotor?.mode = DcMotor.RunMode.RUN_TO_POSITION
        slideMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
        slideMotor!!.targetPosition =
            slideMotor!!.currentPosition +
                    (gamepad1.right_trigger - gamepad1.left_trigger).toInt() * 1000

        telemetry.addLine("The slide is at ${slideMotor!!.currentPosition} tics.")
    }
}