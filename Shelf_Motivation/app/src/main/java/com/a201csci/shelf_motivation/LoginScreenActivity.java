package com.a201csci.shelf_motivation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signupTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //the user login already
            //firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
        }
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.login);
        editTextEmail = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        signupTextView = (TextView) findViewById(R.id.textViewSignup);

        buttonRegister.setOnClickListener(this);
        signupTextView.setOnClickListener(this);
    }

    private void userLogin(){
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

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            userLogin();
        }
        else if(view == signupTextView){
            finish();
            startActivity(new Intent(this, CreateAccountScreenActivity.class));
        }
    }
}
