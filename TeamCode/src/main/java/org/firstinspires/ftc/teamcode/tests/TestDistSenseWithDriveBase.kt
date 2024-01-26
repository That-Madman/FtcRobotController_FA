package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.abs
import kotlin.math.max

@Autonomous(group = "Tests")
class TestDistSenseWithDriveBase : OpMode() {
    private var distSense: DistanceSensor? = null
    private val driveBase: Array<DcMotorImplEx?> = arrayOfNulls<DcMotorImplEx?>(4)

    override fun init() {
        distSense = hardwareMap.get(DistanceSensor::class.java, "distSense")

        driveBase[0] = hardwareMap.get(DcMotorImplEx::class.java, "frontLeft")
        driveBase[1] = hardwareMap.get(DcMotorImplEx::class.java, "frontRight")
        driveBase[2] = hardwareMap.get(DcMotorImplEx::class.java, "backLeft")
        driveBase[3] = hardwareMap.get(DcMotorImplEx::class.java, "backRight")

        for (wheels in driveBase) {
            wheels?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            wheels?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }

        driveBase[0]?.direction = DcMotorSimple.Direction.REVERSE
        driveBase[1]?.direction = DcMotorSimple.Direction.FORWARD
        driveBase[2]?.direction = DcMotorSimple.Direction.REVERSE
        driveBase[3]?.direction = DcMotorSimple.Direction.FORWARD
    }

    override fun loop() {
        telemetry.addData("Distance is", "  ${distSense!!.getDistance(DistanceUnit.INCH)}")

        drive(
            -gamepad1.left_stick_y,
            gamepad1.left_stick_x,
            gamepad1.right_stick_x
        )
    }

    private fun drive(forward: Float, right: Float, rotate: Float) {
        val frontLeftPower = (forward + right + rotate).toDouble()
        val frontRightPower = (forward - right - rotate).toDouble()
        val backLeftPower = (forward - right + rotate).toDouble()
        val backRightPower = (forward + right - rotate).toDouble()

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
