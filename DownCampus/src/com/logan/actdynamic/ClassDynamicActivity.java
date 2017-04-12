package com.logan.actdynamic;

import java.util.ArrayList;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.mobilecampus.R;
import com.logan.adapter.ClassDynamicAdapter;
import com.logan.bean.ClassDynamicBean;
import com.util.TitleBar;
import com.util.view.FloatImageButton;

@ContentView(R.layout.find_classact)
public class ClassDynamicActivity extends Activity {
    private Intent mIntent;
    // 列表
    @ViewInject(R.id.list)
    private ListView mListView;
    private ArrayList<ClassDynamicBean> mArrayList;
    ClassDynamicBean mClassDynamicBean;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.btn_send)
    private FloatImageButton mFloatImageButton;

    @ViewInject(R.id.scroll)
    private ScrollView mScrollView;

    private ClassDynamicAdapter mClassDynamicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("班级动态");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
        mClassDynamicAdapter=new ClassDynamicAdapter(this, mArrayList);
        mListView.setAdapter(mClassDynamicAdapter);
        mScrollView.smoothScrollTo(0,0);

    }

    @Event(value = R.id.btn_send)
    private void onSendClick(View v) {
        mIntent = new Intent(this, SendDynamicActivity.class);
        startActivity(mIntent);
    }

    private void getData() {
        mArrayList = new ArrayList<>();
        mClassDynamicBean=new ClassDynamicBean();
        mClassDynamicBean.setHead("R.drawable.touxiang");
        mClassDynamicBean.setTitle("李老师");
        mClassDynamicBean.setTime("2007-05-06");
        mClassDynamicBean.setUpload("R.drawable.touxiang");
        mClassDynamicBean.setContent("最近好喜欢则首个，徐佳莹棒棒哒啦啦啦啦啦啦，好久没去鼓楼大姐了，特别喜欢这个图，晚安每个灵魂记载我们的微笑");
        mClassDynamicBean.setType_pic("0");
        mArrayList.add(mClassDynamicBean);

        mClassDynamicBean=new ClassDynamicBean();
        mClassDynamicBean.setHead("R.drawable.touxiang");
        mClassDynamicBean.setTitle("李老师");
        mClassDynamicBean.setTime("2007-05-06");
        mClassDynamicBean.setUpload("R.drawable.touxiang");
        //ListAdapter listAdapter=new ArrayAdapter<>();
        //mClassDynamicBean.setUpload2(listAdapter);
        //List<ClassDynamicBean> image=new ArrayList<>();
        mClassDynamicBean.image.add(R.drawable.upload);
        mClassDynamicBean.image.add(R.drawable.upload);
        mClassDynamicBean.image.add(R.drawable.upload);
        mClassDynamicBean.image.add(R.drawable.upload);
        mClassDynamicBean.setContent("最近好喜欢则首个，徐佳莹棒棒哒啦啦啦啦啦啦，好久没去鼓楼大姐了，特别喜欢这个图，晚安每个灵魂记载我们的微笑");
        mClassDynamicBean.setType_pic("1");
        mArrayList.add(mClassDynamicBean);
    }
}
