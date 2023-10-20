package org.firstinspires.ftc.teamcode

data class PIDcoefficients(
    internal val Kp: Double,
    internal val Ki: Double,
    internal val Kd: Double,
)

class PIDcontroller(
    private val coeffs: PIDcoefficients,
    private var posGet: (() -> Number)? = null,
    private var exFun: ((Number) -> Unit)? = null,
    private var timeGet: (() -> Number)? = null,
) {
    private var i: Double = 0.0
    private var maxI: Double = Double.NaN

    fun setExecute(function: ((Number) -> Unit)) {
        exFun = function
    }

    fun setTimer(timer: (() -> Number)) {
        timeGet = timer
    }

    fun setPositionGetter(posGetter: (() -> Number)) {
        posGet = posGetter
    }

    private fun getTime(): Number {
        return if (timeGet != null) {
            timeGet!!.invoke()
        } else {
            1
        }
    }

    private fun getPos(): Number {
        return if (posGet != null) {
            posGet!!.invoke()
        } else {
            1
        }
    }

    private var prevTime = 0.0
    private var prevErr = 0.0

    fun pidCalc(target: Number, currPos: Number = getPos(), time: Number = getTime()): Double {
        val currErr: Double = target.toDouble() - currPos.toDouble()
        val p = coeffs.Kp * currErr

        i += coeffs.Ki * (currErr * (time.toDouble() - prevTime))

        if (!maxI.isNaN()) {
            if (i > maxI) i = maxI
            else if (i < -maxI) i = -maxI
        }

        val d = coeffs.Kd * (currErr - prevErr) / (time.toDouble() - prevTime)
        prevErr = currErr
        prevTime = time.toDouble()

        if (exFun != null) exFun!!.invoke(p + i + d)
        return p + i + d
    }
}
