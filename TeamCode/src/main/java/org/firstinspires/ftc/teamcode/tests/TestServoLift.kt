package org.firstinspires.ftc.teamcode.tests

import autoThings.PID
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(group = "Tests")
class TestServoLift : OpMode() {
    private var hook2: DcMotor? = null
    private var intakeLiftServo: CRServo? = null
    private val intakeLiftPID = PID(0.001,
        0.0,
        0.0,
        { hook2!!.currentPosition },
        { intakeLiftServo!!.power = it as Double },
        { runtime })

    private var tarPos = 0
    override fun init() {
        intakeLiftServo = hardwareMap.get(CRServo::class.java, "intakeServoLift")
        intakeLiftServo!!.direction = DcMotorSimple.Direction.REVERSE

        hook2 = hardwareMap.get(DcMotor::class.java, "hook2")
        hook2!!.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        hook2!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        hook2!!.direction = DcMotorSimple.Direction.FORWARD
        hook2!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
        intakeLiftPID.pidCalc(tarPos)

        tarPos += (gamepad1.left_trigger - gamepad1.right_trigger).toInt() *10

        telemetry.addData("Target Position = ", tarPos)
        telemetry.addData("Current Position = ", hook2!!.currentPosition)
    }

}