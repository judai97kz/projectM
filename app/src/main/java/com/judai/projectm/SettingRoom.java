package com.judai.projectm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SettingRoom extends AppCompatActivity {
    ImageButton tback,addbt,delbt;
    ListView lvmb;

    ArrayList<String> listmember;
    ArrayAdapter<String> adaptermember;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_room);
        Mapping();
        ActionBT();
        ShowMember();

    }

    private void Mapping(){
        tback = findViewById(R.id.btr);
        addbt = findViewById(R.id.addfr);
        delbt = findViewById(R.id.delbox);
        lvmb = findViewById(R.id.lvmb);
        context=this;
    }

    private void ActionBT(){
        tback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(SettingRoom.this,Chat.class);
                startActivity(intent4);
            }
        });
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        delbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void ShowMember(){
        listmember = new ArrayList<>();
        MainActivity.myRef.child("Room/"+RoomChat.ROOM+"/member").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String member = snapshot.getValue().toString().trim();
                listmember.add(member);
                Set<String> set = new HashSet<String>(listmember);
                List<String> xoatrung = new ArrayList<String>(set);
                adaptermember = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,xoatrung);
                lvmb.setAdapter(adaptermember);
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
}