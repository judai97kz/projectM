package com.judai.projectm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RoomChat extends AppCompatActivity {
    private ImageButton btmenu;
    ArrayList<String> listdata1;
    ArrayAdapter<String> adapter1;
    Context context;
    ListView lv;
    String nameuser = MainActivity.Username;
    public static int keycheck =0;
    public static final String LOG_TAG =  "PopupMenuExample";
    private List<Suggestion> mSuggestions =new ArrayList<>();
    public static String ROOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);
        btmenu = findViewById(R.id.buttonmenu);
        btmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1Clicked();
            }
        });
        context = this;
        lv = findViewById(R.id.lv);
        listdata1 = new ArrayList<>();
        ShowDatalistRoom();
        ActionButton();
        initData();
        SearchAction();

    }

    private void ShowDatalistRoom(){
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
                listdata1.clear();
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
                        MainActivity.check =0;
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
                            MainActivity.myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("Room/"+tp)){
                                        Toast.makeText(RoomChat.this, "The room is already taken!", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        MainActivity.myRef.child("Room/"+tp+"/id").setValue(tp);
                                        MainActivity.myRef.child("Room/"+tp+"/pass").setValue(mp);
                                        MainActivity.myRef.child("Room/"+tp+"/chat").setValue("");
                                        MainActivity.myRef.child("Room/"+tp+"/roommaster").setValue(MainActivity.Username.toString().trim());
                                        MainActivity.myRef.child("Data/"+MainActivity.Username.toString().trim()+"/join").push().setValue(tp);
                                        ROOM = tp;
                                        Intent i2 = new Intent(RoomChat.this,Chat.class);
                                        startActivity(i2);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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

    public void ActionButton(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = adapter1.getItem(position).toString().trim();
                ROOM=a;
                Intent i2 = new Intent(RoomChat.this,Chat.class);
                startActivity(i2);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String a = adapter1.getItem(position);
                MainActivity.myRef.child("Data/"+ nameuser +"/join").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsnapshot:dataSnapshot.getChildren())
                        {
                            String key= dsnapshot.getKey();
                            String value=dsnapshot.getValue().toString();
                            if(a.equals("Phong chung")==true)
                            {
                                Toast.makeText(RoomChat.this,"The room can't delete!",Toast.LENGTH_SHORT).show();
                            }else {
                                if (value.equals(a)) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(RoomChat.this);
                                    dialog.setMessage("Do you want to delete this room?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.myRef.child("Data/" + nameuser + "/join").child(key).removeValue();
                                            Intent reload = new Intent(RoomChat.this, RoomChat.class);
                                            startActivity(reload);
                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.create().show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(RoomChat.this,databaseError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private void SearchAction(){
        final FloatingSearchView searchView= (FloatingSearchView) findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    searchView.swapSuggestions(getSuggestion(newQuery));
                    searchView.hideProgress();
                }
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                searchView.swapSuggestions(getSuggestion(searchView.getQuery()));
                searchView.hideProgress();
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                int checkroom =0;
                Suggestion suggestion= (Suggestion) searchSuggestion;
                for(int i=0;i<listdata1.size();i++){

                    if(adapter1.getItem(i).toString().toUpperCase(Locale.ROOT).equals(suggestion.getmName().toUpperCase())==true){
                        Toast.makeText(RoomChat.this,adapter1.getItem(i).toString(),Toast.LENGTH_SHORT).show();
                        ROOM=suggestion.getmName().toString().trim();
                        Intent i2 = new Intent(RoomChat.this,Chat.class);
                        startActivity(i2);
                        checkroom=0;
                        break;
                    }
                    else{
                        checkroom ++;
                    }
                }
                if(checkroom!=0)
                {
                    AlertDialog.Builder inroom = new AlertDialog.Builder(RoomChat.this);
                    View alert1 = LayoutInflater.from(RoomChat.this).inflate(R.layout.in_room,null);
                    inroom.setView(alert1);
                    TextView tvir = alert1.findViewById(R.id.tvir);
                    EditText edir = alert1.findViewById(R.id.etir);
                    Button okir = alert1.findViewById(R.id.okir);
                    Button ccir = alert1.findViewById(R.id.cancelir);
                    tvir.setText("Do you want to join room "+ suggestion.getmName() +"?");
                    okir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.myRef.child("Room/"+suggestion.getmName().toString().trim()+"/pass").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String cpass = snapshot.getValue().toString().trim();
                                    Toast.makeText(RoomChat.this,cpass,Toast.LENGTH_SHORT).show();
                                    if(cpass.equals("")==true){
                                        ROOM=suggestion.getmName().toString().trim();
                                        MainActivity.myRef.child("Data/"+MainActivity.Username.toString().trim()+"/join").push().setValue(suggestion.getmName().toString().trim());
                                        Intent i2 = new Intent(RoomChat.this,Chat.class);
                                        startActivity(i2);
                                    }
                                    else{
                                        if(edir.getText().toString().equals(cpass)==true){
                                            ROOM=suggestion.getmName().toString().trim();
                                            MainActivity.myRef.child("Data/"+MainActivity.Username.toString().trim()+"/join").push().setValue(suggestion.getmName().toString().trim());
                                            Intent i2 = new Intent(RoomChat.this,Chat.class);
                                            startActivity(i2);
                                        }
                                        else{
                                            Toast.makeText(RoomChat.this,"Room password is incorrect! Try again.",Toast.LENGTH_SHORT);
                                            edir.setTextColor(getResources().getColor(R.color.red));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    final AlertDialog ad1 =  inroom.show();
                    ccir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad1.dismiss();
                        }
                    });
                }



            }
            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
    }

    private void initData(){
        MainActivity.myRef.child("Room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String d = snapshot.getKey().toString();
                if (d.equals("Phong chung")==false){
                    mSuggestions.add(new Suggestion(d));
                }

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

    private List<Suggestion> getSuggestion(String query){
        List<Suggestion> suggestions=new ArrayList<>();
        for(Suggestion suggestion:mSuggestions){
            if(suggestion.getmName().toLowerCase().contains(query.toLowerCase())){
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

}