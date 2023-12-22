package org.firstinspires.ftc.teamcode

import autoThings.AEyes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode

@Autonomous
class TestEyes : OpMode() {

    private val eyes = AEyes()
    override fun init() {
        try {
            eyes.initVision(hardwareMap)
        } catch (e: Throwable) {
            throw Exception("Some issue with camera because of $e.\nGet that fixed.")
        }
    }

    override fun init_loop() {
        try { //start of TensorFlow
            eyes.tfod!!.recognitions.forEach { telemetry.addLine("found ${it.label}") }
        } catch (e: Throwable) {
            telemetry.addData("Error in Tensorflow because: ", e)
        } //end of tensorFlow
        try { //start of April tags
            eyes.april!!.detections.forEach {
                //use aprilTagDetection class to find april tags/get data
                telemetry.addLine("x of tag ${it.id} is ${it.ftcPose.x}")
                telemetry.addLine("y of tag ${it.id} is ${it.ftcPose.y}")
                telemetry.addLine("z of tag ${it.id} is ${it.ftcPose.z}")
                telemetry.addLine("roll of tag ${it.id} is ${it.ftcPose.roll}")
                telemetry.addLine("pitch of ${it.id} is ${it.ftcPose.pitch}")
                telemetry.addLine("yaw of ${it.id} is ${it.ftcPose.yaw}")
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because: ", e)
        } // end of April Tags
        telemetry.update()
    }

    override fun loop() {
        try { //start of TensorFlow
            eyes.tfod!!.recognitions.forEach { telemetry.addLine("found ${it.label}") }
        } catch (e: Throwable) {
            telemetry.addData("Error in Tensorflow because: ", e)
        } //end of tensorFlow
        try { //start of April tags
            eyes.april!!.detections.forEach {
                //use aprilTagDetection class to find april tags/get data
                telemetry.addLine("x of tag ${it.id} is ${it.ftcPose.x}")
                telemetry.addLine("y of tag ${it.id} is ${it.ftcPose.y}")
                telemetry.addLine("z of tag ${it.id} is ${it.ftcPose.z}")
                telemetry.addLine("roll of tag ${it.id} is ${it.ftcPose.roll}")
                telemetry.addLine("pitch of ${it.id} is ${it.ftcPose.pitch}")
                telemetry.addLine("yaw of ${it.id} is ${it.ftcPose.yaw}")
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because: ", e)
        } // end of April Tags
    }
}