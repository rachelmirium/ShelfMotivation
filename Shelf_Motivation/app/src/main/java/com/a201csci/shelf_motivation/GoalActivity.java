package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.util.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GoalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    Vector<CheckBox> goals;
    ArrayList<goal> goalsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final LinearLayout mLayout = (LinearLayout) findViewById(R.id.goalsCheckboxes);

        goals = new Vector<CheckBox>();
        goalsDB = new ArrayList<goal>();
        GoalActivity.Cleaner cl = new GoalActivity.Cleaner(goals, mLayout);

        Log.i("Goals_Activity","App Running");
        Button button1 = (Button) findViewById(R.id.goalsAddButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();




        // Listeners for interaction with goal interface
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
            final CheckBox cb = createNewCheckBox();
            if(cb!=null) {
                ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Goal Added!");
                mLayout.addView(cb);
                goals.add(cb);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        cb.setText("Goal Accomplished!");
                    }});
            }
            else{
                Log.i("error","Could not parse date");
            }
            }
        });

        // Get reference to user's goals
        DatabaseReference databaseReferenceUserGoals = databaseReference.child("userInfo");
        if (!((Guest) this.getApplication()).getGuest()) {
            String userUID = firebaseAuth.getCurrentUser().getUid();
            databaseReferenceUserGoals = databaseReferenceUserGoals.child(userUID).child("goals");
        }
        else {
            // use guest account
            databaseReferenceUserGoals = databaseReferenceUserGoals.child("guest").child("goals");
        }

        // Grab user's existing goals
        databaseReferenceUserGoals.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count: " ,""+snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String bookTitleString = (String) dataSnapshot.child("bookTitle").getValue();
                    String dateTitleString = (String) dataSnapshot.child("goalDate").getValue();
                    goal mGoal = new goal(bookTitleString, dateTitleString);
                    Log.e("Title", mGoal.getBookTitle());
                    Log.e("Date", mGoal.getGoalDate());
                    goalsDB.add(mGoal);

                    final GoalActivity.GoalCheckBox checkBox = new GoalActivity.GoalCheckBox(bookTitleString, dateTitleString, GoalActivity.this);
                    mLayout.addView(checkBox.getCheckBox());
                    goals.add(checkBox.getCheckBox());
                    checkBox.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            checkBox.getCheckBox().setText("Goal Accomplished!");
                            checkBox.getCheckBox().setEnabled(false);

                            // Remove goal from goals and goalsDB data structures
                            for (CheckBox cb : goals) {
                                // Not sure how to do this
                            }
//                            for (goal g : goalsDB) {
//                                if (g.getBookTitle().equals(checkBox.bookTitle)) {
//                                    goalsDB.remove(new goal(g.getBookTitle(), g.getGoalDate()));
//                                    Log.e("GOALS", "removed from goalsDB");
//                                }
//                            }
//
//
//
//                            // Delete goal from database
//                            final String userID = firebaseAuth.getCurrentUser().getUid();
//
//                            databaseReference.child("userInfo").child(userID).child("goals").addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot snapshot) {
//                                    long numGoals = snapshot.getChildrenCount();
//                                    Log.e("GOAL", ""+numGoals);
//                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                                        // Check if correct goal entry
//                                        Log.e("GOAL", ""+dataSnapshot.child("bookTitle").getValue());
//                                        if(dataSnapshot.child("bookTitle").getValue().equals(checkBox.bookTitle)) {
//                                            Log.e("GOAL", "Found "+checkBox.bookTitle);
//                                            dataSnapshot.getRef().removeValue();
//                                            if (numGoals == 1) {
//                                                Map<String, Object> userMap = new HashMap<String, Object>();
//                                                userMap.put("goals", "");
//                                                databaseReference.child("userInfo").child(userID).updateChildren(userMap);
//                                            }
//                                            break;
//
//                                        }
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) { }
//                            });

                        }});
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


    }

    private CheckBox createNewCheckBox(){
        String bookTitle = ((EditText)findViewById(R.id.goalsBookTitle)).getText().toString();
        String dateTitle = ((EditText)findViewById(R.id.goalsDateTitle)).getText().toString();
        if(bookTitle.isEmpty() || dateTitle.isEmpty()){
            ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Required fields cannot be left empty.");
            return null;
        }

        // Get reference to user's goals in database
        goal newGoal = new goal(bookTitle, dateTitle);
        goalsDB.add(newGoal);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReferenceUser = databaseReference.child("userInfo");

        if (!((Guest) this.getApplication()).getGuest()) {
            firebaseAuth = FirebaseAuth.getInstance();
            String userUID = firebaseAuth.getCurrentUser().getUid();
            databaseReferenceUser = databaseReferenceUser.child(userUID);
        }
        else {
            databaseReferenceUser = databaseReferenceUser.child("guest");
        }

        // Check to make sure entered book title is in the user's bookshelf
        databaseReferenceUser.child("bookshelf").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("GOAL", ""+ dataSnapshot);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Make api call to get book title from bookID (ds.getKey
                    // check if ds.getKey equals the book title entered by the user
                    // if equals, add goal to user's db
                }

                // otherwise did not find book, do not add goal to user's db and display error toast
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReferenceUser.child("goals").setValue(goalsDB);

        // Create checkbox
        GoalActivity.GoalCheckBox checkBox = new GoalActivity.GoalCheckBox(bookTitle, dateTitle, this);
        return checkBox.getCheckBox();

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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
        }  else if (id == R.id.nav_bookshelf) {
            Intent intent = new Intent(this, BookshelfActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bookclubs) {
            if ( ((Guest) this.getApplication()).getGuest()){
                Intent intent = new Intent(this, ErrorActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, bookclubsOverviewActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_notifications) {
            if (((Guest) this.getApplication()).getGuest()) {
                Intent intent = new Intent(this, ErrorActivity.class);
                startActivity(intent);
            } else{
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_goals) {
            Intent intent = new Intent(this, GoalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class GoalCheckBox extends Thread {
        String bookTitle;
        String timeLeft;
        CheckBox checkBox;
        Date targetDate;
        Calendar cal;
        GoalCheckBox(String bookTitle, String time, GoalActivity gg){
            this.bookTitle = bookTitle;
            DateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            try {
                sdf.setLenient(false);
                targetDate = sdf.parse(time);
                cal = Calendar.getInstance();
                cal.setTime(targetDate);
                long diff= cal.getTimeInMillis() - (new Date()).getTime();
                if(diff<0){
                    checkBox = null;
                    ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Date has already passed");
                }else {
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    checkBox = new CheckBox(gg);
                    checkBox.setLayoutParams(lparams);
                    this.start();
                }
            } catch (ParseException e) {
                ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Invalid Date");
                checkBox = null;
                e.printStackTrace();
            }
        }
        CheckBox getCheckBox(){
            return checkBox;
        }
        @Override
        public void run() {
            super.run();
            while(true){
                if(checkBox.isChecked())
                    break;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long diff= cal.getTimeInMillis() - (new Date()).getTime();
                        long round = diff%1000;
                        diff-=round;
                        if(diff<=0){
                            checkBox.setText(bookTitle+"Date Passed!");
                            checkBox.setTextColor(getResources().getColor(R.color.error));
                        }
                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);

                        checkBox.setText(bookTitle+"   "+
                                Long.toString(diffDays)+" Days "+
                                (diffHours<10?"0":"")+
                                Long.toString(diffHours)+":"+
                                (diffMinutes<10?"0":"")+
                                Long.toString(diffMinutes)+":"+
                                (diffSeconds<10?"0":"")+
                                Long.toString(diffSeconds));
                        Log.i("checkbox",Long.toString(diff));
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    public class Cleaner extends Thread{
        Vector<CheckBox> goals;
        LinearLayout mLayout;
        Cleaner(Vector<CheckBox> goals, LinearLayout mLayout) {
            this.goals = goals;
            this.mLayout = mLayout;
            this.start();
        }
        @Override
        public void run() {
            super.run();
            while(true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (CheckBox c : goals) {
                            if (c.isChecked())
                                mLayout.removeView(c);
                        }
                    }
                });
            }
        }
    }

}
