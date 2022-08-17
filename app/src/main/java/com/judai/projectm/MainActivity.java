package com.judai.projectm;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();

    UserDatabase userdata;

    Button login;
    EditText username, userpassword;
    ListView lvac;
    public static String FullName;
    public static String Username;

    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    List<User> listUser;
    UserAdapter arrayAdapter;
    int i = 0;
    public static int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Datacaching", MODE_PRIVATE);
        AnhXa();
        processUN();
        DatabaseUser();
        ShowAccount();
        Action();
    }

    public void Nhap(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    private void AnhXa() {
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        userpassword = findViewById(R.id.userpassword);
        lvac = findViewById(R.id.lv);
//        userpassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void Action() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tk = username.getText().toString();
                String mk = userpassword.getText().toString().trim();
                if (tk.equals("") == true || mk.equals("") == true) {
                    Toast.makeText(MainActivity.this, "Please enter your username and password!", Toast.LENGTH_LONG).show();
                } else {
                    LoginAction(tk,mk);
                }
            }
        });
        if (RoomChat.keycheck == 1) {
            String usdt = listUser.get(0).get_username().toString();
            String pwdt = listUser.get(0).get_password().toString().trim();
            LoginAction(usdt, pwdt);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private void LoginAction(String tk, String mk) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Data/" + tk)) {
                    myRef.child("Data/" + tk + "/username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String ct = snapshot.getValue().toString();
                            myRef.child("Data/" + tk + "/pass").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String cp = snapshot.getValue().toString();
                                    if (mk.equals(cp) == true && tk.equals(ct) == true) {
                                        myRef.child("Data/" + tk + "/firstname").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String first = snapshot.getValue().toString();
                                                myRef.child("Data/" + tk + "/lastname").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        initPreferences(tk,mk);
                                                        String last = snapshot.getValue().toString();
                                                        FullName = first.concat(" ");
                                                        FullName = FullName.concat(last);
                                                        Username = tk;
                                                        if (check == 0) {
                                                            Cursor data = userdata.getData("SELECT * FROM User");
                                                            if (data.getCount() == 0) {
                                                                userdata.QueryData("INSERT INTO User VALUES('" + tk + "','" + mk + "')");
                                                            } else {
                                                                while (data.moveToNext()) {
                                                                    String dtun = data.getString(0).toString().trim();
                                                                    if (tk.equals(dtun) == false) {
                                                                        i++;
                                                                    } else {
                                                                        i = 0;
                                                                        break;
                                                                    }
                                                                }
                                                                if (i != 0) {
                                                                    userdata.QueryData("INSERT INTO User VALUES('" + tk + "','" + mk + "')");
                                                                }
                                                            }
                                                        }
                                                        Intent i1 = new Intent(MainActivity.this, RoomChat.class);
                                                        startActivity(i1);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        userpassword.setTextColor(getResources().getColor(R.color.red));
                                        Toast.makeText(MainActivity.this, "Password is incorrect!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Username is incorrect or does not exist !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DatabaseUser() {
        userdata = new UserDatabase(MainActivity.this, "Userdt.sqlite", null, 1);
        userdata.QueryData("CREATE TABLE IF NOT EXISTS User(Username VARCHAR(50) PRIMARY KEY,Pass VARCHAR(50))");
    }

    private void ShowAccount() {
        listUser = new ArrayList<>();
        Cursor data = userdata.getData("SELECT * FROM User");
        while (data.moveToNext()) {
            String dtun = data.getString(0).toString();
            String dtpw = data.getString(1).toString();
            listUser.add(new User(dtun, dtpw));
        }
        arrayAdapter = new UserAdapter(MainActivity.this, listUser, R.layout.account_row);
        lvac.setAdapter(arrayAdapter);
        lvac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String usdt = listUser.get(position).get_username().toString();
                String pwdt = listUser.get(position).get_password().toString().trim();
                LoginAction(usdt, pwdt);
                check = 1;
            }
        });
        lvac.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String usdt = listUser.get(position).get_username().toString();
                return true;
            }
        });
    }

    public void initPreferences(String name, String pass) {
        editor = sharedPreferences.edit();
        editor.putString("Username", name);
        editor.putString("Password", pass);
        editor.commit();
    }

    public void processUN(){
        String namelocal = sharedPreferences.getString("Username","");
        String passlocal = sharedPreferences.getString("Password","");
        if(namelocal.equals("")==true || passlocal.equals("")==true){
            Toast.makeText(this,sharedPreferences.getString("Username",""),Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoginAction(namelocal,passlocal);
        }
    }
}

