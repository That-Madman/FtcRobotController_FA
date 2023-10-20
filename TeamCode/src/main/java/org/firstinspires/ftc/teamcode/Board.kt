package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
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

class Board {
    private val OPEN: Double = 1.0
    private val CLOSE: Double = 0.0

    private var wheel: Array<DcMotorImplEx?> = arrayOfNulls<DcMotorImplEx?>(4)
    private var slideMotor: DcMotorImplEx? = null
    private var armRotateMotor: DcMotorImplEx? = null
    private var armRotateMotor2: DcMotorImplEx? = null

    private var wrist: Array<Servo?> = arrayOfNulls(2)

    private var claw: Servo? = null

    private var intakeServo: CRServo? = null
    private var intakeMotor: DcMotorImplEx? = null

    private var imu: IMU? = null

    private var visionPortal: VisionPortal? = null

    private fun initVision(hwMap: HardwareMap) {
        val builder = VisionPortal.Builder()
        try {
            builder.setCamera(hwMap.get(WebcamName::class.java, "Webcam 1"))
        } catch (_: Exception) {
        }
        builder.addProcessors(
            TfodProcessor.Builder().build(), AprilTagProcessor.easyCreateWithDefaults()
        )
        visionPortal = builder.build()
    }

    fun vision(): VisionPortal? {
        return visionPortal
    }

    fun getHW(hwMap: HardwareMap, telemetry: Telemetry) {
        val broken: ArrayList<String> = ArrayList()
        initVision(hwMap)

        try {
            slideMotor = hwMap.get(DcMotorImplEx::class.java, "slideMotor")
            slideMotor?.direction = Direction.FORWARD
            slideMotor?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            slideMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        } catch (_: Exception) {
            broken.add("slide Motor")
        }

        try {
            armRotateMotor = hwMap.get(DcMotorImplEx::class.java, "armRotateMotor")
            armRotateMotor?.direction = Direction.REVERSE
            armRotateMotor?.targetPosition = 0
            armRotateMotor?.power = 1.0
            armRotateMotor?.mode = DcMotor.RunMode.RUN_TO_POSITION
            armRotateMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        } catch (_: Exception) {
            broken.add("Arm Rotate Motor")
        }

        try {
            armRotateMotor2 = hwMap.get(DcMotorImplEx::class.java, "armRotateMotor2")
            armRotateMotor2?.direction = Direction.FORWARD
            armRotateMotor2?.targetPosition = 0
            armRotateMotor2?.power = 1.0
            armRotateMotor2?.mode = DcMotor.RunMode.RUN_TO_POSITION
            armRotateMotor2?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        } catch (_: Exception) {
            broken.add("Second Arm Rotate Motor")
        }

        try {
            wheel[0] = hwMap.get(DcMotorImplEx::class.java, "frontLeft")
            wheel[1] = hwMap.get(DcMotorImplEx::class.java, "frontRight")
            wheel[2] = hwMap.get(DcMotorImplEx::class.java, "backLeft")
            wheel[3] = hwMap.get(DcMotorImplEx::class.java, "backRight")

            for (wheels in wheel) {
                wheels?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                wheels?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            }

            wheel[0]?.direction = Direction.REVERSE
            wheel[1]?.direction = Direction.FORWARD
            wheel[2]?.direction = Direction.REVERSE
            wheel[3]?.direction = Direction.FORWARD
        } catch (_: Exception) {
            broken.add("Drivebase")
        }

        try {
            wrist[0] = hwMap.get(Servo::class.java, "wrist1")
            wrist[1] = hwMap.get(Servo::class.java, "wrist2")

            wrist[1]!!.direction = Servo.Direction.REVERSE
        } catch (_: Exception) {
            broken.add("Wrist")
        }

        try {
            claw = hwMap.get(Servo::class.java, "claw")
        } catch (_: Exception) {
            broken.add("Claw")
        }
        try {
            intakeMotor = hwMap.get(DcMotorImplEx::class.java, "intakeMotor")
            intakeMotor?.direction = Direction.FORWARD
        } catch (_: Exception) {
            broken.add("Intake Motor")
        }

        try {
            intakeServo = hwMap.get(CRServoImplEx::class.java, "intakeServo")
            intakeServo?.direction = Direction.REVERSE
        } catch (_: Exception) {
            broken.add("Intake Servo")
        }

        imu = hwMap.get(IMU::class.java, "imu")
        imu?.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
            )
        )
        if (broken.isNotEmpty())
            telemetry.addData("the following could not be accessed", broken)
    }

    fun changeToPos() {
        for (wheels in wheel) {
            wheels?.targetPosition = 0
            wheels?.power = 0.5
            wheels?.mode = DcMotor.RunMode.RUN_TO_POSITION
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

    fun setRot(pos: Int) {
        armRotateMotor?.targetPosition = pos
        armRotateMotor2?.targetPosition = pos
    }

    fun setSlide(pow: Double) {
        slideMotor?.power = pow
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

    fun setIntake(speed: Double) {
        intakeMotor?.power = speed
        intakeServo?.power = speed
    }
}
