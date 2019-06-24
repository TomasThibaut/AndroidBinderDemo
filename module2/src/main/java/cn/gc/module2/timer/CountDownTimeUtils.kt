package cn.gc.module2.timer

import android.util.Log
import java.math.BigDecimal

/**
 * Created by 宫成 on 2019/5/29 下午2:13.
 */
object CountDownTimeUtils {
    const val TYPE_MM_SS = "mm:ss"
    const val TYPE_HH_MM_SS = "hh_mm:ss"
    const val TYPE_DD_HH_MM_SS = "dd_mm:ss"

    /**
     * 把毫秒换成对应的 格式
     */
    fun getTimeTextFromSec(rawSec: Int, timeType: String): String {
        Log.i("G_C", "onTick rawSec: $rawSec")
        Log.i("G_C", "onTick realTime: $rawSec")
        val secTotal: Int = (rawSec)
        val minTotal = secTotal / 60
        val hourTotal = minTotal / 60
        val dayTotal = hourTotal / 60
        val sec = secTotal % 60
        val min = minTotal % 60
        val hour = hourTotal % 24

        return when (timeType) {
            TYPE_MM_SS -> {
                "${zeroHead(minTotal)}:${zeroHead(sec)}"
            }
            TYPE_HH_MM_SS -> {
                "${zeroHead(hourTotal)}:${zeroHead(min)}:${zeroHead(sec)}"
            }
            TYPE_DD_HH_MM_SS -> {
                "${zeroHead(dayTotal)}:${zeroHead(hour)}:${zeroHead(min)}:${zeroHead(sec)}"
            }
            else -> {
                ""
            }
        }
    }

    /**
     * 个位前面补个zero!
     */
    private fun zeroHead(value: Int): String = if (value < 10) "0$value" else "$value"
}