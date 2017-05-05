package com.logan.adapter;

import java.util.List;

import android.content.Context;
import android.text.Editable;
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
    private Context context;
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
        this.context = context;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.home_rate_list, null);
            viewHolder = new ViewHolder(convertView);

            viewHolder.editscore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mTouchItemPosition = (Integer) v.getTag();
                    return false;
                }
            });
            viewHolder.textWatcher = new MyTextWatcher();
            viewHolder.editscore.addTextChangedListener(viewHolder.textWatcher);
            viewHolder.textWatcher.updatePosition(position);
            convertView.setTag(viewHolder);

            viewHolder.title.setText(list.get(position).title);
            viewHolder.content.setText(list.get(position).content);
            viewHolder.score.setText(list.get(position).score + "");
            viewHolder.editscore.setText(list.get(position).setscore + "");
            viewHolder.editscore.setTag(position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mTouchItemPosition == position) {
            viewHolder.editscore.requestFocus();
            viewHolder.editscore.setSelection(viewHolder.editscore.getText().length());
        } else viewHolder.editscore.clearFocus();

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
            if(s.toString().equals("")||s.toString().equals("0")) list.get(mPosition).setSetscore(0);
            else list.get(mPosition).setSetscore(Integer.parseInt(s.toString()));
        }
    }
}
