package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.CM
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.METER
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM

@Autonomous(group = "Tests")
class TestDistSense : OpMode() {
    private var distSense: DistanceSensor? = null
    override fun init() {
        distSense = hardwareMap.get(DistanceSensor::class.java, "distSense")
    }

    override fun loop() {
        telemetry.addData("Distance is in inches", "  ${distSense!!.getDistance(INCH)}")
        telemetry.addData("Distance is in millimeters", "  ${distSense!!.getDistance(MM)}")
        telemetry.addData("Distance is in centimeters", "  ${distSense!!.getDistance(CM)}")
        telemetry.addData("Distance is in meters", "  ${distSense!!.getDistance(METER)}")
    }
}