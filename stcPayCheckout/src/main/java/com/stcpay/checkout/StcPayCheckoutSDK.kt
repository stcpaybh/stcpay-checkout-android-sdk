package com.stcpay.checkout

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.stcpay.checkout.utils.AMOUNT
import com.stcpay.checkout.utils.ActivityResultLauncherNotInitializedException
import com.stcpay.checkout.utils.DATE_MILLISECONDS
import com.stcpay.checkout.utils.EXTERNAL_REF_ID
import com.stcpay.checkout.utils.HASHED_DATA
import com.stcpay.checkout.utils.MERCHANT_ID
import com.stcpay.checkout.utils.STC_PAY_APP_PACKAGE_NAME
import com.stcpay.checkout.utils.STC_PAY_APP_PACKAGE_NAME_UAT
import com.stcpay.checkout.utils.StcPayCheckoutSDKNotInitializedException
import com.stcpay.checkout.utils.getFormattedPrice
import com.stcpay.checkout.utils.getHashedData

object StcPayCheckoutSDK {
    private lateinit var stcPayCheckoutSDKConfiguration: StcPayCheckoutSDKConfiguration

    fun initialize(
        stcPayCheckoutSDKConfiguration: StcPayCheckoutSDKConfiguration
    ) {
        this.stcPayCheckoutSDKConfiguration = stcPayCheckoutSDKConfiguration
        this.stcPayCheckoutSDKConfiguration.validate()
        openStcPayApp()
    }

    private fun openStcPayApp() {
        if (this::stcPayCheckoutSDKConfiguration.isInitialized) {
            val hashedData = getHashedData(
                stcPayCheckoutSDKConfiguration.secretKey,
                "${stcPayCheckoutSDKConfiguration.merchantId}-${stcPayCheckoutSDKConfiguration.externalRefId}-${stcPayCheckoutSDKConfiguration.amount.getFormattedPrice()}"
            )

            val packageManager = stcPayCheckoutSDKConfiguration.context.packageManager

            if (isAppInstalled(packageManager, STC_PAY_APP_PACKAGE_NAME_UAT)) {
                openApp(packageManager, STC_PAY_APP_PACKAGE_NAME_UAT, hashedData)
            } else if (isAppInstalled(packageManager, STC_PAY_APP_PACKAGE_NAME)) {
                openApp(packageManager, STC_PAY_APP_PACKAGE_NAME, hashedData)
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
        } else {
            throw StcPayCheckoutSDKNotInitializedException
        }
    }

    private fun openApp(
        packageManager: PackageManager,
        packageName: String,
        hashedData: String
    ) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            intent.apply {
                flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
                putExtra(MERCHANT_ID, stcPayCheckoutSDKConfiguration.merchantId)
                putExtra(EXTERNAL_REF_ID, stcPayCheckoutSDKConfiguration.externalRefId)
                putExtra(AMOUNT, stcPayCheckoutSDKConfiguration.amount)
                putExtra(DATE_MILLISECONDS, stcPayCheckoutSDKConfiguration.currentDate)
                putExtra(HASHED_DATA, hashedData)
            }

            if (this.stcPayCheckoutSDKConfiguration.listener != null) {
                stcPayCheckoutSDKConfiguration.listener?.launch(intent)
            } else {
                throw ActivityResultLauncherNotInitializedException
            }
        }
    }

    private fun isAppInstalled(
        packageManager: PackageManager,
        packageName: String
    ): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}