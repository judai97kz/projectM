package com.judai.projectm;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {
    EditText fn,ln,un,pw,rp,pn,gm,dtv;
    Button su,cc,dbt;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    public int yearold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        Action();

    }

    private void mapping(){
        fn = findViewById(R.id.firstname);
        ln = findViewById(R.id.lastname);
        un = findViewById(R.id.suname);
        pw = findViewById(R.id.supass);
        rp = findViewById(R.id.rppass);
        pn = findViewById(R.id.phone);
        gm = findViewById(R.id.mail);
        su = findViewById(R.id.su);
        cc = findViewById(R.id.cc);
        dtv = findViewById(R.id.datetv);
        dbt = findViewById(R.id.datech);
    }

    private void Action(){
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });
        GetCurrentDate();
        dbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectDate();
            }
        });

        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tk = un.getText().toString();
                String mk = pw.getText().toString();
                String FN = fn.getText().toString();
                String LN = ln.getText().toString();
                String RP = rp.getText().toString();
                String PHONE = pn.getText().toString();
                String MAIL = gm.getText().toString();
                String DATE = dtv.getText().toString();
                MainActivity.myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mk.length() < 8)
                        {
                            Toast.makeText(SignUp.this,"Password must be more than 8 characters",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(snapshot.hasChild("Data/"+tk))
                            {
                                Toast.makeText(SignUp.this,"Tên Đăng Ký Đã Có Người Sử Dụng !",Toast.LENGTH_LONG).show();

                            }
                            else {
                                if(yearold >= 18) {
                                    Toast.makeText(SignUp.this, "Tài khoản đăng ký thành công !", Toast.LENGTH_LONG).show();
                                    MainActivity.myRef.child("Data/" + tk + "/username").setValue(tk);
                                    MainActivity.myRef.child("Data/" + tk + "/pass").setValue(mk);
                                    MainActivity.myRef.child("Data/" + tk + "/firstname").setValue(FN);
                                    MainActivity.myRef.child("Data/" + tk + "/lastname").setValue(LN);
                                    MainActivity.myRef.child("Data/" + tk + "/phone").setValue(PHONE);
                                    MainActivity.myRef.child("Data/" + tk + "/mail").setValue(MAIL);
                                    MainActivity.myRef.child("Data/" + tk + "/date").setValue(DATE);
                                    MainActivity.myRef.child("Data/" + tk + "/join").push().setValue("Phong chung");
                                }else
                                {
                                    Toast.makeText(SignUp.this,"You are so young to use my app !",Toast.LENGTH_LONG).show();
                                }
                            }
                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void GetCurrentDate(){
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    private void buttonSelectDate() {

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                dtv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
                Date date = new Date();
                int yearsystem = date.getYear()+1900;
                yearold = yearsystem - year;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        datePickerDialog.show();
    }
}