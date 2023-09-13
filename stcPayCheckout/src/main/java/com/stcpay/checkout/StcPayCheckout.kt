package com.stcpay.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.stcpay.checkout.utils.ACTIVITY_RESULT_LAUNCHER_EXCEPTION
import com.stcpay.checkout.utils.AMOUNT_EXCEPTION
import com.stcpay.checkout.utils.EXTERNAL_REFERENCE_ID_EXCEPTION
import com.stcpay.checkout.utils.HASHED_DATA
import com.stcpay.checkout.utils.MERCHANT_ID_EXCEPTION
import com.stcpay.checkout.utils.SECRET_KEY_EXCEPTION
import com.stcpay.checkout.utils.STC_PAY_APP_PACKAGE_NAME
import com.stcpay.checkout.utils.getFormattedPrice
import com.stcpay.checkout.utils.getHashedData

class StcPayCheckout private constructor(
    private val secretKey: String,
    private val merchantId: String,
    private val externalRefId: String,
    private val amount: Double,
    private val activityResultLauncher: ActivityResultLauncher<Intent>?,
    private val stcPayCheckoutResultListener: StcPayCheckoutResultListener?
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

        if (activityResultLauncher == null) {
            throw IllegalArgumentException(ACTIVITY_RESULT_LAUNCHER_EXCEPTION)
        }
    }

    class Builder {
        private var secretKey: String = ""
        private var merchantId: String = ""
        private var externalRefId: String = ""
        private var amount: Double = 0.0
        private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
        private var stcPayCheckoutResultListener: StcPayCheckoutResultListener? = null

        fun setSecretKey(secretKey: String) = apply {
            this.secretKey = secretKey
        }

        fun setMerchantId(merchantId: String) = apply {
            this.merchantId = merchantId
        }

        fun setExternalRefId(externalRefId: String) = apply {
            this.externalRefId = externalRefId
        }

        fun setAmount(amount: Double) = apply {
            this.amount = amount
        }

        fun setActivityResultLauncher(activityResultLauncher: ActivityResultLauncher<Intent>) = apply {
            this.activityResultLauncher = activityResultLauncher
        }

        fun setStcPayCheckoutResultListener(stcPayCheckoutResultListener: StcPayCheckoutResultListener) = apply {
            this.stcPayCheckoutResultListener = stcPayCheckoutResultListener
        }

        fun build(): StcPayCheckout {
            return StcPayCheckout(
                secretKey,
                merchantId,
                externalRefId,
                amount,
                activityResultLauncher,
                stcPayCheckoutResultListener
            )
        }
    }

    fun openStcPayApp(context: Context) {
        val hashedData = getHashedData(
            secretKey,"${merchantId}-${amount.getFormattedPrice()}-${externalRefId}"
        )

        val packageManager = context.packageManager

        val isStcPayInstalled = try {
            packageManager.getPackageInfo(STC_PAY_APP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

        if (isStcPayInstalled) {
            val intent = packageManager.getLaunchIntentForPackage(STC_PAY_APP_PACKAGE_NAME)

            if (intent != null) {
                intent.putExtra(HASHED_DATA, hashedData)
                activityResultLauncher?.launch(intent)
            }
        } else {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$STC_PAY_APP_PACKAGE_NAME")
                )
                context.startActivity(intent)
            } catch (e: android.content.ActivityNotFoundException) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$STC_PAY_APP_PACKAGE_NAME")
                )
                context.startActivity(intent)
            }
        }
    }
}