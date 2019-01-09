package com.example.miskra.paybook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphPaymentScreen extends AppCompatActivity {
    private DatabaseReference myRef;
    List<Payment> paymentsList = new ArrayList<>();
    List<String> ptypes = new ArrayList<>();
    List<PaymentMonthly> paymentMonthlyList = new ArrayList<>();
    ValueEventListener postListener;
    FirebaseUser user;
    Spinner spinner;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_payment_screen);



        Log.e("TAG", "PAAAAAAAAAAAAAAAAAAAAAAAAAAh");


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
        adapter = new ArrayAdapter<String>(GraphPaymentScreen.this, android.R.layout.simple_spinner_item,ptypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Log.e("TAG", "Preparing for graph");


        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        postListener = new ValueEventListener() {
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
               // Log.e("TAG", " downloaded amount : " + paymentsList.size());
                   /* for (int i = 0; i < paymentsList.size(); i++) {
                        Log.e("TAG", "edit payment: " + i + " :"+ paymentsList.get(i).getProduct());

                    }*/
                Log.e("TAG", "Test1: ");
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

    public void clickDraw(View view){


        Log.e("TAG", "Test2: ");
       //  myRef.child("users").child(user.getUid()).child("payments").addValueEventListener(postListener);

        Log.e("TAG", "PaymentList: "+paymentsList.size());

        for(int i = 0; i < paymentsList.size(); i++){
            Log.e("TAG", "Converting: "+i);
            try {
                Log.e("TAG", "Converting: "+i);
                int year = Integer.parseInt(paymentsList.get(i).getDate().substring(0, 4));
                int month = Integer.parseInt(paymentsList.get(i).getDate().substring(6, 7));
                double amount = Double.parseDouble(paymentsList.get(i).getAmount());
                String type = paymentsList.get(i).getType();

                Log.e("TAG", "Normal: " + i + " Type: "+type + " Year: " + year + " Month: " + month + " Amount: "+amount);


                int payPos = 0;
                for (int j = 0; j < paymentMonthlyList.size(); j++) {
                    Log.e("TAG", i +" : " + j + type + " : " + paymentMonthlyList.get(j).getType() + " "+ year + " : " +  paymentMonthlyList.get(j).getYear() + " " + month + " : " + paymentMonthlyList.get(j).getMonth() );
                    if (paymentMonthlyList.get(j).getType().equals(type)  && paymentMonthlyList.get(j).getYear() == year && paymentMonthlyList.get(j).getMonth() == month) {
                        Log.e("TAG","paypos set");
                        payPos = j;

                    }

                }
                //Log.e("TAG", "Payment monthly created");
                if (payPos != 0) {
                    Log.e("TAG","payPos != 0");
                    paymentMonthlyList.get(payPos).setAmount(paymentMonthlyList.get(payPos).getAmount() + amount);
                } else {
                    Log.e("TAG","payPos = 0");
                    paymentMonthlyList.add(new PaymentMonthly(type, amount, year, month));
                }

            } catch(NumberFormatException nfe) {}

            Log.e("TAG", "Payment monthly: " + paymentMonthlyList.size());





        }


        paymentMonthlyList.sort(new Comparator<PaymentMonthly>(){
            public int compare(PaymentMonthly obj1, PaymentMonthly obj2) {
                // ## Ascending order
                return obj1.getMonth() - obj2.getMonth(); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });

        Log.e("TAG", "posAmount");
        int posAmount =0;
        for(int z = 0; z < paymentMonthlyList.size(); z++){
            Log.e("TAG", "paymentMonthly: type: " + paymentMonthlyList.get(z).getType() + " Year: " + paymentMonthlyList.get(z).getYear() + " Month: " + paymentMonthlyList.get(z).getMonth() + "Amount: " + paymentMonthlyList.get(z).getAmount());
            if(paymentMonthlyList.get(z).getType().equals(spinner.getSelectedItem().toString()) )
                posAmount++;
        }
        Log.e("TAG", "posAmount - getting there: "+posAmount);
        DataPoint[] barNames = new DataPoint[posAmount];
        //for(int z = 0; z < paymentMonthlyList.size(); z++){
        int x = 0;
        for(int z = 0; z < paymentMonthlyList.size(); z++){
            if(paymentMonthlyList.get(z).getType().equals(spinner.getSelectedItem().toString()) )
            {
            Log.e("TAG", "Month: "+ paymentMonthlyList.get(z).getMonth() + " :"+ paymentMonthlyList.get(z).getAmount());
            barNames[x] =  new DataPoint(paymentMonthlyList.get(z).getMonth(), paymentMonthlyList.get(z).getAmount());
                x++;
            }
        }

        Log.e("TAG", "To be drawing ");
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(barNames);


        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        graph.addSeries(series);


    }



    @Override
    protected void onStart(){
        super.onStart();


    }
}
