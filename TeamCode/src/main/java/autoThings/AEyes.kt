package autoThings

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor

class AEyes {
    var visionPortal: VisionPortal? = null
    fun initVision(hwMap: HardwareMap) {
        val builder = VisionPortal.Builder()
        builder.setCamera(hwMap.get(WebcamName::class.java, "Webcam 1"))
        builder.addProcessors(
            TfodProcessor.Builder().build(), AprilTagProcessor.easyCreateWithDefaults()
        )
        visionPortal = builder.build()
    }
}