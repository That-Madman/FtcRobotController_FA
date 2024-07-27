package org.firstinspires.ftc.teamcode

import autoThings.AEyes
import autoThings.PID
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin

class Board {
    private val open: Double = 1.0
    private val close: Double = 0.0

    private val driveBase: Array<DcMotorImplEx?> = arrayOfNulls<DcMotorImplEx?>(4)
    private var slideMotor: DcMotorImplEx? = null
    private var dropper: Servo? = null

    private var intakeServo: CRServo? = null
    private var intakeMotor: DcMotorImplEx? = null

    private var hook1: DcMotor? = null
    private var hook2: DcMotor? = null

    private var imu: IMU? = null

    private var distSense: DistanceSensor? = null

    private var launchServo: Servo? = null

    private val hookLifts: Array<Servo?> = arrayOfNulls(2)
    private val bumpers: Array<TouchSensor?> = arrayOfNulls(2)

    var eyes: AEyes? = null

    private var intakeLiftServo: CRServo? = null

    private val intakeLiftPID = PID(0.001,
        0.0,
        0.0,
        { hook2?.currentPosition as Number },
        { intakeLiftServo?.power = it.toDouble() })

    @JvmOverloads
    fun getHW(hwMap: HardwareMap, telemetry: Telemetry? = null, auto: Boolean = false) {
        val broken = ArrayList<String>(0)

        try {
            eyes = AEyes(hwMap)
        } catch (_: Throwable) {
            broken.add("Camera")
        }

        try {
            slideMotor = hwMap.get(DcMotorImplEx::class.java, "slideMotor")
            slideMotor?.direction = Direction.FORWARD
            slideMotor?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
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
            intakeLiftServo = hwMap.get(CRServo::class.java, "intakeServoLift")
            intakeLiftServo!!.direction = Direction.REVERSE
        } catch (_: Throwable) {
            broken.add("Intake Servo Lift")
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
                hook1 = hwMap.get(DcMotor::class.java, "hook")
                hook1!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                hook1!!.direction = Direction.FORWARD
                hook1!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            } catch (_: Throwable) {
                broken.add("Hook 1")
            }
        }

        try {
            hook2 = hwMap.get(DcMotor::class.java, "hook2")
            hook2!!.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            hook2!!.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            hook2!!.direction = Direction.FORWARD
            hook2!!.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        } catch (_: Throwable) {
            broken.add("Hook 2")
        }

        try {
            hookLifts[0] = hwMap.get(Servo::class.java, "hookServo1")
            hookLifts[1] = hwMap.get(Servo::class.java, "hookServo2")

            hookLifts[0]!!.direction = Servo.Direction.REVERSE
        } catch (_: Throwable) {
            broken.add("Hook Servos")
        }

        try {
            bumpers[0] = hwMap.get(TouchSensor::class.java, "hookBumper1")
            bumpers[1] = hwMap.get(TouchSensor::class.java, "hookBumper2")
        } catch (_: Throwable) {
            broken.add("Hook Bumper")
        }

        if (!auto) {
            try {
                imu = hwMap.get(IMU::class.java, "imu")
                imu?.initialize(
                    IMU.Parameters(
                        RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                            RevHubOrientationOnRobot.UsbFacingDirection.UP
                        )
                    )
                )
            } catch (_: Throwable) {
                broken.add("IMU")
            }
        }

        try {
            distSense = hwMap.get(DistanceSensor::class.java, "distSense")
        } catch (_: Throwable) {
            broken.add("Distance Sensor")
        }

        if (broken.isNotEmpty() && telemetry != null) {
            telemetry.addData(
                "The following could not be accessed",
                broken.sortedDescending().joinToString() + "."
            )
        }
    }

    fun getHeading(unit: AngleUnit): Double = imu!!.robotYawPitchRollAngles.getYaw(unit)

    fun resetIMU() = imu!!.resetYaw()

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
        if (slideMotor?.mode != DcMotor.RunMode.RUN_TO_POSITION)
            slideMotor?.mode = DcMotor.RunMode.RUN_TO_POSITION
        slideMotor?.targetPosition = pos
    }

    fun setSlidePow(pow: Double) {
        if (slideMotor?.mode != DcMotor.RunMode.RUN_WITHOUT_ENCODER)
            slideMotor?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        slideMotor?.power = pow
    }

    fun getSlidePos() = slideMotor?.currentPosition

    fun setClaw(open: Boolean) {
        if (open) {
            dropper?.position = this.open
        } else {
            dropper?.position = close
        }
    }

    fun setDrop(state: Int) {
        when ((state % 3)) {
            0 -> dropper?.position = 1.0
            1 -> dropper?.position = 0.5
            2 -> dropper?.position = 0.0
        }
    }

    fun setIntake(speed: Double) {
        intakeMotor?.power = speed
        intakeServo?.power = speed
    }

    fun setIntakeHeight(pos: Int) {
        intakeLiftPID.pidCalc(pos)
    }

    fun launch() {
        launchServo?.position = 1.0
    }

    fun relatch() {
        launchServo?.position = 0.0
    }

    fun theHookBringsYouBack(pow: Double) {
        hook1?.power = pow
        hook2?.power = pow
    }

    fun bumpers(): Boolean = bumpers[0]!!.isPressed || bumpers[1]!!.isPressed

    fun hookServo(pos: Double) {
        hookLifts[0]?.position = pos
        hookLifts[1]?.position = pos
    }

    fun getDist(unit: DistanceUnit): Double = distSense!!.getDistance(unit)
}
