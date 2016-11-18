package com.a201csci.shelf_motivation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check if taken username

                // if yes then error message


                makeNotGuest();


            }
        });


    }


    public void makeNotGuest(){
        ((Guest) this.getApplication()).setGuest(false);
    }
}
