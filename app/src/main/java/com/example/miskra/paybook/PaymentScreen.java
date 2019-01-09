package com.example.miskra.paybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PaymentScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
    }

    public void cancel(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
