package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BookclubOverview extends AppCompatActivity {

    private Button createClub;
    private ListView clubListView;
    private ArrayList<String> bookclubList = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookclub_overview);

        createClub = (Button) findViewById(R.id.createClub);
        clubListView = (ListView) findViewById(R.id.clubList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bookclubList);

        createClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), BookClubSignup.class));
            }
        });



    }
}
