package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        if ( ((Guest) this.getApplication()).getGuest()){
            Button button= (Button) findViewById(R.id.logout);
            button.setText("LOGIN");

        }else{
            View button= findViewById(R.id.createaccount);
            button.setVisibility(View.GONE);
        }

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                firebaseAuth.signOut();
                finish();
                Intent activityChangeIntent = new Intent(SettingsActivity.this, StartScreenActivity.class);
                startActivity(activityChangeIntent);
            }
        });

        final Button createAccount = (Button) findViewById(R.id.createaccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(SettingsActivity.this, CreateAccountScreenActivity.class);

                startActivity(activityChangeIntent);
            }
        });


    }

//    @Override
//    public void onClick(View view) {
//        if(view == logout){
//            firebaseAuth.signOut();
//            finish();
//            startActivity(new Intent(this, LoginScreenActivity.class));
//        }
//    }
}
