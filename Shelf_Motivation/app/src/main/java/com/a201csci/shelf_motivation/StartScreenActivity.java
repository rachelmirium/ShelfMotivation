package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreenActivity extends AppCompatActivity {

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
