package com.judai.projectm;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();

    Button login;
    EditText username,userpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                String text = userpassword.getText().toString();
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();
                if(text.equals("1")==true){
                    Toast.makeText(MainActivity.this,"ổn",Toast.LENGTH_LONG).show();
                }
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