package com.stcpay.checkout

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.stcpay.checkout.utils.AMOUNT_EXCEPTION
import com.stcpay.checkout.utils.CODE
import com.stcpay.checkout.utils.EXTERNAL_REFERENCE_ID_EXCEPTION
import com.stcpay.checkout.utils.MERCHANT_ID_EXCEPTION
import com.stcpay.checkout.utils.MESSAGE
import com.stcpay.checkout.utils.SECRET_KEY_EXCEPTION
import com.stcpay.checkout.utils.SUCCESS_CODE
import com.stcpay.checkout.utils.TRANSACTION_ID

class StcPayCheckoutSDKConfiguration private constructor(
    val context: ComponentActivity,
    val secretKey: String,
    val merchantId: String,
    val listener: ActivityResultLauncher<Intent>?
) {

    var externalRefId: String? = null
    var amount: Double? = null

    init {
        if (secretKey.isBlank()) {
            throw IllegalArgumentException(SECRET_KEY_EXCEPTION)
        }

        if (merchantId.isBlank()) {
            throw IllegalArgumentException(MERCHANT_ID_EXCEPTION)
        }
    }

    fun validate() {
        if (externalRefId == null || externalRefId?.isBlank() == true) {
            throw IllegalArgumentException(EXTERNAL_REFERENCE_ID_EXCEPTION)
        }

        if (amount == null || amount == 0.0) {
            throw IllegalArgumentException(AMOUNT_EXCEPTION)
        }
    }


    class Builder(
        private var context: ComponentActivity,
        private var secretKey: String,
        private var merchantId: String,
        private var stcPayCheckoutResultListener: StcPayCheckoutResultListener?
    ) {
        constructor(context: ComponentActivity) : this(
            context, "", "", null
        )

        fun secretKey(secretKey: String) = apply { this.secretKey = secretKey }

        fun merchantId(merchantId: String) = apply { this.merchantId = merchantId }

        fun stcPayCheckoutResultListener(stcPayCheckoutResultListener: StcPayCheckoutResultListener) =
            apply { this.stcPayCheckoutResultListener = stcPayCheckoutResultListener }

        fun build(): StcPayCheckoutSDKConfiguration {

            val listener = context.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val resultCode = data?.getIntExtra(CODE, -5) ?: -5
                    if (resultCode == SUCCESS_CODE) {
                        data?.getLongExtra(TRANSACTION_ID, -5)?.let {
                            stcPayCheckoutResultListener?.onSuccess(it)
                        }
                    } else {
                        stcPayCheckoutResultListener?.onFailure(
                            resultCode, data?.getStringExtra(MESSAGE) ?: ""
                        )
                    }
                } else {
                    stcPayCheckoutResultListener?.onFailure(-5, "")
                }
            }

            return StcPayCheckoutSDKConfiguration(context, secretKey, merchantId, listener)
        }
    }
}