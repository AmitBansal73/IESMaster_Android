package com.example.iesmaster;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.braintreepayments.cardform.view.CardForm;

public class PaymentActivity extends AppCompatActivity {
    Button btnBuy;
    CardForm card_form;
    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        btnBuy = findViewById(R.id.btnBuy);
        card_form = findViewById(R.id.card_form);

        card_form.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);

        card_form.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (card_form.isValid()) {
                    alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                    alertBuilder.setTitle("Confirm purchase");
                 alertBuilder.setMessage("Card number: " + card_form.getCardNumber() + "\n" +
                            "Card expiry date: " + card_form.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + card_form.getCvv() + "\n" +
                            "Postal code: " + card_form.getPostalCode() + "\n" +
                            "Phone number: " + card_form.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(PaymentActivity.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

        // PaymentActivity.this.finish();
    }
}
