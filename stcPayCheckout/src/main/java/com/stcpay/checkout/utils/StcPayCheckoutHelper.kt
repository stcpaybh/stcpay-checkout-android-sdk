package com.stcpay.checkout.utils

import android.os.Build
import android.util.Base64
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

fun isHuaweiDevice() = Build.MANUFACTURER.lowercase(Locale.US) == "huawei"

fun getHashedData(secretKey: String, data: String): String {
    try {
        val sha512HMAC = Mac.getInstance(HMAC_SHA_512)
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(UTF_8), HMAC_SHA_512)
        sha512HMAC.init(secretKeySpec)
        val bytes = sha512HMAC.doFinal(data.toByteArray(UTF_8))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    }

    return ""
}

object StcPayCheckoutSDKNotInitializedException : Exception(STC_PAY_CHECKOUT_SDK_EXCEPTION) {
    private fun readResolve(): Any = StcPayCheckoutSDKNotInitializedException
}

object ActivityResultLauncherNotInitializedException :
    Exception(ACTIVITY_RESULT_LAUNCHER_EXCEPTION) {
    private fun readResolve(): Any = ActivityResultLauncherNotInitializedException
}