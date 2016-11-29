package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.api.client.util.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookshelfActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AbsctractBooksAPI {


    private ArrayList<ImageButton> buttons;
    private ArrayList<String> bookIDs = new ArrayList<String>(9);
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    static int numberOfSavedBooks = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((Guest) this.getApplication()).setNumberOfBooks(0);

        buttons = new ArrayList<ImageButton>(9);

        buttons.add((ImageButton)(findViewById(R.id.book0)));
        buttons.add((ImageButton)(findViewById(R.id.book1)));
        buttons.add((ImageButton)(findViewById(R.id.book2)));
        buttons.add((ImageButton)(findViewById(R.id.book3)));
        buttons.add((ImageButton)(findViewById(R.id.book4)));
        buttons.add((ImageButton)(findViewById(R.id.book5)));
        buttons.add((ImageButton)(findViewById(R.id.book6)));
        buttons.add((ImageButton)(findViewById(R.id.book7)));
        buttons.add((ImageButton)(findViewById(R.id.book8)));

        // Check db to see if we need to pre-populate bookshelf
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReferenceShelf;
        // Get correct database reference
        if (!((Guest) this.getApplication()).getGuest()) {
            firebaseAuth = FirebaseAuth.getInstance();
            String userUID = firebaseAuth.getCurrentUser().getUid();
            databaseReferenceShelf = databaseReference.child("userInfo").child(userUID).child("bookshelf");
        }
        else {
            databaseReferenceShelf = databaseReference.child("userInfo").child("guest").child("bookshelf");
        }
        // Populate shelf
        databaseReferenceShelf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                numberOfSavedBooks = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if(numberOfSavedBooks < 9) {
                        final String bookID = dataSnapshot.getKey();
                        final String bookURL = dataSnapshot.getValue().toString();
                        Log.e("BOOKSHELF", "" + numberOfSavedBooks);
                        ImageButton imageButton = buttons.get(numberOfSavedBooks);
                        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                        Picasso.with(BookshelfActivity.this).load(bookURL).into(imageButton);
                        bookIDs.add(bookID);
                        numberOfSavedBooks++;

                        imageButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                Intent activityChangeIntent = new Intent(BookshelfActivity.this, BookInfo.class);
                                activityChangeIntent.putExtra("init", bookID);
                                startActivity(activityChangeIntent);
                            }
                        });
                    }
                }
                fixVisibility();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        Bundle b = getIntent().getExtras();
        if (b!= null){
            final String bookID= b.getString("add");
            final String URL = b.getString("URL");
            if (bookID!=null && numberOfSavedBooks < 9){
                bookIDs.add(bookID);

                ImageButton imageButton= buttons.get(numberOfSavedBooks);
                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(this).load(URL).into(imageButton);
                imageButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Intent activityChangeIntent= new Intent (BookshelfActivity.this, BookInfo.class);
                        activityChangeIntent.putExtra("init", bookID);
                        startActivity(activityChangeIntent);
                    }
                });
                fixVisibility();
                numberOfSavedBooks++;
            }
            /*final String removeIndex = b.getString("remove");
            if(removeIndex != null){
                numberOfSavedBooks--;
                ImageButton imageButton= buttons.get(numberOfSavedBooks);
                imageButton.set;
            }*/
        }

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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    /*public void newBook(String bookID, String URL){
        ImageButton button = buttons.get(((Guest) this.getApplication()).getNumberOfBooks());
        Picasso.with(this).load(URL).into(button);
        //bookIDs.set(numberOfSavedBooks, bookID);
        ((Guest) this.getApplication()).setNumberOfBooks(((Guest) this.getApplication()).getNumberOfBooks()+1);
        fixVisibility();
    }*/

    private void fixVisibility() {
        for (int i = 0; i < 9; i++) {
            if (numberOfSavedBooks <= i) {
                Log.e("VISIBILITY", "Setting invisible");
                buttons.get(i).setVisibility(View.INVISIBLE);
            } else {
                Log.e("VISIBILITY", "Setting visible");
                buttons.get(i).setVisibility(View.VISIBLE);

            }
        }
    }


    public void gotBookByID(Book book){
    }
    public void gotAllBooks(ArrayList<Book> books){

    }
    public void gotError(){

    }

    public class bookData {

        public String id;
        public String url;

        public bookData(String id, String url) {
            this.id = id;
            this.url = url;
        }
    }

    public static int getNumberOfSavedBooks(){
        return numberOfSavedBooks;
    }


}

