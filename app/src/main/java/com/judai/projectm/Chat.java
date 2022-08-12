package com.judai.projectm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    EditText ed1;
    ImageButton gui,back;
    ListView lv;
    ArrayList<String> listdata;
    ArrayAdapter<String> adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mapping();
        ShowMessenge();
        ActionButton();
    }

    private void mapping(){
        context = this;
        ed1 = (EditText) findViewById(R.id.ed);
        gui = findViewById(R.id.gui);
        lv = (ListView) findViewById(R.id.lv);
        back =  findViewById(R.id.bk);
        Intent intent = getIntent();
        listdata = new ArrayList<>();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,listdata);
    }

    private void ShowMessenge(){
        MainActivity.myRef.child("Room/"+RoomChat.ROOM+"/chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String d = snapshot.getValue().toString();
                listdata.add(d);
                lv.setAdapter(adapter);
                //Hien thi cuoi danh sach
                lv.post(new Runnable() {
                    @Override
                    public void run() {
                        lv.setSelection(lv.getCount()-1);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ActionButton(){
       gui.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String c = ed1.getText().toString();
               if(c.equals("")==true)

               {   MainActivity.myRef.child("Room/"+RoomChat.ROOM+"/chat").push().setValue(MainActivity.FullName + " : \uD83D\uDC4D" );
                   ed1.setText("");
               }
               else {
                   MainActivity.myRef.child("Room/"+RoomChat.ROOM+"/chat").push().setValue(MainActivity.FullName + " : " + c );
                   adapter.notifyDataSetChanged();
                   ed1.setText("");
               }
           }
       });
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Chat.this,RoomChat.class);
               startActivity(intent);
           }
       });
    }
}