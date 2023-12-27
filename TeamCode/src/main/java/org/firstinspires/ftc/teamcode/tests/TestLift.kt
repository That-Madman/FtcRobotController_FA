package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(group = "Tests")
class TestLift : OpMode() {
    private var slideMotor: DcMotorImplEx? = null
    private var dropper: Servo? = null

    private var dropperPos: Int = 0
    private var bHeld: Boolean = false

    override fun init() {
        slideMotor = hardwareMap.get(DcMotorImplEx::class.java, "slideMotor")
        slideMotor?.direction = DcMotorSimple.Direction.FORWARD
        slideMotor?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideMotor?.power = 1.0
        slideMotor?.targetPosition = 0
        slideMotor?.mode = DcMotor.RunMode.RUN_TO_POSITION
        slideMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


        dropper = hardwareMap.get(Servo::class.java, "claw")
        dropper!!.position = 1.0
    }

    override fun loop() {
        if (gamepad1.b && !bHeld) ++dropperPos

        slideMotor!!.targetPosition =
            slideMotor!!.currentPosition +
                    (gamepad1.right_trigger - gamepad1.left_trigger).toInt() * 1000

        when (dropperPos % 3) {
            0 -> dropper!!.position = 1.0
            1 -> dropper!!.position = 0.5
            2 -> dropper!!.position = 0.0
        }

        bHeld = gamepad1.b

        telemetry.addLine("The slide is at ${slideMotor!!.currentPosition} tics.")
    }
}