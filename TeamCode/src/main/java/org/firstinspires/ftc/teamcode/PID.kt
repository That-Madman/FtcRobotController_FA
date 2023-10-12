package org.firstinspires.ftc.teamcode

class PID(
    private val Kp: Double,
    private val Ki: Double,
    private val Kd: Double,
    var maxI: Double = Double.NaN
) {
    private var i: Double = 0.0

    private var exFun: ((Number) -> Unit)? = null
        set(value) {
            field = value
        }

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

        if (exFun != null) exFun?.invoke(p + i + d)
        return p + i + d
    }
}