package com.logan.actnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mobilecampus.R;
import com.logan.adapter.GroupManagerAdapter;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Z2z on 2017-4-14.
 */

@ContentView(R.layout.me_groupmanager)
public class GroupManagerActivity extends Activity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;
    @ViewInject(R.id.layout)
    private LinearLayout mLayout;
    @ViewInject(R.id.list)
    private ListView mListView;
    private GroupManagerAdapter mGroupManagerAdapter;
    private ArrayList<String> mArrayList;
    private static final String space="data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("分组管理");
        titlebar.setLeftVisible(false);
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                putSharedPreference();

                Intent intent=new Intent();
                intent.putExtra("result",mArrayList);
                setResult(0,intent);
                finish();
            }
        });

        initListView();
    }

    private void putSharedPreference() {
        SharedPreferences sp = getSharedPreferences(space, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("addresslist",mArrayList.size());
        for (int i = 0; i <mArrayList.size() ; i++) {
            editor.remove("addressarraylist"+i);
            editor.putString("addressarraylist"+i,mArrayList.get(i));
        }
        editor.commit();
    }

    private void initListView() {
        mArrayList=new ArrayList<>();
        mArrayList=getIntent().getStringArrayListExtra("group");
        mGroupManagerAdapter=new GroupManagerAdapter(this,mArrayList);
        mListView.setAdapter(mGroupManagerAdapter);
    }

    @Event(value=R.id.layout)
    private void onlayoutClick(View v){
        mArrayList.add("联系人");
        mGroupManagerAdapter.notifyDataSetChanged();
    }
}
