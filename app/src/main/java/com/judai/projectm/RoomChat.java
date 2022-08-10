package com.judai.projectm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomChat extends AppCompatActivity {
    private ImageButton btmenu;
    ArrayList<String> listdata1;
    ArrayAdapter<String> adapter1;
    Context context;
    ListView lv;

    public static final String LOG_TAG =  "PopupMenuExample";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);
        Toast.makeText(this,MainActivity.FullName,Toast.LENGTH_SHORT).show();
        btmenu = findViewById(R.id.buttonmenu);
        btmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1Clicked();
            }
        });
        context = this;
        String nameuser = MainActivity.Username;
        lv = findViewById(R.id.lv);
        listdata1 = new ArrayList<>();
        MainActivity.myRef.child("Data/"+ nameuser +"/join").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String d = snapshot.getValue().toString();
                listdata1.add(d);
                Set<String> set = new HashSet<String>(listdata1);
                List<String> xoatrung = new ArrayList<String>(set);
                adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,xoatrung);
                lv.setAdapter(adapter1);
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

    private void button1Clicked( )  {
        // When user click on the Button 1, create a PopupMenu.
        // And anchor Popup to the Button 2.
        PopupMenu popup = new PopupMenu(this, this.btmenu);
        popup.inflate(R.menu.option_menu);

        Menu menu = popup.getMenu();
        // com.android.internal.view.menu.MenuBuilder
        Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

        // Register Menu Item Click event.
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemClicked(item);
            }
        });

        // Show the PopupMenu.
        popup.show();
    }

    private boolean menuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editprofile:
                Toast.makeText(this, "Bookmark", Toast.LENGTH_SHORT).show();
                break;
            case R.id.createroom:
                OpenDialog();
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to log out of this account?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RoomChat.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void OpenDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder check = new AlertDialog.Builder(this);
        View alert = LayoutInflater.from(this).inflate(R.layout.createroom,null);
        builder.setView(alert);
        EditText edtid,edtpass;
        Button btncr,btncc;
        edtid = alert.findViewById(R.id.idroom);
        edtpass = alert.findViewById(R.id.passroom);
        btncr = alert.findViewById(R.id.btcreate);
        btncc = alert.findViewById(R.id.btcancel);
        btncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.setMessage("Do you want to create this room?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tp = edtid.getText().toString();
                        String mp = edtpass.getText().toString().trim();
                        if(tp.equals("")==true)
                        {
                            Toast.makeText(RoomChat.this,"Hãy Nhập ID phòng Để Tạo !",Toast.LENGTH_SHORT).show();
                        }else
                        {
                            MainActivity.myRef.child("Room/"+tp+"/id").setValue(tp);
                            MainActivity.myRef.child("Room/"+tp+"/pass").setValue(mp);
                            MainActivity.myRef.child("Room/"+tp+"/chat").setValue("");
                            MainActivity.myRef.child("Room/"+tp+"/roommaster").setValue(MainActivity.Username.toString().trim());
                            MainActivity.myRef.child("Data/"+MainActivity.Username.toString().trim()+"/join").push().setValue(tp);
                            Intent i2 = new Intent(RoomChat.this,Chat.class);
                            startActivity(i2);
                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                check.create().show();
            }
        });
        final AlertDialog ad =  builder.show();
        btncc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}