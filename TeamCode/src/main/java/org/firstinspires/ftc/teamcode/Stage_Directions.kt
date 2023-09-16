package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin

class Stage_Directions {
    private var frontRight : DcMotorImplEx? = null
    private var frontLeft : DcMotorImplEx? = null
    private var backRight : DcMotorImplEx? = null
    private var backLeft : DcMotorImplEx? = null
    private var imu : IMU? = null

    fun drumRoll (hardwareMap: HardwareMap) {
        frontRight = hardwareMap.get(DcMotorImplEx::class.java, "frontRight")
        frontLeft = hardwareMap.get(DcMotorImplEx::class.java, "frontLeft")
        backRight = hardwareMap.get(DcMotorImplEx::class.java, "backRight")
        backLeft = hardwareMap.get(DcMotorImplEx::class.java, "backLeft")

        frontRight?.direction = DcMotorSimple.Direction.FORWARD
        frontLeft?.direction = DcMotorSimple.Direction.REVERSE
        backRight?.direction = DcMotorSimple.Direction.FORWARD
        backLeft?.direction = DcMotorSimple.Direction.REVERSE

        frontRight?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        frontLeft?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        backRight?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        backLeft?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        frontRight?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        frontLeft?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backRight?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backLeft?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        imu = hardwareMap.get(IMU::class.java, "imu")
        imu!!.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        )
    }
    fun driveFieldRelative(forward: Double, right: Double, rotate: Double) {
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
        val pows = arrayOf(frontLeftPower, frontRightPower, backLeftPower, backRightPower)
        var maxSpeed = 1.0
        for (i in 0..3){
            maxSpeed = max(maxSpeed, abs(pows[i]))
        }
        for (i in 0..3) {
            pows[i] = pows[i] / maxSpeed
        }
        frontLeft?.power = pows[0]
        frontRight?.power = pows[1]
        backLeft?.power = pows[2]
        backRight?.power = pows[3]
    }
}