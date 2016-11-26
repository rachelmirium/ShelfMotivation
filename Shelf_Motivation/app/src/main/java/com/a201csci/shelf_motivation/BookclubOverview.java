package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookclubOverview extends AppCompatActivity {

    private Button createClub;
    private ListView clubListView;
    private ArrayList<String> bookclubList = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookclub_overview);
        createClub = (Button) findViewById(R.id.createClub);
        clubListView = (ListView) findViewById(R.id.clubList);

        createClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), BookClubSignup.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference bookclubsRef = databaseReference.child("userInfo").child(currUser.getUid()).child("bookclubs");

        bookclubsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    names.add(ds.getKey().toString());
                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
                clubListView.setAdapter(arrayAdapter);

                clubListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String  name    = (String) clubListView.getItemAtPosition(position);
                        Intent intent = new Intent(getApplicationContext(), BookclubActivity.class);
                        intent.putExtra("bookclub_name", name);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



    }

}
