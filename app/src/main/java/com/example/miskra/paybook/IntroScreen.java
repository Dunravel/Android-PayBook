package com.example.miskra.paybook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.password;


public class IntroScreen extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        Log.d("TEST", "Została wywołana metoda onCreate()");

        /*
        AlertDialog alertDialog = new AlertDialog.Builder(IntroScreen.this).create();
        alertDialog.setTitle("This is onCreate");
        alertDialog.setMessage("The personal data has just been saved in the database");
        alertDialog.show();
        */

        ProgressBar v = (ProgressBar) findViewById(R.id.progressBar);
        v.getIndeterminateDrawable().setColorFilter(0xFFccFFFF,
                android.graphics.PorterDuff.Mode.MULTIPLY);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        SharedPreferences paymentTypes = getSharedPreferences("payment_types", 0);
        String jsonTypes = paymentTypes.getString("types", "");

        List<String> ptypes = new ArrayList<String>();
        if (!jsonTypes.isEmpty()) {
            try {
                Log.e("TAG", "Json jsonTypes not empty");
                JSONObject jsonObj = new JSONObject(jsonTypes);

                // Getting JSON Array node
                JSONArray JSONarrTypes = jsonObj.getJSONArray("paymentTypes");

                // looping through All Contacts
                for (int i = 0; i < JSONarrTypes.length(); i++) {
                    ptypes.add(JSONarrTypes.getString(i));
                }


            } catch (final JSONException ex) {
                Log.e("TAG", "Json parsing error: " + ex.getMessage());
                ptypes.add("Groceries");
                ptypes.add("Car");
                ptypes.add("House");
                ptypes.add("Clothes");
                ptypes.add("Gifts");
                ptypes.add("Playtime");
                ptypes.add("Tech");
                ptypes.add("Children");
                ptypes.add("Work");

                jsonTypes = "{\"paymentTypes\":[ \"" + ptypes.get(0)+"\"";
                Log.e("TAG", "registered jsontypes: "+ jsonTypes);
                for (int i = 1; i < ptypes.size(); i++) {
                    jsonTypes = ",\"" + ptypes.get(i) + "\"";
                }
                jsonTypes = jsonTypes + "]}";



                SharedPreferences.Editor editor = paymentTypes.edit();
                editor.putString("types", jsonTypes);
                editor.commit();
            }
        } else {

            ptypes.add("Groceries");
            ptypes.add("Car");
            ptypes.add("House");
            ptypes.add("Groceries");
            ptypes.add("Car");
            ptypes.add("House");
            ptypes.add("Clothes");
            ptypes.add("Gifts");
            ptypes.add("Playtime");
            ptypes.add("Tech");
            ptypes.add("Children");
            ptypes.add("Work");

            jsonTypes = "{\"paymentTypes\":[ \"" + ptypes.get(0)+"\"";
            Log.e("TAG", "registered jsontypes: "+ jsonTypes);
            for (int i = 1; i < ptypes.size(); i++) {
                jsonTypes = ",\"" + ptypes.get(i) + "\"";
            }
            jsonTypes = jsonTypes + "]}";



            SharedPreferences.Editor editor = paymentTypes.edit();
            editor.putString("types", jsonTypes);
            editor.commit();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("TEST", "Została wywołana metoda onStart()");

        mAuth.addAuthStateListener(mAuthListener);
        /*
        AlertDialog alertDialog = new AlertDialog.Builder(IntroScreen.this).create();
        alertDialog.setTitle("This is onStart");
        alertDialog.setMessage("The personal data has just been saved in the database");
        alertDialog.show();
        */


        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally{

                    Intent login = new Intent(IntroScreen.this, LoginScreen.class);
                    startActivity(login);
                    finish();

                    /*

                    SharedPreferences authentication = getSharedPreferences("authentication", 0);

                    if(authentication.contains("authemail")) {
                        String authemail = authentication.getString("authemail", "");
                        String authpass = authentication.getString("authpass","");


                        if(authemail.isEmpty() || authpass.isEmpty()){
                            Intent login = new Intent(IntroScreen.this, LoginScreen.class);
                            startActivity(login);
                            finish();
                        }
                        else{

                            mAuth.signInWithEmailAndPassword(authemail, authpass)
                                    .addOnCompleteListener(IntroScreen.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Log.w("TAG", "signInWithEmail:failed", task.getException());
                                                //Toast.makeText(IntroScreen.this, R.string.auth_failed,
                                                 //       Toast.LENGTH_SHORT).show();
                                                Intent login = new Intent(IntroScreen.this, LoginScreen.class);
                                                startActivity(login);
                                                finish();
                                            }else{
                                                Intent dash = new Intent(IntroScreen.this, DashboardScreen.class);
                                                startActivity(dash);
                                                finish();
                                            }

                                            // ...
                                        }
                                    });
                        }
                    }
                    else{
                        Intent login = new Intent(IntroScreen.this, LoginScreen.class);
                        startActivity(login);
                        finish();
                    }
                    */
                    /*
                    AlertDialog alertDialog = new AlertDialog.Builder(IntroScreen.this).create();
                    alertDialog.setTitle("This is onCreate");
                    alertDialog.setMessage("The personal data has just been saved in the database");
                    alertDialog.show();
                    */


                }


            }
        };
        timer.start();


    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("TEST", "Została wywołana metoda onStop()");

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
