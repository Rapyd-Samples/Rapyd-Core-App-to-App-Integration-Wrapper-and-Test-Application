# Rapyd Demo App #

The project contains an example app showing how to integrate an app against the Rapyd Core.

## Building the project ##

### Setup ###

To run the application open the project up in either IntelliJ or Android Studio.
The project uses gradle wrapper, so it should sync and download all dependencies automatically.

### Running app ###

To run the project a PAX device is required, since the project relies on libraries from PAX that interface with the
hardware it is not possible to run this on an emulator.
The app depends on the Rapyd Core app being installed.

## Documentation ##

### Configuring your project ###

The following needs to be added to your AndroidManifest to be able to communicate with the Rapyd Core.

```xml
<queries>
    <package android:name="net.rapyd.core"/>
</queries>
```

The **ACTION_CALLBACK** intent filter needs to be added to the activity that the Rapyd Core should return to when
any core interaction has finished. To return to the **MainActivity**, add the following in your **AndroidManifest.xml**

```xml
<activity android:name=".MainActivity"
          android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
    <intent-filter>
        <action android:name="ACTION_CALLBACK"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</activity>
```

The final configuration step is to add a receiver class to get the configuration
and transaction result from the Rapyd Core. This example shows one receiver that listens for all actions,
however it is possible to make separate receivers for each action type.

```xml
<receiver android:name=".receivers.ResultReceiver"
          android:enabled="true"
          android:exported="true">
    <intent-filter>
        <action android:name="TRANSACTION_CANCELLED_ACTION"/>
        <action android:name="TRANSACTION_RESULT_ACTION"/>
        <action android:name="END_OF_DAY_RESULT_ACTION"/>
        <action android:name="CONFIG_RESULT_ACTION"/>
    </intent-filter>
</receiver>
```

Define the **ResultReceiver** class and implement handling for each action type.

```java
public class ResultReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (TRANSACTION_RESULT_ACTION.equals(intent.getAction())) {
                // TODO: Implement handling of transaction result here
                TransactionResult result = WrapperUtils.getTransactionResultFromJson(intent.getStringExtra(Messages.TRANSACTION_RESULT));
            } else if (TRANSACTION_CANCELLED_ACTION.equals(intent.getAction())) {
                // TODO: Implement handling of cancelled transaction here
            } else if (Messages.END_OF_DAY_RESULT_ACTION.equals(intent.getAction())) {
                // TODO: Implement handling of end of day result here
            } else if (CONFIG_RESULT_ACTION.equals(intent.getAction())) {
                // TODO: Implement handling of config
                String config = intent.getStringExtra(CONFIG_RESULT);
                Config configFromJson = WrapperUtils.getConfigFromJson(config);
            }
        }
    }
}
```

### Supported actions ###

The following actions are sent from the Rapyd Core.

| Name                             | Description                                                                                                                                                                                                                                |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **TRANSACTION_RESULT_ACTION**    | Action that contains the transaction result. The transaction result is passed as a JSON string and can be obtained as a string parameter on the intent named **TRANSACTION_RESULT**. See transaction result table for further information. |
| **TRANSACTION_CANCELLED_ACTION** | Action that contains the transaction cancelled result. Omitted when the transaction is cancelled before completion.                                                                                                                        |
| **END_OF_DAY_RESULT_ACTION**     | Action that contains the end of day result. Passes the result string as a string parameter on the intent named **END_OF_DAY_RESULT**.                                                                                                      |
| **CONFIG_RESULT_ACTION**         | Action that contains the Rapyd Core configuration. This is sent at initial startup of the core.                                                                                                                                            |

### Config data fields ###

The following data is passed in the **CONFIG_RESULT** JSON string.

| Name                 | Type    | Description                                                                                        |
|----------------------|---------|----------------------------------------------------------------------------------------------------|
| printReceipt         | boolean | Boolean value indicating whether the Rapyd Core is setup to physically print receipts.             |
| preauth              | boolean | Boolean value indicating whether preauth is supported.                                             |
| useOnScreenSignature | boolean | Boolean value indicating whether signatures is written on screen.                                  |
| moto                 | boolean | Boolean value indicating whether MOTO is supported.                                                |
| refund               | boolean | Boolean value indicating whether refund is supported.                                              |
| refundSecure         | boolean | Boolean value indicating whether refunds require password.                                         |
| audioSetting         | string  | String defining the audio settings for the core, possible values are: MUTE, AUDIO, SILENT_KEYBOARD |
| additionalZeros      | boolean | Boolean value indicating whether 00 and 000 should be visible on the keyboard.                     |
| multiMerchant        | boolean | Boolean value indicating whether there are multiple merchants setup for the device.                |
| language             | string  | The language in Java locale format: is_IS                                                          |
| currentTheme         | string  | The theme of the core, possible values are: LIGHT, DARK                                            |
| logoUrl              | string  | The logo URL.                                                                                      |
| merchants            | array   | JSON array containing the merchants config, see merchant config table                              |

### Merchant config data fields ###

| Name                    | Type    | Description                                                                         |
|-------------------------|---------|-------------------------------------------------------------------------------------|
| id                      | string  | The internal ID of the merchant, this can be used to switch between merchants.      |
| terminalId              | boolean | The terminal ID of the merchant.                                                    |
| merchantId              | boolean | The merchant ID.                                                                    |
| merchantNameAndLocation | string  | Name of the merchant, shown on receipts.                                            |
| merchantAddress         | string  | The merchant address, shown on receipts.                                            |
| merchantCity            | string  | The city of the merchant, shown on receipts.                                        |
| currencyCode            | string  | ISO 4217 A3 currency code.                                                          |
| currencyNum             | string  | ISO 4217 N3 currency code.                                                          |
| countryCode             | string  | Country code in ISO 4217 N3 format.                                                 |
| merchantPassword        | string  | The merchant PIN code.                                                              |
| mailMerchant            | boolean | Boolean indicating if the merchant wants mailed receipts.                           |
| mailMerchantAddress     | string  | The mail address of the merchant.                                                   |
| printMerchantReceipt    | string  | Boolean indicating whether the merchant wants to print merchant copies of receipts. |
| dbaName                 | string  | Partner merchant DBA name.                                                          |
| dbaCity                 | string  | Partner merchant DBA city.                                                          |
| sponsoredMerchantId     | string  | Partner sponsored merchant ID.                                                      |

### Transaction result fields ###

| Name                         | Type    | Description                                                                                                                                       |
|------------------------------|---------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| uti                          | string  | The UTI of the transaction.                                                                                                                       |
| cardAcceptorAddress          | string  | The merchants address.                                                                                                                            |
| cardholderVerificationMethod | boolean | The CVM of the transaction, possible values are: CVM_NO_CVM, CVM_OFFLINE_PIN, CVM_ONLINE_PIN, CVM_SIG, CVM_ONLINE_PIN_SIG and CVM_CONSUMER_DEVICE |
| transactionDate              | string  | The transaction date.                                                                                                                             |
| primaryAccountNumber         | string  | The primary account number with first 6 and last 4 unmasked. The characters in between are masked as *.                                           |
| totalAmount                  | string  | The total transaction amount, 500.15 will be represented as the following string 50015.                                                           |
| transactionResponse          | string  | The transaction response code, two letter combination.                                                                                            |
| transactionSource            | string  | The transaction source, possible values are: PICC, ICC, MAG and MOTO                                                                              |
| transactionType              | string  | The transaction type, possible values are: SALE, REFUND, PREAUTH and COMPLETION                                                                   |
| applicationId                | string  | The application id, the selected card application of the transaction.                                                                             |
| authCode                     | string  | The auth code, only populated for approved transactions.                                                                                          |
| merchantId                   | string  | The merchant id.                                                                                                                                  |
| merchantReceipt              | string  | The merchant receipt.                                                                                                                             |
| cardholderReceipt            | string  | The cardholder receipt.                                                                                                                           |
| signature                    | string  | The signature as a byte array, only sent when signature on screen.                                                                                |
| terminalId                   | string  | The terminal id.                                                                                                                                  |
| messageId                    | integer | The message id of the transaction.                                                                                                                |
| currencyCode                 | string  | The currency code of the transaction.                                                                                                             |
| appName                      | string  | The application name in hex format.                                                                                                               |
| transSource                  | string  | The transaction source card type. 240, 250 and 260 indicates debit card. 400, 500 and 530 indicates credit card.                                  |
| referenceId                  | string  | The reference id.                                                                                                                                 |
| correlationId                | string  | The correlation id.                                                                                                                               |
| isSignature                  | boolean | Boolean indicating whether the receipt needs to be signed with signature.                                                                         |
| tipAmount                    | integer | The tip amount, 350.45 will be represented as the following 35045.                                                                                |
| dccAmount                    | integer | The DCC amount, 350.45 will be represented as the following 35045. Set to zero if no DCC.                                                         |
| dccCommissionAmount          | integer | DCC commission amount. Set to zero if no DCC.                                                                                                     |
| dccExchangeRate              | double  | The DCC exchange rate. Set to zero if no DCC.                                                                                                     |
| dccTransaction               | boolean | Boolean indicating whether this is a DCC transaction.                                                                                             |
| cashbackAmount               | integer | The cashback amount added to the transaction, 2.50 will be represented as the following 250                                                       |
| cashbackAdded                | boolean | Boolean indicating whether cashback has been added to the transaction                                                                             |


### Api ###

The wrapper library provides an **Api** class that provides a number of functions to integrate with the Rapyd Core.
In the following table all the functions and input parameters are explained.

| Function signature                                                                                                      | Description                                                                                                                                                  |
|-------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **startCore(Context context)**                                                                                          | Function to start the core, it is preferred to call when starting your app, this avoid excessive loading times when performing the first transaction.        |
| **startTransaction(long amount, TransactionType transactionType, Context context)**                                     | Function to start a transaction without any parameters. The amount is specified with two minor units. Possible transaction types are: SALE, REFUND, PREAUTH  |
| **startTransaction(long amount, TransactionType transactionType, HashMap<String, String> parameters, Context context)** | Function to start a transaction with parameters. The supported parameters when starting a transaction are the following:                                     |
| **showTransactionList(Context context)**                                                                                | Function to show transaction list.                                                                                                                           |
| **showVoidList(Context context)**                                                                                       | Function to show list of voidable transactions.                                                                                                              |
| **cycleKeys(Context context)**                                                                                          | Function to cycle keys.                                                                                                                                      |
| **sendEndOfDayReport(Context context)**                                                                                 | Function to send end of day report for active merchant.                                                                                                      |
| **requestConfig(Context context)**                                                                                      | Function to request the core config, the config is sent as CONFIG_RESULT_ACTION intent.                                                                      |
| **updateParameter(Context context, HashMap<String, String> parameters)**                                                | Function to update the parameters. The supported parameters are the following: printReceipt, useOnScreenSignature, audioSetting, themes and merchantReceipt. |
| **completion(Context context)**                                                                                         | Function to bring up the completion UI.                                                                                                                      |
| **cancelPreAuth(Context context)**                                                                                      | Function to bring up the cancel preauth UI.                                                                                                                  |
| **switchMerchant(Context context, final int merchantId)**                                                               | Function to switch merchant, the merchant id can be retrieved from the Rapyd Core configuration.                                                             |
| **printReceipt(Context context, final String uti, boolean cardHolder)**                                                 | Function to print the receipt for a given transaction. UTI of transaction is returned in the transaction result.                                             |
| **printOnCommand(Context context, final String message)**                                                               | Function to print custom formatted receipt.                                                                                                                  |
| **cancelTransaction(Context context)**                                                                                  | Function to cancel transaction, does not do anything if no transaction is ongoing.                                                                           |
