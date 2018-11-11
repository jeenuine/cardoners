package com.example.user.cardoners;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Agree2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree2);

        Button button1 = (Button) findViewById(R.id.service_next2); //해당 버튼을 지정합니다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                //Intent intent = new Intent(Agree2Activity.this, Main2Activity.class);
                Intent intent = new Intent(Agree2Activity.this, VolunteerActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });
    }
}


