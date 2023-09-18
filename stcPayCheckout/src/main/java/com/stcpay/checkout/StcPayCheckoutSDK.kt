package com.stcpay.checkout

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.stcpay.checkout.utils.ActivityResultLauncherNotInitializedException
import com.stcpay.checkout.utils.HASHED_DATA
import com.stcpay.checkout.utils.STC_PAY_APP_PACKAGE_NAME
import com.stcpay.checkout.utils.StcPayCheckoutSDKNotInitializedException
import com.stcpay.checkout.utils.getFormattedPrice
import com.stcpay.checkout.utils.getHashedData

object StcPayCheckoutSDK {
    private lateinit var stcPayCheckoutSDKConfiguration: StcPayCheckoutSDKConfiguration
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    fun initialize(
        stcPayCheckoutSDKConfiguration: StcPayCheckoutSDKConfiguration
    ) {
        this.stcPayCheckoutSDKConfiguration = stcPayCheckoutSDKConfiguration

        this.stcPayCheckoutSDKConfiguration.apply {
            if (stcPayCheckoutResultListener != null) {
                activityResultLauncher =
                    stcPayCheckoutSDKConfiguration.context.registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) { result: ActivityResult ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            if (true) {
                                stcPayCheckoutResultListener.onSuccess()
                            } else {
                                stcPayCheckoutResultListener.onFailure()
                            }
                        }
                    }
            }
        }
    }

    fun getStcPayCheckoutSDKConfiguration(): StcPayCheckoutSDKConfiguration {
        return if (this::stcPayCheckoutSDKConfiguration.isInitialized) {
            this.stcPayCheckoutSDKConfiguration
        } else {
            throw StcPayCheckoutSDKNotInitializedException
        }
    }

    fun openStcPayApp() {
        val hashedData = getHashedData(
            stcPayCheckoutSDKConfiguration.secretKey,
            "${stcPayCheckoutSDKConfiguration.merchantId}-${stcPayCheckoutSDKConfiguration.amount.getFormattedPrice()}-${stcPayCheckoutSDKConfiguration.externalRefId}"
        )

        val packageManager = stcPayCheckoutSDKConfiguration.context.packageManager

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

                if (this::activityResultLauncher.isInitialized) {
                    activityResultLauncher.launch(intent)
                } else {
                    throw ActivityResultLauncherNotInitializedException
                }
            }
        } else {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$STC_PAY_APP_PACKAGE_NAME")
                )
                stcPayCheckoutSDKConfiguration.context.startActivity(intent)
            } catch (e: android.content.ActivityNotFoundException) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$STC_PAY_APP_PACKAGE_NAME")
                )
                stcPayCheckoutSDKConfiguration.context.startActivity(intent)
            }
        }
    }
}