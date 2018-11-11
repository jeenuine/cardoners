package com.example.user.cardoners;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.cardoners.dbmanager.DBManager;
import com.example.user.cardoners.dbmanager.Driver;

public class Volunteer2Activity extends AppCompatActivity {

    DBManager dbManager = new DBManager(
            this,
            "register",
            null,1
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer2);

        TextView name = findViewById(R.id.text_volunteer1);
        TextView phone = findViewById(R.id.text_volunteer2);
        TextView age = findViewById(R.id.text_volunteer3);

        Driver UserData =  dbManager.getDriverData();

        name.setText("이름 :"+UserData.getName());
        phone.setText("핸드폰번호 :"+UserData.getPhone());
        age.setText("생년월일 :"+UserData.getAge());

    }
}
