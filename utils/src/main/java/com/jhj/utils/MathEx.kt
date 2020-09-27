package com.jhj.utils

import java.math.BigDecimal

/**
 * 保留两位小数
 */
fun Number.decimalDigits(decimalsNum: Int = 2): String {
    val bigDecimal = BigDecimal(this.toString())
    return bigDecimal.setScale(decimalsNum, BigDecimal.ROUND_HALF_UP).toString()
}

/**
 * 去除小数后面多余的0
 */
fun Number.removeDecimalPoint(decimalsNum: Int = 2): String {
    var str = decimalDigits(decimalsNum)
    if (str.contains(".")) {
        str = str.replace("0+?$".toRegex(), "")
        str = str.replace("[.]$".toRegex(), "")
    }
    return str
}

/**
 * 距离转换
 */
fun Float.formatDistance(): String {
    return if (this > 1000) {
        "${(this / 1000).decimalDigits()}km"
    } else {
        "${this.decimalDigits()}m"
    }
}


/**
 * 处理数据为null 时不方便计算问题
 */
val Byte?.notNull: Byte
    get() = this ?: 0
val Short?.notNull: Short
    get() = this ?: 0
val Int?.notNull: Int
    get() = this ?: 0
val Long?.notNull: Long
    get() = this ?: 0
val Double?.notNull: Double
    get() = this ?: 0.toDouble()
val Float?.notNull: Float
    get() = this ?: 0.toFloat()






