package com.a201csci.shelf_motivation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignup;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView loginTextView;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceUserInfo = databaseReference.child("userInfo");

        progressDialog = new ProgressDialog(this);
        buttonSignup = (Button) findViewById(R.id.createAccount);
        editTextName = (EditText) findViewById(R.id.name);
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
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check that all fields have been filled out
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (name.contains(".") || name.contains("$") || name.contains("[") || name.contains("]") || name.contains("#")) {
            Toast.makeText(this, "Cannot use '.$[]# in name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that password is long enough
        if(password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
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
                    notGuest();
                    saveUserInformation(name, email);
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
                }else{
                    Toast.makeText(CreateAccountScreenActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), CreateAccountScreenActivity.class));
                }
            }
        });

    }

    private void notGuest(){
        ((Guest) this.getApplication()).setGuest(false);
    }

    private void saveUserInformation(final String name, final String email) {

        // Create user and populate in database
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(user.getUid(), "");
        databaseReferenceUserInfo.updateChildren(map);

        databaseReferenceUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(user.getUid())) {
                        Map<String, Object> userMap = new HashMap<String, Object>();
                        userMap.put("email", email);
                        userMap.put("name", name);
                        userMap.put("bookshelf", "");
                        userMap.put("goals", "");
                        userMap.put("bookclubs", "");
                        userMap.put("notifications", "");
                        databaseReferenceUserInfo.child(user.getUid()).updateChildren(userMap);

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

//        //store the new user to database along with the username
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put(name, editTextEmail.getText().toString().trim());
//        databaseReferenceUsers.updateChildren(map);
    }
}


