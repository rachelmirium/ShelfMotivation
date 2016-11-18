package com.a201csci.shelf_motivation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);



        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check if registered user

                //if not show error label saying wrong combination


                makeNotGuest();


            }
        });
    }


    public void makeNotGuest(){
        ((Guest) this.getApplication()).setGuest(false);
    }
}
