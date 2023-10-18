package com.stcpay.checkout

interface StcPayCheckoutResultListener {
    fun onSuccess(transactionId: Long)
    fun onFailure(resultCode: Int, message:String )
}