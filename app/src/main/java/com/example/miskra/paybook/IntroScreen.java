package com.example.miskra.paybook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class IntroScreen extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        //Log.d("TEST", "Została wywołana metoda onCreate()");

        ProgressBar v = (ProgressBar) findViewById(R.id.progressBar);
        v.getIndeterminateDrawable().setColorFilter(0xFFccFFFF,
                android.graphics.PorterDuff.Mode.MULTIPLY);

        /* Auth */
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


        initTypes();

    }

    private void initTypes() {
        JsonTypes jsonTypes = new JsonTypes();
        jsonTypes.init(getSharedPreferences("payment_types", 0));
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
                }
            }
        };
        timer.start();

    }

    @Override
    public void onStop() {
        super.onStop();

        //Log.d("TEST", "Została wywołana metoda onStop()");

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
