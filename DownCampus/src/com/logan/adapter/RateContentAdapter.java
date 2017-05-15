package com.logan.adapter;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.acthome.more.RateActivityUtil;

public class RateContentAdapter extends BaseAdapter {
    private List<RateActivityUtil> list;
    private LayoutInflater mLayoutInflater;
    private int mTouchItemPosition = -1;

    private final class ViewHolder {
        TextView title;
        TextView score;
        TextView content;
        EditText editscore;
        MyTextWatcher textWatcher;

        ViewHolder(View v) {
            title = (TextView) v.findViewById(R.id.title);
            score = (TextView) v.findViewById(R.id.score);
            content = (TextView) v.findViewById(R.id.content);
            editscore = (EditText) v.findViewById(R.id.editscore);
        }
    }

    public RateContentAdapter(Context context, List<RateActivityUtil> listItems) {
        mLayoutInflater = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.list = listItems;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.home_rate_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textWatcher.updatePosition(position);
        }

        viewHolder.title.setText(list.get(position).getTitle());
        viewHolder.content.setText(list.get(position).getContent());
        viewHolder.score.setText("分值:"+list.get(position).getScore());
        //viewHolder.editscore.setText(list.get(position).getSetscore());
        viewHolder.editscore.setTag(list.get(position));

        if (mTouchItemPosition == position) {
            viewHolder.editscore.requestFocus();
            viewHolder.editscore.setSelection(viewHolder.editscore.getText().length());
        } else viewHolder.editscore.clearFocus();

        viewHolder.textWatcher = new MyTextWatcher();
        //viewHolder.editscore.addTextChangedListener(viewHolder.textWatcher);
        viewHolder.editscore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RateActivityUtil bean=(RateActivityUtil)viewHolder.editscore.getTag();
                bean.setSetscore(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        viewHolder.textWatcher.updatePosition(position);

        if(!TextUtils.isEmpty(list.get(position).getSetscore()+""))
            viewHolder.editscore.setText(list.get(position).getSetscore());
        else viewHolder.editscore.setText("");

        return convertView;
    }

    class MyTextWatcher implements TextWatcher {
        //由于TextWatcher的afterTextChanged中拿不到对应的position值，所以自己创建一个子类
        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            /*if (s.toString().equals("") )
                list.get(mPosition).setSetscore("");
            else list.get(mPosition).setSetscore(s.toString());*/
        }
    }
}
