package com.example.miskra.paybook;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPaymentScreen extends AppCompatActivity {
    private Spinner addPaymentType;
    private EditText addPaymentProduct;
    private EditText addPaymentAmount;
    private EditText addPaymentShop;

    private EditText addPaymentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_screen);

        SharedPreferences paymentTypes = getSharedPreferences("payment_types", 0);
        String jsonTypes = paymentTypes.getString("types", "");
        Log.e("TAG", "jsonTypes: " + jsonTypes);
        List<String> ptypes = new ArrayList<String>();
        ptypes.add("Groceries");
        ptypes.add("Car");
        ptypes.add("House");
        ptypes.add("Clothes");
        ptypes.add("Gifts");
        ptypes.add("Playtime");
        ptypes.add("Tech");
        ptypes.add("Children");
        ptypes.add("Work");
       /* if (!jsonTypes.isEmpty()) {
            try {
                Log.e("TAG", "Json jsonTypes not empty");
                JSONObject jsonObj = new JSONObject(jsonTypes);

                // Getting JSON Array node
                JSONArray JSONarrTypes = jsonObj.getJSONArray("paymentTypes");

                // looping through All Contacts
                for (int i = 0; i < JSONarrTypes.length(); i++) {
                    JSONObject jsonType = JSONarrTypes.getJSONObject(i);
                    ptypes.add(jsonType.getString("type"));

                }


            } catch (final JSONException ex) {
                Log.e("TAG", "Json parsing error: " + ex.getMessage());
                ptypes = new ArrayList<String>();
                ptypes.add("Groceries");
                ptypes.add("Car");
                ptypes.add("House");
                ptypes.add("Clothes");
                ptypes.add("Gifts");
                ptypes.add("Playtime");
                ptypes.add("Tech");
                ptypes.add("Children");
                ptypes.add("Work");

                jsonTypes = "{\"paymentTypes\":[ {\"type\":\"" + ptypes.get(0)+"\"}";
                Log.e("TAG", "registered jsontypes: "+ jsonTypes);
                for (int i = 1; i < ptypes.size(); i++) {
                    jsonTypes = jsonTypes + ",{\"type\":\"" + ptypes.get(i) + "\"}";
                }
                jsonTypes = jsonTypes + "]}";

                Log.e("TAG", "Json parsing error: " + ex.getMessage());

                SharedPreferences.Editor editor = paymentTypes.edit();
                editor.putString("types", jsonTypes);
                editor.commit();
            }
        } else {
            ptypes.add("Groceries");
            ptypes.add("Car");
            ptypes.add("House");
            ptypes.add("Clothes");
            ptypes.add("Gifts");
            ptypes.add("Playtime");
            ptypes.add("Tech");
            ptypes.add("Children");
            ptypes.add("Work");

            jsonTypes = "{\"paymentTypes\":[ {\"type\":\"" + ptypes.get(0)+"\"}";
            Log.e("TAG", "registered jsontypes: "+ jsonTypes);
            for (int i = 1; i < ptypes.size(); i++) {
                jsonTypes = jsonTypes + ",{\"type\":\"" + ptypes.get(i) + "\"}";
            }
            jsonTypes = jsonTypes + "]}";



            SharedPreferences.Editor editor = paymentTypes.edit();
            editor.putString("types", jsonTypes);
            editor.commit();
        }
    */

        Spinner spinner = (Spinner) findViewById(R.id.editPaymentTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPaymentScreen.this, android.R.layout.simple_spinner_item,ptypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        addPaymentType = (Spinner) findViewById(R.id.editPaymentTypes);
        addPaymentProduct = (EditText) findViewById(R.id.editPaymentProducts);
        addPaymentAmount = (EditText) findViewById(R.id.editPaymentAmount);
        addPaymentShop = (EditText) findViewById(R.id.editPaymentShop);
        addPaymentDate = (EditText) findViewById(R.id.editPaymentDate);



    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void addPayment(View view){

        //get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
//get reference
        Payment newPayment = new Payment(addPaymentProduct.getText().toString(),addPaymentType.getSelectedItem().toString(), addPaymentShop.getText().toString(), addPaymentAmount.getText().toString(),addPaymentDate.getText().toString());
//build child
        Long timeLong = System.currentTimeMillis()/1000;
        myRef.child("users").child(user.getUid()).child("payments").child(timeLong.toString()).setValue(newPayment);
       //myRef.child("users").child(user.getUid()).child("payments").push().setValue(newPayment);
        /*
        Intent retData=new Intent();
        retData.putExtra("paymentAmount", addPaymentAmount.getText().toString());
        retData.putExtra("paymentShop", addPaymentShop.getText().toString());
        retData.putExtra("paymentDate",addPaymentDate.getText().toString());
        retData.putExtra("paymentType", addPaymentType.getSelectedItem().toString());
        setResult(RESULT_OK, retData);
        */
        finish();

    }
}
