package com.stcpay.checkout.utils

import android.util.Base64
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun getHashedData(secretKey: String, data: String): String {
    try {
        val sha512HMAC = Mac.getInstance(HMAC_SHA_512)
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), HMAC_SHA_512)
        sha512HMAC.init(secretKeySpec)
        val bytes = sha512HMAC.doFinal(data.toByteArray())
        return Base64.encodeToString(bytes, 2)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: InvalidKeyException) {
        e.printStackTrace()
    }

    return ""
}