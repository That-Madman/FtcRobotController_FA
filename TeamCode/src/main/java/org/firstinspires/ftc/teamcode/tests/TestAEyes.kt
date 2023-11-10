package org.firstinspires.ftc.teamcode.tests

import autoThings.AEyes
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection


@Autonomous(group = "Tests")
class TestAEyes : OpMode() {

    private val eyes = AEyes()

    override fun init() {
        try {
            eyes.initVision(hardwareMap)
        } catch (e: Exception) {
            telemetry.addData("Could not access hardware because ", e)
        } catch (e: Error) {
            telemetry.addData("Could not access hardware because ", e)
        }
    }

    override fun init_loop() {
        try { //start of TensorFlow
            val view: List<Recognition>? = eyes.tfod!!.recognitions
            view!!.iterator().forEach {
                telemetry.addLine("found: $it")
            }
        } catch (e: Exception) {
            telemetry.addData("Error in using camera because:", e)
        } catch (e: Error) {
            telemetry.addData("Error in using camera because:", e)
        }  //End of TensorFlow

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
        } catch (e: Exception) {
            telemetry.addData("Issue with April Tags because ", e)
        } catch (e: Error) {
            telemetry.addData("Issue with April Tags because ", e)
        }//end of April Tags
    }

    override fun loop() {
        try { //start of TensorFlow
            val view: List<Recognition>? = eyes.tfod!!.recognitions
            view!!.iterator().forEach {
                telemetry.addLine("found: $it")
            }
        } catch (e: java.lang.Exception) {
            telemetry.addData("Error in using camera because:", e)
        } catch (e: java.lang.Error) {
            telemetry.addData("Error in using camera because:", e)
        }  //End of TensorFlow

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
        } catch (e: Exception) {
            telemetry.addData("Issue with April Tags because ", e)
        } catch (e: Error) {
            telemetry.addData("Issue with April Tags because ", e)
        }//end of April Tags
    }
}
