package com.a201csci.shelf_motivation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
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

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = new TextView(getApplicationContext());
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(lparams);
                textView.setText(inputUsername.getText().toString());
                showAllInvitedUsers.addView(textView);
                invitedUserList.add(inputUsername.getText().toString());
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
                map.put(bookclubName, host);
                databaseReference.child("bookclubs").updateChildren(map);

                Map<String, Object> memberList = new HashMap<String, Object>();
                memberList.put("members", invitedUserList);
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(memberList);

                Map<String, Object> description = new HashMap<String, Object>();
                description.put("description", des);
                databaseReference.child("bookclubs").child(bookclubName).updateChildren(description);

            }
        });


    }
}
