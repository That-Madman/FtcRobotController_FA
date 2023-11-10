package org.firstinspires.ftc.teamcode.tests

import autoThings.AEyes
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection

@TeleOp(group = "Tests")
class TestAEyes : OpMode() {

    private val eyes = AEyes()

    override fun init() {
        try {
            eyes.initVision(hardwareMap)
        } catch (e: Throwable) {
            telemetry.addData("Could not access hardware because ", e)
        }
    }

    override fun init_loop() {
        try { //start of TensorFlow
            val view: List<Recognition>? = eyes.tfod!!.recognitions
            view!!.iterator().forEach {
                telemetry.addLine("found: $it")
            }
        } catch (e: Throwable) {
            telemetry.addData("Error in using camera because:", e)
        } //End of TensorFlow

        try { //April tags
            if (eyes.april!!.detections.size > 0) {
                val tag: AprilTagDetection = eyes.april!!.detections[0]
                telemetry.addData("x", tag.ftcPose.x)
                telemetry.addData("y", tag.ftcPose.y)
                telemetry.addData("z", tag.ftcPose.z)
                telemetry.addData("roll", tag.ftcPose.roll)
                telemetry.addData("pitch", tag.ftcPose.pitch)
                telemetry.addData("yaw", tag.ftcPose.yaw)
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because ", e)
        } //end of April Tags
    }

    override fun loop() {
        try { //start of TensorFlow
            val view: List<Recognition>? = eyes.tfod!!.recognitions
            view!!.iterator().forEach {
                telemetry.addLine("found: $it")
            }
        } catch (e: Throwable) {
            telemetry.addData("Error in using camera because:", e)
        } //End of TensorFlow

        try { //April tags
            if (eyes.april!!.detections.size > 0) {
                val tag: AprilTagDetection = eyes.april!!.detections[0]
                telemetry.addData("x", tag.ftcPose.x)
                telemetry.addData("y", tag.ftcPose.y)
                telemetry.addData("z", tag.ftcPose.z)
                telemetry.addData("roll", tag.ftcPose.roll)
                telemetry.addData("pitch", tag.ftcPose.pitch)
                telemetry.addData("yaw", tag.ftcPose.yaw)
            }
        } catch (e: Throwable) {
            telemetry.addData("Issue with April Tags because ", e)
        } //end of April Tags
    }
}
