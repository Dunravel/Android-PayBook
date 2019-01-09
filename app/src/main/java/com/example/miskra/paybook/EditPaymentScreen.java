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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPaymentScreen extends AppCompatActivity {

    private int position;
    private String childId;
    DatabaseReference myRef;

    private Spinner addPaymentType;
    private EditText addPaymentProduct;
    private EditText addPaymentAmount;
    private EditText addPaymentShop;

    private EditText addPaymentDate;

    List<Payment> paymentsList;
    ArrayAdapter<String> adapter;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_screen);

        Log.e("TAG", "EDIT  ");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        addPaymentType = (Spinner) findViewById(R.id.editPaymentTypes);
        addPaymentProduct = (EditText) findViewById(R.id.editPaymentProducts);
        addPaymentAmount = (EditText) findViewById(R.id.editPaymentAmount);
        addPaymentShop = (EditText) findViewById(R.id.editPaymentShop);
        addPaymentDate = (EditText) findViewById(R.id.editPaymentDate);

        SharedPreferences paymentTypes = getSharedPreferences("payment_types", 0);
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

        spinner = (Spinner) findViewById(R.id.editPaymentTypes);
        adapter = new ArrayAdapter<String>(EditPaymentScreen.this, android.R.layout.simple_spinner_item,ptypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Log.e("TAG", "EDIT: Trying to download data ");
        position = getIntent().getIntExtra("position",-1);
        childId = getIntent().getStringExtra("childId");
        if(position <0)
            finish();
        else{

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    //Post post = dataSnapshot.getValue(Post.class);
                    // ...
                   ;
                    //List<Payment> payTest = new ArrayList<>();
                    Log.e("TAG", "Starting download ");
                    paymentsList = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        paymentsList.add(postSnapshot.getValue(Payment.class));

                    }
                    Log.e("TAG", " downloaded amount : " + paymentsList.size());
                   /* for (int i = 0; i < paymentsList.size(); i++) {
                        Log.e("TAG", "edit payment: " + i + " :"+ paymentsList.get(i).getProduct());

                    }*/
                    Log.e("TAG", "edit payment: " + position + " :"+ paymentsList.get(position).getProduct() + " PaymentID: "+paymentsList.get(position).getPaymentId().toString());
                    Log.e("TAG", "Trying to insert data ");
                    //addPaymentType

                    int spinnerPosition = adapter.getPosition(paymentsList.get(position).getType());
                    spinner.setSelection(spinnerPosition);

                    addPaymentProduct.setText(paymentsList.get(position).getProduct());
                    addPaymentAmount.setText(paymentsList.get(position).getAmount());
                    addPaymentShop.setText(paymentsList.get(position).getShop());
                    addPaymentDate.setText(paymentsList.get(position).getDate());

                }



                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            //myRef.child("users").child(user.getUid()).child("payments").child(childId).addValueEventListener(postListener);
            myRef.child("users").child(user.getUid()).child("payments").addValueEventListener(postListener);
        }
    }

    public void editSave(View view){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
//get reference
        Log.e("TAG", "Trying to update data ");

//build child
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("paymentId",paymentsList.get(position).getPaymentId().toString());
        childUpdates.put("product",addPaymentProduct.getText().toString());
        childUpdates.put("type",addPaymentType.getSelectedItem().toString());
        childUpdates.put("shop",addPaymentShop.getText().toString());
        childUpdates.put("amount",addPaymentAmount.getText().toString());
        childUpdates.put("date",addPaymentDate.getText().toString());
        childUpdates.put("popId",0);

        myRef.child("users").child(user.getUid()).child("payments").child(paymentsList.get(position).getPaymentId().toString()).updateChildren(childUpdates);
        Log.e("TAG", "Data updated");
        finish();
    }

    public void editCancel(View view){
        finish();
    }
}
