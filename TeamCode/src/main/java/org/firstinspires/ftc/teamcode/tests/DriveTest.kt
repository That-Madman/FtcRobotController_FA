package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin

@TeleOp(group = "Tests")
class DriveTest : OpMode() {
    private var driveBase = arrayOfNulls<DcMotorImplEx>(4)
    private var imu: IMU? = null

    private var trueNorth = false
    private var bHeld = false

    override fun init() {
        try {
            driveBase[0] = hardwareMap.get(DcMotorImplEx::class.java, "frontLeft")
            driveBase[1] = hardwareMap.get(DcMotorImplEx::class.java, "frontRight")
            driveBase[2] = hardwareMap.get(DcMotorImplEx::class.java, "backLeft")
            driveBase[3] = hardwareMap.get(DcMotorImplEx::class.java, "backRight")

            for (wheels in driveBase) {
                wheels?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                wheels?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            }

            driveBase[0]?.direction = DcMotorSimple.Direction.REVERSE
            driveBase[1]?.direction = DcMotorSimple.Direction.FORWARD
            driveBase[2]?.direction = DcMotorSimple.Direction.REVERSE
            driveBase[3]?.direction = DcMotorSimple.Direction.FORWARD
        } catch (e: Throwable) {
            throw IllegalArgumentException("Couldn't find the drivebase because $e.")
        }
        try {
            imu = hardwareMap.get(IMU::class.java, "imu")
            imu?.initialize(
                IMU.Parameters(
                    RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                    )
                )
            )
        } catch (e: Throwable) {
            telemetry.addLine("Couldn't find the IMU because $e")
        }
        telemetry.update()
    }

    override fun loop() {
        if (trueNorth) driveFieldRelative(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        ) else drive(
            -gamepad1.left_stick_y.toDouble(),
            gamepad1.left_stick_x.toDouble(),
            gamepad1.right_stick_x.toDouble()
        )

        if (gamepad1.b && !bHeld) trueNorth = !trueNorth
        bHeld = gamepad1.b
        telemetry.addData("True North is ", if (trueNorth) "on." else "off.")
    }

    private fun driveFieldRelative(forward: Double, right: Double, rotate: Double) {
        val robotAngle = imu!!.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)
        var theta = atan2(forward, right)
        val r = hypot(forward, right)
        theta = AngleUnit.normalizeRadians(theta - robotAngle)
        val newForward = r * sin(theta)
        val newRight = r * cos(theta)

        drive(newForward, newRight, rotate)
    }

    fun drive(forward: Double, right: Double, rotate: Double) {
        val frontLeftPower = forward + right + rotate
        val frontRightPower = forward - right - rotate
        val backLeftPower = forward - right + rotate
        val backRightPower = forward + right - rotate

        setPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower)
    }

    private fun setPowers(
        frontLeftPower: Double,
        frontRightPower: Double,
        backLeftPower: Double,
        backRightPower: Double
    ) {
        val powers = arrayOf(frontLeftPower, frontRightPower, backLeftPower, backRightPower)
        var maxSpeed = 1.0

        for (i in powers) {
            maxSpeed = max(maxSpeed, abs(i))
        }

        for (i in powers.indices) {
            powers[i] /= maxSpeed
        }

        for (i in powers.indices) {
            driveBase[i]?.power = powers[i]
        }
    }
}