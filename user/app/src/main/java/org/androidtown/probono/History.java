package org.androidtown.probono;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class History extends AppCompatActivity {
    private  DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("요청이력");
        dbHelper = new DBHelper(
                this,
                "history",
                null,1
        );
        ListView listview;
        ListViewAdapter adapter;
        List historyList =dbHelper.getAllHistory();

        adapter = new ListViewAdapter(historyList,this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

    }
}
