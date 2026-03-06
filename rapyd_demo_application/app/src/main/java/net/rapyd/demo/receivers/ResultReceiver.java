package net.rapyd.demo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import net.rapyd.wrapper.Messages;
import net.rapyd.wrapper.data.Config;
import net.rapyd.wrapper.data.TransactionResult;
import net.rapyd.wrapper.utils.WrapperUtils;

import static net.rapyd.wrapper.Messages.CONFIG_RESULT;
import static net.rapyd.wrapper.Messages.CONFIG_RESULT_ACTION;
import static net.rapyd.wrapper.Messages.TRANSACTION_CANCELLED_ACTION;
import static net.rapyd.wrapper.Messages.TRANSACTION_RESULT_ACTION;

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
