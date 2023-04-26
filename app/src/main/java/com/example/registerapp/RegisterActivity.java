package com.example.registerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName,editTextRegisterEmail,editTextRegisterMobile,editTextRegisterPassword,editTextRegisterConfirmPassword;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You  can register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_Full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPassword = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPassword = findViewById(R.id.editText_register_confirm_password);

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtain the entered data
                String textFullname = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPassword = editTextRegisterPassword.getText().toString();
                String textConfirmPassword = editTextRegisterConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(textFullname)) {
                    Toast.makeText(RegisterActivity.this, "please enter your full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "please enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "please enter your mobile no", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. is required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length()!=10) {
                    Toast.makeText(RegisterActivity.this, "please re-enter your mobile no", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "please enter your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterPassword.setError("Password is required");
                    editTextRegisterPassword.requestFocus();
                } else if (textPassword.length()< 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPassword.setError("Password to weak");
                    editTextRegisterPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "please enter your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPassword.setError("Password Confirmation is required");
                    editTextRegisterConfirmPassword.requestFocus();
                } else if (!textPassword.equals(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "please same same Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPassword.setError("Password Confirmation is required");
                    editTextRegisterConfirmPassword.requestFocus();
                    //Clear the entered passwords
                    editTextRegisterPassword.clearComposingText();
                    editTextRegisterConfirmPassword.clearComposingText();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullname, textEmail, textMobile, textPassword, textConfirmPassword);


                }


            }




    //Register User using the credential given
    private void registerUser(String textFullname, String textEmail, String textMobile, String textPassword, String textConfirmPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Send Verification Email
                    firebaseUser.sendEmailVerification();

                    //Open User Profile after successful registration
                    Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                    //To Prevent User from returning back to Register Activity on pressing back button after registration
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish(); //to close Register Activity
                }
            };
        });

    };
});
        }
        }