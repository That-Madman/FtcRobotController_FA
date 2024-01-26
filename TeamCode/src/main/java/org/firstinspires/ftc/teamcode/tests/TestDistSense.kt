package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH

@Autonomous(group = "Tests")
class TestDistSense : OpMode() {
    private var distSense : DistanceSensor? = null
    override fun init() {
        distSense = hardwareMap.get(DistanceSensor::class.java, "distSense")
    }

    override fun loop() {
        telemetry.addData("Distance is", "  ${distSense!!.getDistance(INCH)}")
    }
}