package com.simple_tracking_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Script;
import android.service.autofill.RegexValidator;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvRgister;
    EditText etEmail, etPassword;
    Button btnLogin;

    AwesomeValidation av;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Welcome User ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        };


        av = new AwesomeValidation(ValidationStyle.BASIC);

        av.addValidation(this, R.id.et_email,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        av.addValidation(this, R.id.et_password,
                ".{6,}", R.string.invalid_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(av.validate()){

                    String email = etEmail.getText().toString();
                    // String nama = TvNama.getText().toString();
                    String password = etPassword.getText().toString();

                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Email atau password anda salah, dan Pastikan Anda Terhubung dengan Internet", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });

        tvRgister = findViewById(R.id.tv_signup);
        tvRgister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent moveToRegister = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(moveToRegister);
        finish();
    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Are you sure want to exit ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void forgotPassword(View view) {
        Intent moveToForgotPasswordActivity = new Intent(this, ForgotPaswordActivity.class);
        startActivity(moveToForgotPasswordActivity);
        finish();
    }


}
