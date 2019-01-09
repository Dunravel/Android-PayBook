package com.example.miskra.paybook;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardScreen extends AppCompatActivity {

    private List<Payment> paymentsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PaymentsListAdapter mAdapter;
    DatabaseReference myRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        //get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         myRef = database.getReference("message");
//get reference


//build child



        recyclerView = (RecyclerView) findViewById(R.id.dashPaymentsList);

        mAdapter = new PaymentsListAdapter(paymentsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                //Toast.makeText(DashboardScreen.this, "Single Click on position        :"+position,
                 //       Toast.LENGTH_SHORT).show();

                mStorageRef = FirebaseStorage.getInstance().getReference();

               /* Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                StorageReference riversRef = storageRef.child("images/rivers.jpg");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });*/
            }

            @Override
            public void onLongClick(View view, final int position) {
                //Toast.makeText(DashboardScreen.this, "Long press on position :"+position,
                //        Toast.LENGTH_LONG).show();

                //set up dialog
                final Dialog dialog = new Dialog(DashboardScreen.this);
                dialog.setContentView(R.layout.layout_payment_click_dialog);
                dialog.setTitle("This is my custom dialog box");
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!

                //set up button
                Button buttonEdit = (Button) dialog.findViewById(R.id.paymentDialogEdit);
                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent edit = new Intent(DashboardScreen.this, EditPaymentScreen.class);
                        edit.putExtra("position", position);
                        edit.putExtra("childId",paymentsList.get(position).getPaymentId());
                        startActivity(edit);
                       dialog.cancel();
                    }
                });

                Button buttonDelete = (Button) dialog.findViewById(R.id.paymentDialogDelete);
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("message");

                        myRef.child("users").child(user.getUid()).child("payments").child(paymentsList.get(position).getPaymentId()).removeValue();

                        dialog.cancel();
                    }
                });

                //now that the dialog is set up, it's time to show it
                dialog.show();
            }
        }));

        /*
        SharedPreferences payments = getSharedPreferences("payments", 0);
        String JSONpaymentsList = payments.getString("payments", "");

        List<String> ptypes = new ArrayList<String>();
        if (!JSONpaymentsList.isEmpty()) {
            try {
                Log.e("TAG", "Json jsonTypes not empty");
                JSONObject jsonObj = new JSONObject(JSONpaymentsList);

                // Getting JSON Array node
                JSONArray jsonPaymentsArray = jsonObj.getJSONArray("payments");

                // looping through All Contacts
                for (int i = 0; i < jsonPaymentsArray.length(); i++) {
                    JSONObject jsonPayment = jsonPaymentsArray.getJSONObject(i);

                    Payment payment = new Payment(jsonPayment.getString("Type"), jsonPayment.getString("Shop"), jsonPayment.getString("Amount"), jsonPayment.getString("Date"));
                    paymentsList.add(payment);
                }


            } catch (final JSONException ex) {
                Log.e("TAG", "Json parsing error: " + ex.getMessage());
            }
        }
    */
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                // ...
                Log.e("TAG", "On data change ");
                //List<Payment> payTest = new ArrayList<>();
                Log.e("TAG", "Trying to download data ");
                paymentsList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    paymentsList.add(postSnapshot.getValue(Payment.class));

                    /*for (int i = 0; i < paymentsList.size(); i++) {
                        Log.e("TAG", "Added payment: " + i + " :"+ paymentsList.get(i).getProduct());

                    }
                    */
                }
                mAdapter.notifyDataSetChanged();

                double amountThisMonth = 0;
                double amountPrevoiusMonth = 0;
                Calendar calendar = Calendar.getInstance();
                int thisMonth = calendar.get(Calendar.MONTH) +1;
                int thisYear = calendar.get(Calendar.YEAR);



                Log.e("TAG", "Curernt Year ");
                for(int i = 0; i < paymentsList.size(); i++){
                    try{
                        Log.e("TAG", "This year " + paymentsList.get(i).getDate().substring(0, 4));
                        Log.e("TAG", "This month" + paymentsList.get(i).getDate().substring(6, 7));
                        int year = Integer.parseInt(paymentsList.get(i).getDate().substring(0, 4));
                        int month = Integer.parseInt(paymentsList.get(i).getDate().substring(6, 7));
                        double amount = Double.parseDouble(paymentsList.get(i).getAmount());


                        if(year == thisYear && month == thisMonth){
                            amountThisMonth = amountThisMonth + amount;
                        }
                        if(thisMonth - 1 == 0){
                            if(year == thisYear -1 && month == 12)
                                amountPrevoiusMonth = amountPrevoiusMonth + amount;
                        }else{
                            if(year == thisYear  && month == thisMonth -1)
                                amountPrevoiusMonth = amountPrevoiusMonth + amount;
                        }
                    }
                    catch(NumberFormatException nfe){

                    }
                }

                TextView vAmountThisMonth = (TextView) findViewById(R.id.amountThisMonth);

                TextView vAmountpreviousMonth = (TextView) findViewById(R.id.amountPreviosMonth);

                vAmountThisMonth.setText("Paid this month: " + amountThisMonth);
                vAmountpreviousMonth.setText("Paid last month: " + amountPrevoiusMonth);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.child("users").child(user.getUid()).child("payments").addValueEventListener(postListener);
        Log.e("tag","After firebase");






    }

    private void newPayment(String type, String shop, String amount, String date){
        Payment payment = new Payment(type, shop, amount,date);
        paymentsList.add(payment);
        mAdapter.notifyDataSetChanged();

       /* String JSONpaymentsList = "{\"payments\":[{\"Type\":\""+paymentsList.get(0).getType()+"\", \"Shop\":\""
                +paymentsList.get(0).getShop()+"\", \"Amount\":\""+paymentsList.get(0).getAmount()+"\", \"Date\":\""
                +paymentsList.get(0).getDate()+"\"}";

        for(int i = 1; i < paymentsList.size(); i++){
            JSONpaymentsList = JSONpaymentsList + ",{\"Type\":\"" + paymentsList.get(0).getType() + "\", \"Shop\":\""
                    + paymentsList.get(0).getShop() + "\", \"Amount\":\"" + paymentsList.get(0).getAmount() + "\", \"Date\":\""
                    + paymentsList.get(0).getDate() + "\"}";
        }

        JSONpaymentsList = JSONpaymentsList + "]}";

        SharedPreferences payments = getSharedPreferences("payments", 0);
        SharedPreferences.Editor editor = payments.edit();
        editor.putString("payments", JSONpaymentsList);
        editor.commit();
        */
    }
    public void graphView(View view){
        Intent graph = new Intent(DashboardScreen.this, GraphPaymentScreen.class);
        startActivity(graph);

    }

    public void addPayment(View view){
        Intent add_payment = new Intent(DashboardScreen.this, AddPaymentScreen.class);
        startActivityForResult(add_payment,1);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String paymentAmount = data.getStringExtra("paymentAmount");
                String paymentShop = data.getStringExtra("paymentShop");
                String paymentDate = data.getStringExtra("paymentDate");
                String paymentType = data.getStringExtra("paymentType");
                Log.e("TAG", "Added payment: " + paymentType + " " +paymentShop+" "+paymentAmount+" "+paymentDate );
                newPayment(paymentType,paymentShop,paymentAmount,paymentDate);
            }
        }
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }
}
