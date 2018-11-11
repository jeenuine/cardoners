package org.androidtown.probono;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Driverinfo extends AppCompatActivity{

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverinfo);

        TextView name = (TextView)findViewById(R.id.name);
        final TextView call = (TextView)findViewById(R.id.call2);
        TextView number = (TextView)findViewById(R.id.myName);
        TextView kind = (TextView)findViewById(R.id.myAge);
        TextView color = (TextView)findViewById(R.id.color);

        Intent refintent = this.getIntent();
        String tmp = refintent.getStringExtra("str");
        String[] splits =tmp.split(",");
        name.setText("이름: "+splits[2]);
        call.setText(splits[3]);
        number.setText("차번호: "+splits[4]);
        kind.setText("차종류: "+splits[5]);
        color.setText("차색상: "+splits[6]);

        call.setPaintFlags(call.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= call.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                startActivity(callIntent);
            }
        });


    }
}
