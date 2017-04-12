package com.logan.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilecampus.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Z2z on 2017/3/24.
 */

public class HomeGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, Object>> listitem;

    public HomeGridAdapter(Context mContext,List<Map<String, Object>> listitem) {
        super();
        this.mContext = mContext;
        this.listitem = listitem;
    }


    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment,
                    parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.image);
        Map<String, Object> map = listitem.get(position);
        iv.setImageResource((Integer) map.get("image"));
        tv.setText(map.get("text") + "");
        return convertView;
    }
}
