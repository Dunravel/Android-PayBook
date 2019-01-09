package com.example.miskra.paybook;

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

public class RegisterScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText registerEmail;
    private EditText registerPass;
    private EditText registerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

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

        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPass = (EditText) findViewById(R.id.registerPass);
        registerName = (EditText) findViewById(R.id.registerName);
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

    public void register(View view){
        if(registerEmail.getText().toString().isEmpty()||registerPass.getText().toString().isEmpty()||registerName.getText().toString().isEmpty()) {
            Toast.makeText(RegisterScreen.this, R.string.register_data_empty,
                    Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPass.getText().toString())
                    .addOnCompleteListener(RegisterScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterScreen.this, R.string.register_failed  + " " + task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterScreen.this, R.string.register_success,
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signInWithEmailAndPassword(registerEmail.getText().toString(), registerPass.getText().toString())
                                        .addOnCompleteListener(RegisterScreen.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                if (!task.isSuccessful()) {
                                                    Log.w("TAG", "signInWithEmail:failed", task.getException());
                                                    Toast.makeText(RegisterScreen.this, R.string.auth_failed + " " + task.getException().getLocalizedMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(RegisterScreen.this, R.string.login_success,
                                                            Toast.LENGTH_SHORT).show();
                                                    SharedPreferences authentication = getSharedPreferences("authentication", 0);
                                                    SharedPreferences.Editor editor = authentication.edit();
                                                    editor.putString("authemail", registerEmail.getText().toString());
                                                    editor.putString("authpass", registerPass.getText().toString());
                                                    editor.putString("username", registerName.getText().toString());
                                                    editor.commit();

                                                    Intent dash = new Intent(RegisterScreen.this, DashboardScreen.class);
                                                    startActivity(dash);
                                                    LoginScreen.LogScr.finish();
                                                    finish();


                                                }

                                                // ...
                                            }
                                        });
                            }

                            // ...
                        }
                    });
        }

    }

    public void reset(View view){
        registerEmail.setText("");
        registerPass.setText("");
        registerName.setText("");
    }
}
