package com.stcpay.sampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stcpay.checkout.StcPayCheckoutResultListener
import com.stcpay.checkout.StcPayCheckoutSDK
import com.stcpay.checkout.StcPayCheckoutSDKConfiguration
import com.stcpay.sampleapp.ui.theme.StcPayCheckoutTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    private lateinit var stcPayCheckoutSDKConfiguration: StcPayCheckoutSDKConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        // stcPayCheckoutSDK
        stcPayCheckoutSDKConfiguration =
            StcPayCheckoutSDKConfiguration.Builder(this)
                .secretKey("9ec20e2b5bc569f37ad3df432b70dbb0eca39db68cd3be63d103f8ce9d1217bcef95d688334de74553f9df0c4e0171cc65f65e94c4beb8a3420cfed31ef2ab50")
                .merchantId("1")
                .stcPayCheckoutResultListener(object :
                    StcPayCheckoutResultListener {
                    override fun onSuccess(transactionId: Long) {
                        Toast.makeText(
                            context,
                            "Transaction Id: $transactionId",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(
                        resultCode: Int,
                        message: String
                    ) {
                        Toast.makeText(
                            context,
                            "ResultCode: $resultCode, Message: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                .build()

        setContent {
            StcPayCheckoutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            onClick = {

                                stcPayCheckoutSDKConfiguration.amount = 1.0
                                stcPayCheckoutSDKConfiguration.externalRefId = "${(1..10000).random()}"
                                stcPayCheckoutSDKConfiguration.currentDate = Date().time

                                StcPayCheckoutSDK.initialize(stcPayCheckoutSDKConfiguration)
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Open StcPay App",
                                fontSize = 18.sp,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}