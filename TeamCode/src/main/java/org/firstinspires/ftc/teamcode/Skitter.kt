package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU

@TeleOp
class Skitter : OpMode() {
    private val driveBase = arrayOfNulls<DcMotorImplEx>(4)
    private var imu : IMU? = null

    override fun init() {
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

        imu = hardwareMap.get(IMU::class.java, "imu")

    }

    override fun loop() {

    }
}