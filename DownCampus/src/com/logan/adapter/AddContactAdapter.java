package com.logan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.actnews.AddContactDetailActivity;
import com.util.address.GroupMemberBean;

import java.util.List;

/**
 * Created by Z2z on 2017-4-18.
 */

public class AddContactAdapter extends BaseAdapter {
    private Context context;
    private List<GroupMemberBean> arr;

    public AddContactAdapter(Context context, List<GroupMemberBean> arr) {
        this.context = context;
        this.arr = arr;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(List<GroupMemberBean> arr) {
        this.arr = arr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arr.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.adapteritem_addcontact, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.name.setText(arr.get(position).getName());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddContactDetailActivity.class);
                intent.putExtra("searchname",arr.get(position).getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        RelativeLayout layout;
        TextView name;
        ImageView icon;
    }
}
