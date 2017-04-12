package com.logan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mobilecampus.R;

import java.util.List;

import static com.example.mobilecampus.R.id.iv;

/**
 * Created by Z2z on 2017/3/20.
 */

public class ClassDynamicGridAdapter extends BaseAdapter {
    Context mContext;
    List<Integer> mList;

    public ClassDynamicGridAdapter(Context context,List<Integer> list){
        mContext=context;
        mList=list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Integer getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_classdynamic_more_gridimage,parent,false);
        ImageView imageView=(ImageView)view.findViewById(iv);
        imageView.setImageResource(getItem(position));
        return view;
    }
}
