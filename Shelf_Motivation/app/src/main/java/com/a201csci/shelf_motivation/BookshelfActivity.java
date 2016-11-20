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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BookshelfActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AbsctractBooksAPI {


    private ArrayList<ImageButton> buttons;
    private ArrayList<String> bookIDs;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<bookData> bookDataDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((Guest) this.getApplication()).setNumberOfBooks(0);

        buttons = new ArrayList<ImageButton>(9);

        bookIDs = new ArrayList<String>(9);

        bookDataDB = new ArrayList<bookData>();

        buttons.add((ImageButton)(findViewById(R.id.book0)));
        buttons.add((ImageButton)(findViewById(R.id.book1)));
        buttons.add((ImageButton)(findViewById(R.id.book2)));
        buttons.add((ImageButton)(findViewById(R.id.book3)));
        buttons.add((ImageButton)(findViewById(R.id.book4)));
        buttons.add((ImageButton)(findViewById(R.id.book5)));
        buttons.add((ImageButton)(findViewById(R.id.book6)));
        buttons.add((ImageButton)(findViewById(R.id.book7)));
        buttons.add((ImageButton)(findViewById(R.id.book8)));


        Bundle b = getIntent().getExtras();
        if (b!= null){
            final String bookID= b.getString("add");
            final String URL = b.getString("URL");
            if (bookID!=null){
                bookIDs.add(bookID);

                // Add book to user's database and load shelf
//                if (!((Guest) this.getApplication()).getGuest()) {
//                    bookDataDB.add(new bookData(bookID, URL));
//
//                    firebaseAuth = FirebaseAuth.getInstance();
//                    String userUID = firebaseAuth.getCurrentUser().getUid();
//
//                    databaseReference = FirebaseDatabase.getInstance().getReference();
//                    DatabaseReference databaseReferenceBookshelf = databaseReference.child("userInfo").child(userUID).child("bookshelf");
//                    databaseReferenceBookshelf.setValue(bookDataDB);
//
//                    databaseReferenceBookshelf.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot snapshot) {
//                            Log.e("Count: " ,""+snapshot.getChildrenCount());
//                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                                String bookID = dataSnapshot.child("id").getValue().toString();
//                                String bookURL = dataSnapshot.child("url").getValue().toString();
//                                newBook(bookID, bookURL);
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) { }
//                    });
//                }


                ImageButton imageButton= buttons.get(Integer.parseInt(bookID));
                final String id= bookIDs.get(Integer.parseInt(bookID));
                imageButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Intent activityChangeIntent= new Intent (BookshelfActivity.this, BookInfo.class);
                        activityChangeIntent.putExtra("init", id);
                        startActivity(activityChangeIntent);
                    }
                });
            }
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
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
            BooksAPI.getBookByID(this, "zyTCAlFPjgYC", this);
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





    public void newBook(String bookID, String URL){
        ImageButton button = buttons.get(((Guest) this.getApplication()).getNumberOfBooks());
        Picasso.with(this).load(URL).into(button);
        //bookIDs.set(numberOfSavedBooks, bookID);
        ((Guest) this.getApplication()).setNumberOfBooks(((Guest) this.getApplication()).getNumberOfBooks()+1);
        fixVisibility();


    }

    private void fixVisibility() {
        for (int i = 0; i < 9; i++) {
            if ( ((Guest) this.getApplication()).getNumberOfBooks() - 1 < i) {
                buttons.get(i).setVisibility(View.INVISIBLE);
            } else {
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


}

