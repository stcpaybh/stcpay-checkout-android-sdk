package com.stcpay.checkout

interface StcPayCheckoutResultListener {
    fun onSuccess()
    fun onFailure()
}