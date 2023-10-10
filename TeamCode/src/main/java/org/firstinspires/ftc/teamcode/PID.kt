package org.firstinspires.ftc.teamcode

class PID constructor(
    private val Kp: Double,
    private val Ki: Double,
    private val Kd: Double,
) {
    private var i: Double = 0.0

    var maxI: Double = Double.NaN

    private var prevTime = 0.0
    private var prevErr = 0

    fun pidCalc(currPos: Int, target: Int, time: Double): Double {
        val currErr = target - currPos
        val p = Kp * currErr

        i += Ki * (currErr * (time - prevTime))

        if (!maxI.isNaN()) {
            if (i > maxI) i = maxI
            else if (i < -maxI) i = -maxI
        }

        val d = Kd * (currErr - prevErr) / (time - prevTime)
        prevErr = currErr
        prevTime = time

        return p + i + d
    }
}