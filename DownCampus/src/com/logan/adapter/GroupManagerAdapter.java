package com.logan.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mobilecampus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Z2z on 2017-4-14.
 */
public class GroupManagerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> arr;
    private List<Map<String, String>> mListedit;
    private Integer index = -1;

    public GroupManagerAdapter(Context context, ArrayList<String> arr) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.adapteritem_groupmanager, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tv = (EditText) convertView.findViewById(R.id.tv);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);

            viewHolder.tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                        index = (Integer) v.getTag();
                    return false;
                }
            });
            viewHolder.myTextWatcher = new MyTextWatcher();
            viewHolder.tv.addTextChangedListener(viewHolder.myTextWatcher);
            viewHolder.updatePosition(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.updatePosition(position);
        }
        viewHolder.tv.setText(arr.get(position).toString());

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.remove(position);
                notifyDataSetChanged();
            }
        });

        //viewHolder.tv.addTextChangedListener(new MyTextWatcher(viewHolder));
        return convertView;
    }

    private class MyTextWatcher implements TextWatcher {
        private int pos;

        public void updatePosition(int pos) {
            this.pos = pos;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            arr.set(pos, s.toString());
        }
    }

    private class ViewHolder {
        private ImageView img;
        private EditText tv;
        private ImageView delete;
        MyTextWatcher myTextWatcher;

        public void updatePosition(int position) {
            myTextWatcher.updatePosition(position);
        }
    }
}
