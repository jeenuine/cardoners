package org.androidtown.probono;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainInqireActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inqire);
        final TextView Gcall = (TextView)findViewById(R.id.textView18);
        final TextView Gmail = (TextView)findViewById(R.id.textView20);
        final TextView Kcall = (TextView)findViewById(R.id.textView23);
        final TextView Kmail = (TextView)findViewById(R.id.textView25);

        Gcall.setPaintFlags(Gcall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Gmail.setPaintFlags(Gmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Kcall.setPaintFlags(Kcall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Kmail.setPaintFlags(Kmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Gcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= Gcall.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                startActivity(callIntent);
            }
        });
        Kcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= Kcall.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                startActivity(callIntent);
            }
        });

        Kmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail= Kmail.getText().toString();
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                String[] address = {mail};
                mailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,"카도너스 문의");
                startActivity(mailIntent);
            }
        });

        Gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail= Gmail.getText().toString();
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                String[] address = {mail};
                mailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,"카도너스 문의");
                startActivity(mailIntent);     }
        });

    }
}
