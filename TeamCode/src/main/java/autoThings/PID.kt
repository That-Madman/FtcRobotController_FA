package autoThings

/**
 * A PID class by FTC Team 2173, Till the Wheels Fall Off
 * @param kP the P value
 * @param kI the I value
 * @param kD the D value
 * @param posGet the function to get the current value of what is being influenced
 * @param exFun the function executed when pidCalc is called
 * @param timeGet the function to get the time
 * @property pidCalc calculates the PID value and executes exFun if given
 * @property resetI resets the I value that gets accrued as the program runs
 * @author Alex Bryan
 */
@Suppress("unused")
class PID @JvmOverloads constructor(
    private val kP: Double,
    private val kI: Double,
    private val kD: Double,
    private var posGet: (() -> Number)? = null,
    private var exFun: ((Number) -> Unit)? = null,
    private var timeGet: (() -> Number)? = { System.nanoTime() / 1e9 },
) {
    /**
     * In PID, the I value is a value that gets aggregated while the formula goes on. It increases
     * based off the difference between the current and target position, multiplied by [kI]. It is
     * used to address a constant error, slowly increasing or decreasing to help sustain the target
     * position. If [kP] is 0, i will never change, which is sometimes the best option.
     */
    private var i: Double = 0.0
    private var maxI: Double = Double.NaN

    /**
     * Calculates the output for PID based off of the P, I, and D values.
     * If [exFun] was set, the function also executes it.
     * @param target The target value
     * @param currPos The current value. If it is not set, the function will instead use [posGet]
     * @param time The current time. If it is not set, the function will instead use [timeGet]
     * @author Alex Bryan
     */
    @JvmOverloads
    fun pidCalc(
        target: Number, currPos: Number = getPos(), time: Number = getTime()
    ): Double {
        val currErr: Double = target.toDouble() - currPos.toDouble()
        val p = kP * currErr

        i += kI * (currErr * (time.toDouble() - prevTime))

        if (i > maxI) i = maxI
        else if (i < -maxI) i = -maxI

        val d = kD * (currErr - prevErr) / (time.toDouble() - prevTime)
        prevErr = currErr
        prevTime = time.toDouble()

        if (exFun != null) exFun!!.invoke(p + i + d)
        return p + i + d
    }

    /**
     * Resets the [i] value back to 0
     * @author Alex Bryan
     */
    fun resetI() {
        i = 0.0
    }

    fun setExecute(function: ((Number) -> Unit)) {
        exFun = function
    }

    fun setTimer(timer: (() -> Number)) {
        timeGet = timer
    }

    fun setPositionGetter(posGetter: (() -> Number)) {
        posGet = posGetter
    }

    private fun getTime(): Number = if (timeGet != null) timeGet!!.invoke() else 0

    private fun getPos(): Number = if (posGet != null) posGet!!.invoke() else 0

    private var prevTime = 0.0
    private var prevErr = 0.0
}
