package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin

class Stage_Directions {
    private var wheel: Array<DcMotorImplEx>? = emptyArray<DcMotorImplEx>()
    private var imu: IMU? = null

    var visionPortal: VisionPortal? = null

    private fun initVision(hwMap: HardwareMap) {
        val tensor = TfodProcessor.Builder().build()
        val aprilTag = AprilTagProcessor.easyCreateWithDefaults()

        val builder = VisionPortal.Builder()
        builder.setCamera(hwMap.get(WebcamName::class.java, "Webcam 1"))


        builder.addProcessor(tensor)
        builder.addProcessor(aprilTag)

        visionPortal = builder.build()
    }

    fun getHW(hwMap: HardwareMap) {
        initVision(hwMap)

        wheel?.set(0, hwMap.get(DcMotorImplEx::class.java, "frontLeft"))
        wheel?.set(1, hwMap.get(DcMotorImplEx::class.java, "frontRight"))
        wheel?.set(2, hwMap.get(DcMotorImplEx::class.java, "backLeft"))
        wheel?.set(3, hwMap.get(DcMotorImplEx::class.java, "backRight"))

        for (i in 0..3) {
            wheel?.get(i)?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            wheel?.get(i)?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }

        wheel?.get(1)?.direction = DcMotorSimple.Direction.FORWARD
        wheel?.get(0)?.direction = DcMotorSimple.Direction.REVERSE
        wheel?.get(3)?.direction = DcMotorSimple.Direction.FORWARD
        wheel?.get(2)?.direction = DcMotorSimple.Direction.REVERSE

        imu = hwMap.get(IMU::class.java, "imu")
        imu?.initialize(
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
        for (i in pows) {
            maxSpeed = max(maxSpeed, abs(i))
        }
        for (i in 0..3) {
            pows[i] /= maxSpeed
        }
        wheel?.get(0)?.power = pows[0]
        wheel?.get(1)?.power = pows[1]
        wheel?.get(2)?.power = pows[2]
        wheel?.get(3)?.power = pows[3]
    }
}