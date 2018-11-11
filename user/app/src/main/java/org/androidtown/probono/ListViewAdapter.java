package org.androidtown.probono;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.user.cardoners.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List history;
    private Context context;

    public ListViewAdapter(List history, Context context) {
        this.history = history;
        this.context = context;
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance) ;
        TextView timeTextView = (TextView) convertView.findViewById(R.id.time) ;

        TextView start_addressTextView = (TextView) convertView.findViewById(R.id.start_address) ;
        TextView end_addressTextView = (TextView) convertView.findViewById(R.id.end_address) ;

        historyResource listViewItem = (historyResource) getItem(position);

        distanceTextView.setText(listViewItem.getDistance());
        timeTextView.setText(listViewItem.getTime());

        start_addressTextView.setText(listViewItem.getNow());
        end_addressTextView.setText(listViewItem.getDest());

        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return history.get(position) ;
    }


}