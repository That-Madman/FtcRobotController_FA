package org.firstinspires.ftc.teamcode

import autoThings.AEyes
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
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin

class Board {
    private val open: Double = 1.0
    private val close: Double = 0.0

    private var driveBase: Array<DcMotorImplEx?> = arrayOfNulls<DcMotorImplEx?>(4)
    private var slideMotor: DcMotorImplEx? = null

    private var dropper: Servo? = null

    private var intakeServo: CRServo? = null
    private var intakeMotor: DcMotorImplEx? = null

    private var hook: DcMotor? = null

    private var imu: IMU? = null

    private var launchServo: Servo? = null

    var eyes = AEyes()

    @JvmOverloads
    fun getHW(hwMap: HardwareMap, telemetry: Telemetry? = null, auto: Boolean = false) {
        val broken = ArrayList<String>(0)

        try {
            eyes.initVision(hwMap)
        } catch (_: Throwable) {
            broken.add("Camera")
        }

        try {
            slideMotor = hwMap.get(DcMotorImplEx::class.java, "slideMotor")
            slideMotor?.direction = Direction.FORWARD
            slideMotor?.power = 1.0
            slideMotor?.targetPosition = 0
            slideMotor?.mode = DcMotor.RunMode.RUN_TO_POSITION
            slideMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        } catch (_: Throwable) {
            broken.add("slide Motor")
        }
        if (!auto) {
            try {
                driveBase[0] = hwMap.get(DcMotorImplEx::class.java, "frontLeft")
                driveBase[1] = hwMap.get(DcMotorImplEx::class.java, "frontRight")
                driveBase[2] = hwMap.get(DcMotorImplEx::class.java, "backLeft")
                driveBase[3] = hwMap.get(DcMotorImplEx::class.java, "backRight")

                for (wheels in driveBase) {
                    wheels?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                    wheels?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
                }

                driveBase[0]?.direction = Direction.REVERSE
                driveBase[1]?.direction = Direction.FORWARD
                driveBase[2]?.direction = Direction.REVERSE
                driveBase[3]?.direction = Direction.FORWARD
            } catch (_: Throwable) {
                broken.add("Drivebase")
            }
        }
        try {
            dropper = hwMap.get(Servo::class.java, "claw")
            dropper!!.position = 1.0
        } catch (_: Throwable) {
            broken.add("Claw")
        }

        try {
            intakeMotor = hwMap.get(DcMotorImplEx::class.java, "intakeMotor")
            intakeMotor?.direction = Direction.FORWARD
        } catch (_: Throwable) {
            broken.add("Intake Motor")
        }

        try {
            intakeServo = hwMap.get(CRServoImplEx::class.java, "intakeServo")
            intakeServo?.direction = Direction.FORWARD
        } catch (_: Throwable) {
            broken.add("Intake Servo")
        }


        try {
            launchServo = hwMap.get(Servo::class.java, "launchServo")
            launchServo?.direction = Servo.Direction.REVERSE
            try {
                relatch()
            } catch (e: Throwable) {
                telemetry?.addData("Could not relatch launch servo because ", e)
            }
        } catch (_: Throwable) {
            broken.add("Launch Servo")
        }
        if (!auto) {
            try {
                hook = hwMap.get(DcMotor::class.java, "hook")
                hook!!.direction = Direction.FORWARD
            } catch (_: Throwable) {
                broken.add("Hook")
            }
            try {
                imu = hwMap.get(IMU::class.java, "imu")
                imu?.initialize(
                    IMU.Parameters(
                        RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                        )
                    )
                )
            } catch (_: Throwable) {
                broken.add("IMU")
            }
        }

        if (broken.isNotEmpty() && telemetry != null) {
            telemetry.addData(
                "The following could not be accessed", broken.joinToString() + "."
            )
        }
    }

    fun changeToPos() {
        for (wheel in driveBase) {
            wheel?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            wheel?.targetPosition = 0
            wheel?.power = 0.5
            wheel?.mode = DcMotor.RunMode.RUN_TO_POSITION
        }
        driveBase[0]?.direction = Direction.REVERSE
        driveBase[1]?.direction = Direction.FORWARD
        driveBase[2]?.direction = Direction.REVERSE
        driveBase[3]?.direction = Direction.FORWARD
    }

    fun changeToPow() {
        for (wheel in driveBase) wheel?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun posRun(target: Int) {
        try {
            for (i in driveBase.indices) driveBase[i]!!.targetPosition = target
        } catch (_: Throwable) {
        }
    }

    fun posRunSide(target: Int) {
        driveBase[0]!!.targetPosition += target
        driveBase[1]!!.targetPosition -= target
        driveBase[2]!!.targetPosition -= target
        driveBase[3]!!.targetPosition += target
    }

    fun getWheelPos(index: Int): Int = driveBase[index]!!.targetPosition

    fun getHeading(unit: AngleUnit): Double = imu!!.robotYawPitchRollAngles.getYaw(unit)
    fun getNormalizedDegrees(): Double =
        AngleUnit.normalizeDegrees(getHeading(AngleUnit.DEGREES))

    fun resetIMU() = imu!!.resetYaw()
    fun resetWheels() {
        val bool = driveBase[0]!!.mode == DcMotor.RunMode.RUN_WITHOUT_ENCODER
        for (i in driveBase.indices) {
            driveBase[i]!!.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            if (bool) driveBase[i]!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            else driveBase[i]!!.mode = DcMotor.RunMode.RUN_TO_POSITION
        }
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

    fun setSlideTar(pos: Int) {
        slideMotor?.targetPosition = pos
    }

    fun getSlidePos() = slideMotor?.currentPosition
    fun setClaw(open: Boolean) {
        if (open) {
            dropper?.position = this.open
        } else {
            dropper?.position = close
        }
    }

    fun setIntake(speed: Double) {
        intakeMotor?.power = speed
        intakeServo?.power = speed
    }

    fun launch() {
        launchServo?.position = 1.0
    }

    fun relatch() {
        launchServo?.position = 0.0
    }

    fun theHookBringsYouBack(pow: Double) {
        hook?.power = pow
    }
}
