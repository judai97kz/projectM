package com.judai.projectm;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();

    Button login;
    EditText username,userpassword;

    public static String FullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        Action();
    }

    public void Nhap(View view){
        Intent intent = new Intent(MainActivity.this,SignUp.class);
        startActivity(intent);
    }

    private void AnhXa(){
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        userpassword = findViewById(R.id.userpassword);
//        userpassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void Action()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String tk = username.getText().toString();
               String mk = userpassword.getText().toString().trim();
               myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.hasChild("Data/" + tk)){
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
                                                      myRef.child("Data/"+tk+"/lastname").addValueEventListener(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String last = snapshot.getValue().toString();
                                                            FullName = first.concat(" ");
                                                            FullName = FullName.concat(last);
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
                                           }
                                           else
                                           {
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
                       }
                       else{
                           Toast.makeText(MainActivity.this,"Username is incorrect or does not exist !",Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "back press",
                    Toast.LENGTH_LONG).show();
        return false;
    }
}