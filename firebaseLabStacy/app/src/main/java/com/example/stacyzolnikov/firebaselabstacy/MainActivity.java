package com.example.stacyzolnikov.firebaselabstacy;

import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String mUserName;
    String mChosenName;
    ListView mMessageList;
    EditText mEditTextMessage;
    Button mSendButton, mRandomUser, mChooseUser, mClearChat;
    DatabaseReference mRef;
    TextView mCurrentText;
    private FirebaseListAdapter<Messages> firebaseListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome to the chat room");
        //Randomly Creating a username
        mRandomUser = (Button) findViewById(R.id.randomUser);
        mRandomUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName = "User" + ((int) (Math.random() * 5000));
                getSupportActionBar().setTitle("Chatting as " + mUserName);
            }
        });




                mChooseUser = (Button) findViewById(R.id.chooseUser);
        mChooseUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialogShow();
                getSupportActionBar().setTitle("Chatting as " + mUserName);
            }
        });


        mMessageList = (ListView) findViewById(R.id.messageList);
        mEditTextMessage = (EditText) findViewById(R.id.userMessageInput);
        mSendButton = (Button) findViewById(R.id.buttonSend);
        mRef = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference messages = mRef.child("messages");

        //PULLING down data from firebase using ChildEventListener

        mRef.child("users").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String user = dataSnapshot.getValue(String.class);
                mUserName = user;
                getSupportActionBar().setTitle("Chatting as " + mUserName);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


         final FirebaseListAdapter<String> firebaseAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, messages) {
            @Override
            protected void populateView(View view, String model, int position) {
                TextView message = (TextView) view.findViewById(android.R.id.text1);
                message.setText(model);
            }
        };
        mMessageList.setAdapter(firebaseAdapter);
        mMessageList.setAdapter(firebaseAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                messages.push().setValue(mUserName + " : " + mEditTextMessage.getText().toString());
                mEditTextMessage.setText("");
            }
        });
        //Haven't implemented anything
        mClearChat = (Button) findViewById(R.id.clearChat);

    }


    final void dialogShow() {
        //Alert dialog to type in username
        final DatabaseReference users = mRef.child("users");
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "Welcome: " + value,
                        Toast.LENGTH_SHORT).show();
                users.push().setValue(value);


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }


}
