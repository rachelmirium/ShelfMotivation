package com.a201csci.shelf_motivation;
//working
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Goals extends AppCompatActivity {

    Vector<CheckBox> goals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final LinearLayout mLayout = (LinearLayout) findViewById(R.id.goalsCheckboxes);

        goals = new Vector<CheckBox>();
        Cleaner cl = new Cleaner(goals, mLayout);

        Log.i("Goals_Activity","App Running");
        Button button1 = (Button) findViewById(R.id.goalsAddButton);


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

    }

    private CheckBox createNewCheckBox(){
        String bookTitle = ((EditText)findViewById(R.id.goalsBookTitle)).getText().toString();
        String dateTitle = ((EditText)findViewById(R.id.goalsDateTitle)).getText().toString();
        if(bookTitle.isEmpty() || dateTitle.isEmpty()){
            ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Required fields cannot be left empty.");
            return null;
        }
//        if(userLibrary.bookExists(bookttitle)) {
            GoalCheckBox checkBox = new GoalCheckBox(bookTitle, dateTitle, this);
            return checkBox.getCheckBox();
//        }
//        else{
//            ((TextView) findViewById(R.id.goalsAlertLabel)).setText("Book not found in your bookshelf.");
//            return null;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goals, menu);
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

    public class GoalCheckBox extends Thread {
        String bookTitle;
        String timeLeft;
        CheckBox checkBox;
        Date targetDate;
        Calendar cal;
        GoalCheckBox(String bookTitle, String time, Goals gg){
            this.bookTitle = bookTitle;
            DateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            try {
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

                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);

                        checkBox.setText(bookTitle+"   "+
                                Long.toString(diffDays)+"Days "+
                                Long.toString(diffHours)+":"+
                                Long.toString(diffMinutes)+":"
                                +Long.toString(diffSeconds));
                        Log.i("checkbox",Long.toString(diff));
                    }
                });
                try {
                    Thread.sleep(5000);
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

