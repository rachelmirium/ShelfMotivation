package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageButton;

import java.util.ArrayList;


public class BookshelfActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int numberOfSavedBooks;

    private ArrayList<ImageButton> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        numberOfSavedBooks=0;

        buttons= new ArrayList<ImageButton>(9);

        buttons.add((ImageButton)(findViewById(R.id.book0)));
        buttons.add((ImageButton)(findViewById(R.id.book1)));
        buttons.add((ImageButton)(findViewById(R.id.book2)));
        buttons.add((ImageButton)(findViewById(R.id.book3)));
        buttons.add((ImageButton)(findViewById(R.id.book4)));
        buttons.add((ImageButton)(findViewById(R.id.book5)));
        buttons.add((ImageButton)(findViewById(R.id.book6)));
        buttons.add((ImageButton)(findViewById(R.id.book7)));
        buttons.add((ImageButton)(findViewById(R.id.book8)));

        fixVisibility();

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
        getMenuInflater().inflate(R.menu.bookshelf, menu);
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
            // Handle the camera action
        } else if (id == R.id.nav_bookshelf) {
            Intent intent = new Intent(this, BookshelfActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_bookclubs) {
            if ( ((Guest) this.getApplication()).getGuest()){
                Intent intent = new Intent(this, GuestError.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_notifications) {
            if ( ((Guest) this.getApplication()).getGuest()){
                Intent intent = new Intent(this, GuestError.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_goals) {
            Intent intent = new Intent(this, Goals.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings ) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newBook(){

        ImageButton button=  buttons.get(numberOfSavedBooks);
//        button.setBackgroundResource(R.drawable.cover);
//set the cover of the book to an image passed in from the API
        numberOfSavedBooks++;
        fixVisibility();
    }

    private void fixVisibility(){
        for (int i=0; i<9; i++){
            if (numberOfSavedBooks-1<i){
                buttons.get(i).setVisibility(View.INVISIBLE);
            } else{
                buttons.get(i).setVisibility(View.VISIBLE);
            }
        }
    }



}
