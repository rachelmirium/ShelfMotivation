package com.a201csci.shelf_motivation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LoginScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signupTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
<<<<<<< HEAD
            //the user login already
            //firebaseAuth.signOut();
=======
            // Sign out the user if still logged in
            Log.d("USER", firebaseAuth.getCurrentUser().getEmail());
            firebaseAuth.signOut();
            return;
>>>>>>> origin/master

//            finish();
//            startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
        }
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
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
<<<<<<< HEAD
                            Log.d("USER EMAIL", firebaseAuth.getCurrentUser().getEmail());
=======

                            // Check if user is in database, update last login date if so
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (dataSnapshot.hasChild(user.getUid())) {
                                        Log.d("DBCHECK", "Updating last login time!");
                                        long timeStamp = System.currentTimeMillis();
                                        databaseReference.child(user.getUid()).child("lastLogin").setValue(timeStamp);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });
>>>>>>> origin/master

                            notGuest();
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


    private void notGuest(){
        ((Guest) this.getApplication()).setGuest(false);
    }
}
