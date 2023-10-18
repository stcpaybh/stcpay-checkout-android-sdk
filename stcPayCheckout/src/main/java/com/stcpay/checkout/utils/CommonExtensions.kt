package com.stcpay.checkout.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Double?.getFormattedPrice(): String {
    val svSE = DecimalFormat("#,###.000")
    val symbols = DecimalFormatSymbols(Locale.US)
    svSE.roundingMode = RoundingMode.HALF_UP
    symbols.decimalSeparator = '.'
    svSE.isGroupingUsed = false
    svSE.decimalFormatSymbols = symbols
    var formatted = "0.000"
    return if (this != null) {
        val bigDecimal = BigDecimal.valueOf(this)
        formatted = svSE.format(bigDecimal)
        if (formatted.indexOf('.') == 0) {
            formatted = "0$formatted"
        }
        formatted
    } else {
        formatted
    }
}

fun String?.getFormattedPrice(): String {
    var mPrice = this
    if (mPrice.isNullOrEmpty()) {
        mPrice = "0.000"
    }
    if (mPrice.removeWhitespaces().endsWith(".")) {
        mPrice = "${mPrice}0"
    }
    return mPrice.toDoubleOrNull()?.getFormattedPrice() ?: "0.000"
}

fun String.removeWhitespaces() = filter { !it.isWhitespace() }