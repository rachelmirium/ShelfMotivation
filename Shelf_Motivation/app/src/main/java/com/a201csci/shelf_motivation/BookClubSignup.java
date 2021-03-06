package com.a201csci.shelf_motivation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookClubSignup extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private Button inviteButton;
    private EditText inputUsername;
    private LinearLayout showAllInvitedUsers;
    private Button create;
    private ArrayList<String> invitedUserList = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_club_signup);


        name = (EditText) findViewById(R.id.editText2);
        description = (EditText) findViewById(R.id.editText3) ;
        inviteButton = (Button) findViewById(R.id.invite);
        inputUsername = (EditText) findViewById(R.id.invitedUser);
        showAllInvitedUsers = (LinearLayout) findViewById(R.id.showInvited);
        create = (Button) findViewById(R.id.button3);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        invitedUserList.add(firebaseAuth.getCurrentUser().getEmail());

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = new TextView(getApplicationContext());
                final String invitedUser = inputUsername.getText().toString().trim();

                // Check if user exists, add to invited list if found
                    databaseReference.child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.e("Count: " ,""+snapshot.getChildrenCount());
                            boolean foundUser = false;
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                String emailString = (String) dataSnapshot.child("email").getValue();
                                if (invitedUser.equals(firebaseAuth.getCurrentUser().getEmail())) {
                                    Toast.makeText(BookClubSignup.this, "Cannot add self to book club", Toast.LENGTH_SHORT).show();
                                    foundUser = true;
                                    break;
                                }
                                else if (invitedUserList.contains(invitedUser)) {
                                    Toast.makeText(BookClubSignup.this, "User already in book club", Toast.LENGTH_SHORT).show();
                                    foundUser = true;
                                    break;
                                }
                                else if (invitedUser.equals(emailString)) {
                                    invitedUserList.add(invitedUser);
                                    foundUser = true;

                                    // Update user's profile in db
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(name.getText().toString(), "");
                                    String uid = dataSnapshot.getKey();
                                    databaseReference.child("userInfo").child(uid).child("bookclubs").updateChildren(map);

                                    //update notifications on this user
                                    DatabaseReference notiRef = databaseReference.child("userInfo").child(uid).child("notifications");
                                    Map<String, Object> map2 = new HashMap<String, Object>();
                                    String temp_Key = notiRef.push().getKey();
                                    notiRef.updateChildren(map2);
                                    DatabaseReference eachNotifRef = notiRef.child(temp_Key);
                                    map2.put("sendBy", firebaseAuth.getCurrentUser().getEmail());
                                    map2.put("type", "invitation");
                                    map2.put("message", name.getText().toString());
                                    eachNotifRef.updateChildren(map2);


                                    // Show list on screen
                                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    textView.setLayoutParams(lparams);
                                    textView.setText(invitedUser);
                                    showAllInvitedUsers.addView(textView);
                                    break;
                                }

                            }
                            if (!foundUser) {
                                Toast.makeText(BookClubSignup.this, "Unable to find user", Toast.LENGTH_SHORT).show();
                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookclubName = name.getText().toString();
                String des = description.getText().toString();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String host = user.getEmail();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(bookclubName, "");
                databaseReference.child("bookclubs").updateChildren(map);

                Map<String, Object> host1 = new HashMap<String, Object>();
                host1.put("host", host);
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(host1);

                Calendar cal = Calendar.getInstance(Locale.US);
                Map<String, Object> timeCreate = new HashMap<String, Object>();
                timeCreate.put("Created", cal.getTime().toString());
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(timeCreate);

                Map<String, Object> memberList = new HashMap<String, Object>();
                memberList.put("members", invitedUserList);
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(memberList);

                Map<String, Object> description = new HashMap<String, Object>();
                description.put("description", des);
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(description);

                Map<String, Object> chatroom = new HashMap<String, Object>();
                chatroom.put("chatroom", "");
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(chatroom);

//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                FirebaseUser currUser = firebaseAuth.getCurrentUser();
                Map<String, Object> bookclubInUser = new HashMap<String, Object>();
                bookclubInUser.put(bookclubName, "");
                databaseReference.child("userInfo").child(user.getUid()).child("bookclubs").updateChildren(bookclubInUser);


                Intent intent = new Intent(getApplicationContext(), BookclubActivity.class);
                intent.putExtra("bookclub_name", bookclubName);
                startActivity(intent);

            }
        });


    }
}
