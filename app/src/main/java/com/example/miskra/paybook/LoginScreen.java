package com.example.miskra.paybook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText loginEmail;
    private EditText loginPass;


    public static Activity LogScr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        LogScr = this;

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

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);
        //findViewById(R.id.loginButtonLogin).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void login(View view){
        if(loginEmail.getText().toString().isEmpty()) {
            Toast.makeText(LoginScreen.this, R.string.login_email_empty,
                    Toast.LENGTH_SHORT).show();
        }else if(loginPass.getText().toString().isEmpty()){
            Toast.makeText(LoginScreen.this, R.string.login_pass_empty,
                    Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPass.getText().toString())
                    .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginScreen.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                SharedPreferences authentication = getSharedPreferences("authentication", 0);
                                SharedPreferences.Editor editor = authentication.edit();
                                editor.putString("authemail", loginEmail.getText().toString());
                                editor.putString("authpass", loginPass.getText().toString());
                                editor.putString("userid", mAuth.getCurrentUser().getUid().toString());
                                editor.commit();

                                Intent dash = new Intent(LoginScreen.this, DashboardScreen.class);
                                startActivity(dash);
                                finish();
                            }

                            // ...
                        }
                    });
        }
    }

    public void register(View view){
        Intent register = new Intent(LoginScreen.this, RegisterScreen.class);
        startActivity(register);
    }
}
