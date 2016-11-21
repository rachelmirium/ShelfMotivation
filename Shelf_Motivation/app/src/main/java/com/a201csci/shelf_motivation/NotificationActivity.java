package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class NotificationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView notificationListView;
    private ArrayAdapter<String> arrayAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        notificationListView = (ListView) findViewById(R.id.notificationView);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference notiRef = databaseReference.child("userInfo").child(uid).child("notifications");

        notiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                makeAList(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                makeAList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //notiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //@Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<String> notifications = new ArrayList<>();
//                for (DataSnapshot ds: dataSnapshot.getChildren()) {
//                    notifications.add(ds.getKey().toString());
//                }
//                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, notifications);
//                notificationListView.setAdapter(arrayAdapter);
//
//                notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String content = (String) notificationListView.getItemAtPosition(position);
//                        if(content.contains("invitation")){
//
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void makeAList(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        ArrayList<String> notifications = new ArrayList<>();
        while(i.hasNext()){
            String sendBy = (String) ((DataSnapshot)i.next()).getValue();
            String type = (String) ((DataSnapshot)i.next()).getValue();
            String message = (String) ((DataSnapshot)i.next()).getValue();
            notifications.add(sendBy + " " +type+ " " +message);
        }
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, notifications);
        notificationListView.setAdapter(arrayAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        } else if (id == R.id.nav_bookshelf) {
            Intent intent = new Intent(this, BookshelfActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_bookclubs) {

            if ( ((Guest) this.getApplication()).getGuest()){
                Intent intent = new Intent(this, GuestError.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, BookclubOverview.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_notifications) {
            if (((Guest) this.getApplication()).getGuest()) {
                Intent intent = new Intent(this, GuestError.class);
                startActivity(intent);
            } else{
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_goals) {
            Intent intent = new Intent(this, GoalActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
