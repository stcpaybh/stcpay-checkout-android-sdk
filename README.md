# stc pay checkout Android Sdk

[![](https://jitpack.io/v/stcpaybh/stcpay-checkout-android-sdk.svg)](https://jitpack.io/#stcpaybh/stcpay-checkout-android-sdk)

## Installation

SDK for Android supports API 21 and above.

##### Step 1.
Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

##### Step 2.
Add the dependency

```Gradle
dependencies {
  implementation 'com.github.stcpaybh:stcpay-checkout-android-sdk:<version>'
}
```

## Sample Apps

A project with basic example is provided [here](https://github.com/stcpaybh/stcpay-checkout-android-sdk/tree/main/sampleapp).

## Code Initialization

To initialize the stc pay checkout SDK in your app, use below snippet in your app's Application class or where ever you seems appropiate:

#### Initialize SDK

```kotlin

StcPayCheckoutSDKConfiguration.Builder(this) /* this should be an activity */
            .secretKey("") /* Secret key obtained from stc pay */
            .merchantId("") /* Merchat Id obtained from stc pay */
            .externalRefId("") /* Your own orderId for that payment */
            .amount() /* Amount for that payment */
            .stcPayCheckoutResultListener(object : StcPayCheckoutResultListener {
                override fun onSuccess(transactionId: Long) {
                }

                override fun onFailure(resultCode: Int, message: String) {
                    
                }
            })
            .build()

StcPayCheckoutSDK.initialize(stcPayCheckoutSDKConfiguration)

```

#### Proceed for payment

StcPayCheckoutSDKConfiguration must be initialized, otherwise it will throw exception
```
StcPayCheckoutSDKNotInitializedException
```

Call below line when you want to open the stc pay app
```kotlin
StcPayCheckoutSDK.openStcPayApp()
```

###### Attributes

Following are functions you need to call for SDK initialization:

| Function |  Description | Type | Required | Default value |
|:---|:---|:---|:---|:---|
| secretKey() |Set the secret key | String | Yes | Should be non-null |
| merchantId() | Set the merchant ID | String| Yes | Should be non-null |
| externalRefId() | Set the orderID of your payment | String | Yes| Should be non-null |
| amount() | Amount for that orderID | Double| Yes | false | Should be greater than 0 |
| stcPayCheckoutResultListener() | Listener for callback of success/failure | StcPayCheckoutResultListener | Yes | Should be non-null |

#### Callback

```StcPayCheckoutResultListener``` interface has 2 functions
```onSuccess(transactionId: Long)``` & ```onFailure(resultCode: Int, message: String)```

### Success

In onSuccess, you will receive the transactionId from stc pay which you can use to link it with your own order.

### Failure

In onFailure, you have 2 parameters

```resultCode : Int``` which can have following possible values:

| Result Code values | 
|:---|
|Exception = 1|
|SessionExpired = 2|
|SessionMissing = 24|
|InvalidGuid = 20|
|CustomerProfileNotFound = 26|
|InvalidParam = 35|
|InsufficientBalance = 72|
|IncorrectServiceId = 79|
|TransactionRollback = 84|
|InvalidType = 23|
|OtpLimitExceed = 7|
|IncorrectOtp = 8|
|TryCountExceed = 98|

You can use them based on your own criteria for error handling.

```message : String``` which will be a String and you can use it based on your own criteria for error handling.



