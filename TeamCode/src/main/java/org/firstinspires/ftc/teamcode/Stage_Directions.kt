package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
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
    private val OPEN: Double = 1.0
    private val CLOSE: Double = 0.0

    private var wheel: Array<DcMotorImplEx?> = arrayOfNulls<DcMotorImplEx?>(4)
    private var slideMotor: DcMotorImplEx? = null
    private var armRotateMotor: DcMotorImplEx? = null
    private var armRotateMotor2: DcMotorImplEx? = null

    private var wrist: Array<Servo?> = arrayOfNulls(2)

    private var claw: Servo? = null

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

        slideMotor = hwMap.get(DcMotorImplEx::class.java, "slideMotor")
        slideMotor?.direction = DcMotorSimple.Direction.FORWARD
        slideMotor?.targetPosition = 0
        slideMotor?.mode = RunMode.RUN_TO_POSITION
        slideMotor?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        armRotateMotor = hwMap.get(DcMotorImplEx::class.java, "armRotateMotor")
        armRotateMotor?.direction = DcMotorSimple.Direction.FORWARD
        armRotateMotor?.targetPosition = 0
        armRotateMotor?.mode = RunMode.RUN_TO_POSITION
        armRotateMotor?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        armRotateMotor2 = hwMap.get(DcMotorImplEx::class.java, "armRotateMotor2")
        armRotateMotor2?.direction = DcMotorSimple.Direction.FORWARD
        armRotateMotor2?.targetPosition = 0
        armRotateMotor2?.mode = RunMode.RUN_TO_POSITION
        armRotateMotor2?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE

        wheel[0] = hwMap.get(DcMotorImplEx::class.java, "frontLeft")
        wheel[1] = hwMap.get(DcMotorImplEx::class.java, "frontRight")
        wheel[2] = hwMap.get(DcMotorImplEx::class.java, "backLeft")
        wheel[3] = hwMap.get(DcMotorImplEx::class.java, "backRight")

        for (i in 0..3) {
            wheel[i]?.mode = RunMode.RUN_WITHOUT_ENCODER
            wheel[i]?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        }

        wheel[0]?.direction = DcMotorSimple.Direction.REVERSE
        wheel[1]?.direction = DcMotorSimple.Direction.FORWARD
        wheel[2]?.direction = DcMotorSimple.Direction.REVERSE
        wheel[3]?.direction = DcMotorSimple.Direction.FORWARD

        wrist[0] = hwMap.get(Servo::class.java, "wrist1")
        wrist[1] = hwMap.get(Servo::class.java, "wrist2")

        wrist[1]!!.direction = Servo.Direction.REVERSE

        claw = hwMap.get(Servo::class.java, "claw")

        imu = hwMap.get(IMU::class.java, "imu")
        imu?.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
            )
        )
    }

    fun changeToPos() {
        for (n in 0..3) {
            wheel[n]!!.targetPosition = 0
            wheel[n]!!.mode = RunMode.RUN_TO_POSITION
        }
    }

    fun posRunSide(target: Int) {
        wheel[0]!!.targetPosition = -target
        wheel[1]!!.targetPosition = target
        wheel[2]!!.targetPosition = target
        wheel[3]!!.targetPosition = -target
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
        wheel[0]?.power = pows[0]
        wheel[1]?.power = pows[1]
        wheel[2]?.power = pows[2]
        wheel[3]?.power = pows[3]
    }

    fun setRot(position: Int) {
        armRotateMotor?.targetPosition = position
        armRotateMotor2?.targetPosition = position
    }

    fun setSlide(position: Int) {
        slideMotor?.targetPosition = position
    }

    fun setClaw(open: Boolean) {
        if (open) {
            claw?.position = OPEN
        } else {
            claw?.position = CLOSE
        }
    }

    fun setWrist(position: Double) {
        wrist[0]?.position = position
        wrist[1]?.position = position
    }
}
