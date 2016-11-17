package com.a201csci.shelf_motivation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        buttonSignup = (Button) findViewById(R.id.createAccount);
        editTextEmail = (EditText) findViewById(R.id.signupEmail);
        editTextPassword = (EditText) findViewById(R.id.signupPassword);
        loginTextView = (TextView) findViewById(R.id.textViewLogin);

        buttonSignup.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            registerUser();
        }
        if(view == loginTextView){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginScreenActivity.class));
        }
    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CreateAccountScreenActivity.this, "You are registered!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
                        }else{
                            Toast.makeText(CreateAccountScreenActivity.this, "Oops! Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}


