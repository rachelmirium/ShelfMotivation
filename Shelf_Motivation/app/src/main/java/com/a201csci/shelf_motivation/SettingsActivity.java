package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        if ( ((Guest) this.getApplication()).getGuest()){
            View button= findViewById(R.id.logout);
            button.setVisibility(View.GONE);
        }else{
            View button= findViewById(R.id.createaccount);
            button.setVisibility(View.GONE);
        }

        final Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

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
}
