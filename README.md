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

val stcPayCheckoutSDKConfiguration = StcPayCheckoutSDKConfiguration.Builder(this) /* this should be an activity */
            .secretKey("") /* Secret key obtained from stc pay */
            .merchantId("") /* Merchat Id obtained from stc pay */
            .stcPayCheckoutResultListener(object : StcPayCheckoutResultListener {
                override fun onSuccess(transactionId: Long) {
                }

                override fun onFailure(resultCode: Int, message: String) {
                    
                }
            })
            .build()
```

#### Proceed for payment

StcPayCheckoutSDKConfiguration must be initialized, otherwise it will throw exception
```
StcPayCheckoutSDKNotInitializedException
```

Call below line when you want to open the stc pay app, please note that before calling this function you need to set the ```amount```, ```externalRefId``` and ```currentDate``` on the ```stcPayCheckoutSDKConfiguration``` object
e.g.
```
stcPayCheckoutSDKConfiguration.amount = 1.0
stcPayCheckoutSDKConfiguration.externalRefId = "<sample_ref_id>"
stcPayCheckoutSDKConfiguration.currentDate = "<current date in milliseconds>"

stcPayCheckoutSDKConfiguration.initialize(stcPayCheckoutSDKConfiguration)
```

###### Attributes

Following are functions you need to call for SDK initialization:

| Function                       | Description                              | Type                         | Required | Default value      |
|:-------------------------------|:-----------------------------------------|:-----------------------------|:---------|:-------------------|
| secretKey()                    | Set the secret key                       | String                       | Yes      | Should be non-null |
| merchantId()                   | Set the merchant ID                      | String                       | Yes      | Should be non-null |
| stcPayCheckoutResultListener() | Listener for callback of success/failure | StcPayCheckoutResultListener | Yes      | Should be non-null |

#### Callback

```StcPayCheckoutResultListener``` interface has 2 functions
```onSuccess(transactionId: Long)``` & ```onFailure(resultCode: Int, message: String)```

### Success

In onSuccess, you will receive the transactionId from stc pay which you can use to link it with your own order.

### Failure

In onFailure, you have 2 parameters

```resultCode : Int``` which can have following possible values:

| Result Code values           | 
|:-----------------------------|
| Exception = 1                |
| SessionExpired = 2           |
| SessionMissing = 24          |
| InvalidGuid = 20             |
| CustomerProfileNotFound = 26 |
| InvalidParam = 35            |
| InsufficientBalance = 72     |
| IncorrectServiceId = 79      |
| TransactionRollback = 84     |
| InvalidType = 23             |
| OtpLimitExceed = 7           |
| IncorrectOtp = 8             |
| TryCountExceed = 98          |
| Cancelled by user = -10      |

You can use them based on your own criteria for error handling.

```message : String``` which will be a String and you can use it based on your own criteria for error handling.

## Inquire API

Transactions processed from stc pay Checkout SDK can be verified through an API
To inquire the status of a particular transaction you can use below API endpoint:

##### Endpoint (POST)
#### WILL SHARE WHEN REQUIRED

##### Request

```
{
  "merchant-id": "<your merchant ID>",
  "external-transaction-id": <exteranl transaction ID of a merchant>,
  "hash": "<URL encoded hashed string created by you>"
}
```
##### Header
client-secret : <**API secret** provided to you>

#### How to create hash
You will create hash by encrypting a string using the secret key provided already.
You can use the helper function
```
fun getHashedData(secretKey: String, data: String): String
```
declared in [StcPayCheckoutHelper.kt](https://github.com/stcpaybh/stcpay-checkout-android-sdk/blob/main/stcPayCheckout/src/main/java/com/stcpay/checkout/utils/StcPayCheckoutHelper.kt).

Following are functions you need to call for SDK initialization:

| Params    | Description                          | Type   | Required | Default value      |
|:----------|:-------------------------------------|:-------|:---------|:-------------------|
| secretKey | Pass the secret key provided already | String | Yes      | Should be non-null |
| data      | String which you want to encrypt     | String | Yes      | Should be non-null |

### How to create data
You will create a data string as follow, merchant ID and external transaction ID separated by dash(-):
"<merchant-id>-<external-transaction-id>"
e.g. Your merchant ID is **1234** & Transaction ID is **5678**, then the data string will be: **"1234-5678"**

##### Response

```
{
    "response-code": 0,
    "response-message": "Paid",
    "data": {
        "transaction-id": <stc pay transaction id (Long)>
    }
}
```

#### Response Code possible values

| Values                            | 
|:----------------------------------|
| 0 - Paid                          |
| 1 - Unpaid                        |
| 2 - Merchant not found            |
| 3 - Transaction not found         |
| 4 - Hash not matched              |
| 5 - There is some technical error |
| 6 - Refunded                      |

## Refund API

Merchants can use the following API to refund the transaction, if applicable.


##### Endpoint (POST)
#### WILL SHARE WHEN REQUIRED

##### Request

```
{
  "merchant-id": "<your merchant ID>",
  "external-transaction-id": <exteranl transaction ID of a merchant>
}
```
##### Header

client-secret : <**API secret** provided to you>
client-id : <**Client ID** provided to you>


##### Response

```
{
    "response-code": 0,
    "response-message": "Amount is refunded to customer successfully."
}
```

#### Response Code possible values

| Values                                                                                                 | 
|:-------------------------------------------------------------------------------------------------------|
| 0 - Amount is refunded to customer successfully.                                                       |
| 1 - Unable to refund the amount due to some technical error. Please try again later                    |
| 5 - Unable to refund                                                                                   |
| 4 - Amount is not paid on stcpay                                                                       |
| 3 - Transaction not found                                                                              |
| 2 - Merchant not found                                                                                 |

