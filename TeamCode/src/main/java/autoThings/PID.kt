package autoThings

class PID @JvmOverloads constructor(
    private val Kp: Double,
    private val Ki: Double,
    private val Kd: Double,
    private var posGet: (() -> Number)? = null,
    private var exFun: ((Number) -> Unit)? = null,
    private var timeGet: (() -> Number)? = {System.nanoTime() / 1e9},
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
            0
        }
    }

    private fun getPos(): Number {
        return if (posGet != null) {
            posGet!!.invoke()
        } else {
            0
        }
    }

    private var prevTime = 0.0
    private var prevErr = 0.0

    fun resetI() {
        i = 0.0
    }

    @JvmOverloads
    fun pidCalc(target: Number, currPos: Number = getPos(), time: Number = getTime()): Double {
        val currErr: Double = target.toDouble() - currPos.toDouble()
        val p = Kp * currErr

        i += Ki * (currErr * (time.toDouble() - prevTime))

        if (i > maxI) i = maxI
        else if (i < -maxI) i = -maxI

        val d = Kd * (currErr - prevErr) / (time.toDouble() - prevTime)
        prevErr = currErr
        prevTime = time.toDouble()

        if (exFun != null) exFun!!.invoke(p + i + d)
        return p + i + d
    }
}
