package com.logan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.bean.MultiSpinnerBean;
import com.logan.widgets.MultiSpinner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Z2z on 2017/4/10.
 */

public class MultiSpinnerAdapter extends BaseAdapter implements View.OnClickListener {
    private List<MultiSpinnerBean> list;

    private Set<Object> checkedSet;

    private MultiSpinner context;

    public MultiSpinnerAdapter(List<MultiSpinnerBean> list, MultiSpinner context) {
        this.list = list;
        checkedSet = new HashSet<Object>();
        this.context = context;
    }

    public void setList(List<MultiSpinnerBean> list) {
        this.list = list;
    }

    public Set<Object> getCheckedSet() {
        return this.checkedSet;
    }

    public void setCheckedSet(Set<Object> checkedSet) {
        this.checkedSet = new HashSet<Object>();
        if (checkedSet != null) {
            this.checkedSet.addAll(checkedSet);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MultiSpinnerBean mul = (MultiSpinnerBean) this.getItem(position);
        Wrapper wrapper = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context.getContext()).inflate(R.layout
                    .multi_spinner_item, null);
            wrapper = new Wrapper();
            wrapper.textView = (TextView) convertView.findViewById(R.id.textView);
            wrapper.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            wrapper.checkBox.setOnClickListener(this);
            convertView.setTag(wrapper);
        }
        wrapper = (Wrapper) convertView.getTag();
        wrapper.textView.setText(mul.getName());

        if (checkedSet != null) {
            if (checkedSet.contains(mul.getValue())) {
                wrapper.checkBox.setChecked(true);
            } else {
                wrapper.checkBox.setChecked(false);
            }
        }
        wrapper.checkBox.setTag(position);
        return convertView;
    }

    public void onClick(View v) {
        CheckBox checkBox = (CheckBox) v;
        Integer position = (Integer) checkBox.getTag();
        if (position == null) {
            return;
        }
        MultiSpinnerBean op = (MultiSpinnerBean) getItem(position);
        if (checkBox.isChecked()) {
            int maxCount = context.getSelectCount();
            if (maxCount > -1 && checkedSet.size() >= maxCount) {
                checkBox.setChecked(false);
                //Toast.makeText(context, String.format("最多只能选择 %s 个", selectCount), Toast
                // .LENGTH_SHORT).show();
                return;
            }
            checkedSet.add(op.getValue());
        } else {
            checkedSet.remove(op.getValue());
        }
    }

    class Wrapper {
        public TextView textView;
        public CheckBox checkBox;
    }
}
