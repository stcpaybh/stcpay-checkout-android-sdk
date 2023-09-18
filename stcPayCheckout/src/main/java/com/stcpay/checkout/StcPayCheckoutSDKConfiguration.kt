package com.stcpay.checkout

import androidx.activity.ComponentActivity
import com.stcpay.checkout.utils.AMOUNT_EXCEPTION
import com.stcpay.checkout.utils.EXTERNAL_REFERENCE_ID_EXCEPTION
import com.stcpay.checkout.utils.MERCHANT_ID_EXCEPTION
import com.stcpay.checkout.utils.SECRET_KEY_EXCEPTION

class StcPayCheckoutSDKConfiguration private constructor(
    val context: ComponentActivity,
    val secretKey: String,
    val merchantId: String,
    val externalRefId: String,
    val amount: Double,
    val stcPayCheckoutResultListener: StcPayCheckoutResultListener?
) {
    init {
        if (secretKey.isBlank()) {
            throw IllegalArgumentException(SECRET_KEY_EXCEPTION)
        }

        if (merchantId.isBlank()) {
            throw IllegalArgumentException(MERCHANT_ID_EXCEPTION)
        }

        if (externalRefId.isBlank()) {
            throw IllegalArgumentException(EXTERNAL_REFERENCE_ID_EXCEPTION)
        }

        if (amount == 0.0) {
            throw IllegalArgumentException(AMOUNT_EXCEPTION)
        }
    }

    class Builder(
        private var context: ComponentActivity,
        private var secretKey: String,
        private var merchantId: String,
        private var externalRefId: String,
        private var amount: Double,
        private var stcPayCheckoutResultListener: StcPayCheckoutResultListener?
    ) {
        constructor(context: ComponentActivity) : this(
            context, "", "", "", 0.0, null
        )

        fun secretKey(secretKey: String) = apply { this.secretKey = secretKey }

        fun merchantId(merchantId: String) = apply { this.merchantId = merchantId }

        fun externalRefId(externalRefId: String) = apply { this.externalRefId = externalRefId }

        fun amount(amount: Double) = apply { this.amount = amount }
        fun stcPayCheckoutResultListener(stcPayCheckoutResultListener: StcPayCheckoutResultListener) =
            apply { this.stcPayCheckoutResultListener = stcPayCheckoutResultListener }

        fun build(): StcPayCheckoutSDKConfiguration {
            return StcPayCheckoutSDKConfiguration(
                context,
                secretKey,
                merchantId,
                externalRefId,
                amount,
                stcPayCheckoutResultListener
            )
        }
    }
}