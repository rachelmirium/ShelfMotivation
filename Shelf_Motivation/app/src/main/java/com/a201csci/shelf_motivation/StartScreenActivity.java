package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StartScreenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(StartScreenActivity.this, LoginScreenActivity.class);

                // currentContext.startActivity(activityChangeIntent);

               startActivity(activityChangeIntent);
            }
        });

        final Button createAccountButton = (Button) findViewById(R.id.createAccount);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(StartScreenActivity.this, CreateAccountScreenActivity.class);

                // currentContext.startActivity(activityChangeIntent);

                startActivity(activityChangeIntent);
            }
        });


        final Button continueAsGuest = (Button) findViewById(R.id.continueAsGuest);
        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create guest profile in database
                databaseReference = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("guest", "");
                databaseReference.child("userInfo").updateChildren(map);

                databaseReference.child("userInfo").child("guest").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Map<String, Object> userMap = new HashMap<String, Object>();
                        userMap.put("bookshelf", "");
                        userMap.put("goals", "");
                        databaseReference.child("userInfo").child("guest").updateChildren(userMap);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

                // Perform action on click
                Intent activityChangeIntent = new Intent(StartScreenActivity.this, BookshelfActivity.class);

                // currentContext.startActivity(activityChangeIntent);
                //activityChangeIntent.putExtra("is guest", true);
                makeGuest();
                startActivity(activityChangeIntent);
            }
        });

    }

    public void makeGuest(){
        ((Guest) this.getApplication()).setGuest(true);
    }

}
