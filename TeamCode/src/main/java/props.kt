import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU

class props (hwMap: HardwareMap) {

    private val driveBase: Array<DcMotorEx?> = arrayOfNulls(4)
    private var imu : IMU? = null

    init {
        for (i in driveBase.indices) {
            driveBase[i] = hwMap.get(DcMotorEx::class.java, arrayOf("rf", "rb", "lf", "lb")[i])
            driveBase[i]?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        imu = hwMap.get(IMU::class.java, "imu")
        imu?.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        ))

    }
}