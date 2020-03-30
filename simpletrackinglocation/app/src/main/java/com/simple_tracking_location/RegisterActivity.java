package com.simple_tracking_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView TvEmail, TvNama, TvPassword;
    Button btnSignup;

    FirebaseAuth mFirebaseAuth;
    AwesomeValidation av = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        TvEmail = findViewById(R.id.et_email_regis);
        TvNama = findViewById(R.id.et_name_regis);
        TvPassword = findViewById(R.id.et_password_regis);

        av.addValidation(this, R.id.et_email_regis,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        av.addValidation(this, R.id.et_name_regis,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name);

        av.addValidation(this, R.id.et_password_regis,
                ".{6,}", R.string.invalid_password);

        btnSignup = findViewById(R.id.btn_register);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(av.validate()){

                    String email = TvEmail.getText().toString();
                   // String nama = TvNama.getText().toString();
                    String password = TvPassword.getText().toString();

                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sign Up not succesful, Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                Toast.makeText(RegisterActivity.this, "Success create account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

        });

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

    public void backTologinPage(View view) {
        Intent moveTologinActivity = new Intent(this, MainActivity.class);
        startActivity(moveTologinActivity);finish();
    }
}
