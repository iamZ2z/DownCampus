package com.logan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.mobilecampus.R.id.act;
import static com.example.mobilecampus.R.id.leng;

/**
 * Created by Z2z on 2017/4/1.
 */

public class WorkRestAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> arr;

    public WorkRestAdapter(Context context, ArrayList<HashMap<String, Object>> arr) {
        this.mContext = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.adapteritem_workrest, null);
            viewHolder = new ViewHolder();
            viewHolder.act = (TextView) convertView.findViewById(act);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.leng = (TextView) convertView.findViewById(leng);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.act.setText(arr.get(position).get("act").toString());
        viewHolder.time.setText(arr.get(position).get("time").toString());
        viewHolder.leng.setText(arr.get(position).get("leng").toString());

        if (position % 2 == 0) {
            viewHolder.act.setBackgroundColor(Color.rgb(255, 255, 255));
            viewHolder.time.setBackgroundColor(Color.rgb(255, 255, 255));
            viewHolder.leng.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            viewHolder.act.setBackgroundColor(Color.rgb(223, 236, 255));
            viewHolder.time.setBackgroundColor(Color.rgb(223, 236, 255));
            viewHolder.leng.setBackgroundColor(Color.rgb(223, 236, 255));
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView act;
        public TextView time;
        public TextView leng;
    }
}
