package com.stcpay.sampleapp

import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stcpay.checkout.StcPayCheckoutResultListener
import com.stcpay.checkout.StcPayCheckoutSDK
import com.stcpay.checkout.StcPayCheckoutSDKConfiguration
import com.stcpay.sampleapp.ui.theme.StcPayCheckoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // stcPayCheckoutSDK
        val stcPayCheckoutSDKConfiguration = StcPayCheckoutSDKConfiguration.Builder(this)
            .secretKey("secretKey")
            .merchantId("merchantId")
            .merchantName("merchantName")
            .externalRefId("externalRefId")
            .amount(2.0)
            .stcPayCheckoutResultListener(object : StcPayCheckoutResultListener {
                override fun onSuccess() {}
                override fun onFailure() {}
            })
            .build()

        StcPayCheckoutSDK.initialize(stcPayCheckoutSDKConfiguration)

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
                            onClick = { StcPayCheckoutSDK.openStcPayApp() },
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StcPayCheckoutTheme {
        Greeting("Android")
    }
}