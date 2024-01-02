package autoThings

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor

class AEyes {
    private var visionPortal: VisionPortal? = null
    var tfod: TfodProcessor? = null
    var april: AprilTagProcessor? = null
    fun initVision(hwMap: HardwareMap) {
        val builder = VisionPortal.Builder()
        builder.setCamera(hwMap.get(WebcamName::class.java, "Webcam 1"))
        tfod = TfodProcessor.Builder()
            .setModelFileName("Eyes_Of_Cassidy.tflite")
            .setModelLabels(arrayOf("prop"))
            .build()
        april = AprilTagProcessor
            .Builder()
            .setDrawAxes(true)
            .setDrawCubeProjection(true)
            .setDrawTagID(true)
            .setDrawTagOutline(true)
            .build()

        builder.addProcessors(tfod, april)
        visionPortal = builder.build()
    }
}