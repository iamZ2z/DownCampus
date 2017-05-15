package com.logan.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.logan.adapter.MultiSpinnerAdapter;
import com.logan.bean.MultiSpinnerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Z2z on 2017/4/10.
 */

public class MultiSpinner extends TextView implements DialogInterface.OnClickListener, View
        .OnClickListener {
    private ListView listView;

    private Context context;

    private String title;

    private List<MultiSpinnerBean> dataList;

    private MultiSpinnerAdapter adapter;

    private Set<Object> checkedSet;

    private int selectCount = -1;


    private boolean isEmpty() {
        return dataList == null ? true : dataList.isEmpty();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCheckedSet(Set<Object> checkedSet) {
        this.checkedSet = checkedSet;
        showSelectedContent();
    }

    private void showSelectedContent() {
        StringBuilder sb = new StringBuilder();
        for (MultiSpinnerBean option : getCheckedOptions()) {
            sb.append(option.getName()).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        setText(sb.toString());
    }

    public List<MultiSpinnerBean> getCheckedOptions() {
        List<MultiSpinnerBean> list = new ArrayList<>();
        if (isEmpty() || checkedSet == null || checkedSet.isEmpty()) {
            return list;
        }
        for (MultiSpinnerBean option : dataList) {
            if (checkedSet.contains(option.getValue())) {
                list.add(option);
            }
        }
        return list;
    }

    public List<MultiSpinnerBean> getDataList() {
        return dataList;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public void setDataList(List<MultiSpinnerBean> dataList) {
        this.dataList = dataList;
        if (adapter == null) {
            adapter = new MultiSpinnerAdapter(dataList, this);
            this.listView.setAdapter(adapter);
        } else {
            adapter.setList(dataList);
            adapter.notifyDataSetChanged();
        }
    }


    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOnClickListener(this);
        listView = new ListView(context);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adapter = new MultiSpinnerAdapter(null, this);
        this.listView.setAdapter(adapter);
    }

    public MultiSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case -1:
                this.checkedSet = adapter.getCheckedSet();
                showSelectedContent();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        ViewGroup parent = (ViewGroup) listView.getParent();
        if (parent != null) {
            parent.removeView(listView);
        }
        if (dataList == null) {
            Log.d("MultiSpinner", "no data to show");
        }
        adapter.setCheckedSet(checkedSet);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("确定", this)
                .setNegativeButton("取消", this)
                .setView(listView).show();
    }


}
