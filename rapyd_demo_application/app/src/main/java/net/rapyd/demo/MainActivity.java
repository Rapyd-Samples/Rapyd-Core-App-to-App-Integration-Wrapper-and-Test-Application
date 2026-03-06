package net.rapyd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.rapyd.wrapper.Api;
import net.rapyd.wrapper.Parameters;
import net.rapyd.wrapper.TransactionType;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        // Start Rapyd core to allow transactions.
        try {
            // When core is started a CONFIG_RESULT_ACTION event will be sent.
            Api.startCore(this);
        } catch (Exception e) {
            // This will throw if the core is not installed, either wait
            Toast.makeText(getApplicationContext(), "Rapyd Core application is not installed, the application requires the core to work.",
                    Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);

        Button saleButton = findViewById(R.id.saleButton);
        saleButton.setOnClickListener(v -> {
            // Call to perform a simple sale with amount 1.00
            Api.startTransaction(100, TransactionType.SALE, getBaseContext());
        });

        Button refundButton = findViewById(R.id.refundButton);
        refundButton.setOnClickListener(v -> {
            // Call to perform a password protected refund with amount 1.00
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(Parameters.REFUND_WITH_PASSWORD, "true");
            Api.startTransaction(100, TransactionType.REFUND, parameters, getBaseContext());
        });

        Button cycleKeyButton = findViewById(R.id.cycleKeyButton);
        cycleKeyButton.setOnClickListener(v -> {
            // Call to perform a cycle key
            Api.cycleKeys(getBaseContext());
        });

        Button listButton = findViewById(R.id.listButton);
        listButton.setOnClickListener(v -> {
            // Call to show the transaction list
            Api.showTransactionList(getBaseContext());
        });

        Button preauthButton = findViewById(R.id.preAuthButton);
        preauthButton.setOnClickListener(v -> {
            // Call to perform a preauth with amount 100
            Api.startTransaction(100, TransactionType.PREAUTH, getBaseContext());
        });

        Button completionButton = findViewById(R.id.completionButton);
        completionButton.setOnClickListener(v -> {
            // Call to perform a completion
            Api.completion(getBaseContext());
        });

        Button voidButton = findViewById(R.id.voidButton);
        voidButton.setOnClickListener(v -> {
            // Call to bring up the void list
            Api.showVoidList(getBaseContext());
        });

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> {
            // Call to send the end of day
            Api.sendEndOfDayReport(getBaseContext());
        });

        Button exitButton = findViewById(R.id.exit);
        exitButton.setOnClickListener(v -> {
            // Shutdown app
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            Api.shutdownCore(getBaseContext());
            finishAndRemoveTask();
        });
    }
}
