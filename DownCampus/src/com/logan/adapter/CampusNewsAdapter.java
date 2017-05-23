package com.logan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mobilecampus.R;

import java.util.HashMap;
import java.util.List;

public class CampusNewsAdapter extends BaseAdapter {
    private static final int TYPE_BIG = 0;
    private static final int TYPE_NORNAL = 1;
    private Context context;
    private List<HashMap<String, Object>> arrayList;

    public CampusNewsAdapter(Context context, List<HashMap<String, Object>> arr) {
        this.context = context;
        this.arrayList = arr;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderBig viewHolderBig = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (position % 5 == 0) {
                convertView = LinearLayout.inflate(context, R.layout.item_campusnews_big, null);
                viewHolderBig = new ViewHolderBig();
                viewHolderBig.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolderBig.title = (TextView) convertView.findViewById(R.id.title);
                viewHolderBig.type = (TextView) convertView.findViewById(R.id.type);
                viewHolderBig.date = (TextView) convertView.findViewById(R.id.date);
            } else {
                convertView = LinearLayout.inflate(context, R.layout.item_campusnews, null);
                viewHolder = new ViewHolder();
                viewHolder.img2 = (ImageView) convertView.findViewById(R.id.img2);
                viewHolder.title2 = (TextView) convertView.findViewById(R.id.title2);
                viewHolder.type2 = (TextView) convertView.findViewById(R.id.type2);
                viewHolder.date2 = (TextView) convertView.findViewById(R.id.date2);
            }
        } else {
            if (position % 5 == 0) {
                viewHolderBig = (ViewHolderBig) convertView.getTag();
            } else viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 5 == 0) {
            DrawableRequestBuilder<Integer> thumbnailRequest = Glide.with(context).load(R.drawable
                    .upload);
            Glide.with(context).load(arrayList.get(position).get("img").toString()).thumbnail
                    (thumbnailRequest).diskCacheStrategy(DiskCacheStrategy.ALL).into
                    (viewHolderBig.img);
            viewHolderBig.title.setText(arrayList.get(position).get("title").toString());
            viewHolderBig.type.setText("综合实践");
            Log.e("type",viewHolderBig.type.getText().toString());
            viewHolderBig.date.setText(arrayList.get(position).get("date").toString());
            Log.e("date",viewHolderBig.date.getText().toString());
        } else {
            DrawableRequestBuilder<Integer> thumbnailRequest = Glide.with(context).load(R.drawable
                    .upload);
            Glide.with(context).load(arrayList.get(position).get("img").toString()).thumbnail
                    (thumbnailRequest).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder
                    .img2);
            viewHolder.title2.setText(arrayList.get(position).get("title").toString());
            viewHolder.type2.setText("综合实践");
            viewHolder.date2.setText(arrayList.get(position).get("date").toString());
        }
        return convertView;
    }

    private class ViewHolderBig {
        ImageView img;
        TextView title;
        TextView type;
        TextView date;
    }

    private class ViewHolder {
        ImageView img2;
        TextView title2;
        TextView type2;
        TextView date2;
    }

}
