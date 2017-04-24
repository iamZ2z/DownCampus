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

import static com.example.mobilecampus.R.id.fri;
import static com.example.mobilecampus.R.id.item;
import static com.example.mobilecampus.R.id.sat;
import static com.example.mobilecampus.R.id.sun;
import static com.example.mobilecampus.R.id.thr;
import static com.example.mobilecampus.R.id.tue;
import static com.example.mobilecampus.R.id.wed;

/**
 * Created by Z2z on 2017/4/1.
 */

public class ClassScheduleAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> arr;

    public ClassScheduleAdapter(Context context, ArrayList<HashMap<String, Object>> arr) {
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
            convertView = LinearLayout.inflate(mContext, R.layout.adapteritem_classschedule, null);
            viewHolder = new ViewHolder();
            viewHolder.item = (TextView) convertView.findViewById(R.id.item);
            viewHolder.mon = (TextView) convertView.findViewById(R.id.mon);
            viewHolder.tue = (TextView) convertView.findViewById(R.id.tue);
            viewHolder.wed = (TextView) convertView.findViewById(R.id.wed);
            viewHolder.thr = (TextView) convertView.findViewById(R.id.thr);
            viewHolder.fri = (TextView) convertView.findViewById(R.id.fri);
            viewHolder.sat = (TextView) convertView.findViewById(R.id.sat);
            viewHolder.sun = (TextView) convertView.findViewById(R.id.sun);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.item.setText(arr.get(position).get("item").toString());
        viewHolder.mon.setText(arr.get(position).get("mon").toString());
        viewHolder.tue.setText(arr.get(position).get("tue").toString());
        viewHolder.wed.setText(arr.get(position).get("wed").toString());
        viewHolder.thr.setText(arr.get(position).get("thr").toString());
        viewHolder.fri.setText(arr.get(position).get("fri").toString());
        viewHolder.sat.setText(arr.get(position).get("sat").toString());
        viewHolder.sun.setText(arr.get(position).get("sun").toString());

        if (position  == 0) {
            viewHolder.item.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.item.setTextColor(Color.rgb(255,255,255));
            viewHolder.mon.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.mon.setTextColor(Color.rgb(255,255,255));
            viewHolder.tue.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.tue.setTextColor(Color.rgb(255,255,255));
            viewHolder.wed.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.wed.setTextColor(Color.rgb(255,255,255));
            viewHolder.thr.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.thr.setTextColor(Color.rgb(255,255,255));
            viewHolder.fri.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.fri.setTextColor(Color.rgb(255,255,255));
            viewHolder.sat.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.sat.setTextColor(Color.rgb(255,255,255));
            viewHolder.sun.setBackgroundColor(Color.rgb(56, 194, 232));
            viewHolder.sun.setTextColor(Color.rgb(255,255,255));
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView item;
        public TextView mon;
        public TextView tue;
        public TextView wed;
        public TextView thr;
        public TextView fri;
        public TextView sat;
        public TextView sun;
    }
}
