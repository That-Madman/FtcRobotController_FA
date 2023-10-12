package org.firstinspires.ftc.teamcode

class PID(
    private val Kp: Double,
    private val Ki: Double,
    private val Kd: Double,
) {
    private var i: Double = 0.0
    private var maxI: Double = Double.NaN

    private var exFun: ((Number) -> Unit)? = null
    private var timeGet: (() -> Number)? = null
    private var posGet: (() -> Number)? = null

    private fun getTime(): Number {
        return if (timeGet != null) timeGet!!.invoke()
        else 1
    }

    private fun getPos(): Number {
        return if (posGet != null) posGet!!.invoke()
        else 1
    }

    private var prevTime = 0.0
    private var prevErr = 0.0

    fun pidCal(target: Number, currPos: Number = getPos(), time: Number = getTime()): Double {
        val currErr: Double = target.toDouble() - currPos.toDouble()
        val p = Kp * currErr

        i += Ki * (currErr * (time.toDouble() - prevTime))

        if (!maxI.isNaN()) {
            if (i > maxI) i = maxI
            else if (i < -maxI) i = -maxI
        }

        val d = Kd * (currErr - prevErr) / (time.toDouble() - prevTime)
        prevErr = currErr
        prevTime = time.toDouble()

        if (exFun != null) exFun!!.invoke(p + i + d)
        return p + i + d
    }
}
