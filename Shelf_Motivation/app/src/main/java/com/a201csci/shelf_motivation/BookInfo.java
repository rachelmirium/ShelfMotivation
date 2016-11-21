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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookInfo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AbsctractBooksAPI {

    String bookID;
    String bookURL;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Button addButton = (Button) findViewById(R.id.addToBookshelf);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            addBook();
            }
        });

        final Button recommendButton = (Button) findViewById(R.id.recommendButton);
        recommendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView tv = (TextView) findViewById(R.id.recommendBook);
                recommend(tv.getText().toString().trim());
            }
        });

        Bundle b = getIntent().getExtras();// or other values
        if(b != null) {
            String bookID = b.getString("init");
            if(bookID != null) initializeView(bookID);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

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
        getMenuInflater().inflate(R.menu.book_info, menu);
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
            //BooksAPI.getBookByID(this, "zyTCAlFPjgYC");
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

    public void addBook(){
        if(BookshelfActivity.getNumberOfSavedBooks() < 9){
            //Add book information to database if user is registered
            DatabaseReference databaseReferenceUserInfo = databaseReference.child("userInfo");
            Map<String, Object> bookMap = new HashMap<String, Object>();
            bookMap.put(bookID, bookURL);
            if (!((Guest) this.getApplication()).getGuest()) {
                String userUID = firebaseAuth.getCurrentUser().getUid();
                databaseReferenceUserInfo.child(userUID).child("bookshelf").updateChildren(bookMap);
            }
            else {
                databaseReferenceUserInfo.child("guest").child("bookshelf").updateChildren(bookMap);
            }

            // Change intent
            Intent activityChangeIntent = new Intent(BookInfo.this, BookshelfActivity.class);
            activityChangeIntent.putExtra("add", bookID);
            activityChangeIntent.putExtra("URL", bookURL);
            startActivity(activityChangeIntent);
        }
    }

    public void initializeView(String bookID){
        this.bookID = bookID;
        BooksAPI.getBookByID(this, bookID, this);
    }

    public void recommend(final String username){
        //send bookID to user
        databaseReference.child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean foundUser = false;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String emailString = (String) dataSnapshot.child("email").getValue();
                    if (username.equals(firebaseAuth.getCurrentUser().getEmail())) {
                        Toast.makeText(BookInfo.this, "Cannot recommend to self", Toast.LENGTH_SHORT).show();
                        foundUser = true;
                        break;
                    }
                    else if (username.equals(emailString)) {
                        foundUser = true;

                        // Send recommendation notification

                        Toast.makeText(BookInfo.this, "Recommendation sent!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), BookshelfActivity.class));
                    }
                }
                if (!foundUser) {
                    Toast.makeText(BookInfo.this, "Unable to find user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void gotBookByID(Book book) {
        bookURL = book.getImageURL();
        ImageView i = (ImageView) findViewById(R.id.bookImage);
        Picasso.with(this).load(book.getImageURL()).into(i);

        TextView title = (TextView) findViewById(R.id.bookTitle);
        title.setText(book.getTitle());

        TextView author = (TextView) findViewById(R.id.bookAuthor);
        author.setText(book.getAuthors().get(0));
    }

    @Override
    public void gotAllBooks(ArrayList<Book> books) {

    }

    @Override
    public void gotError() {

    }
}
