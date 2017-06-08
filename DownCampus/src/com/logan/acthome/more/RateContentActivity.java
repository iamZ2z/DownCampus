package com.logan.acthome.more;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.RateContentAdapter;
import com.logan.bean.RateContentBean;
import com.logan.bean.TeacherRateBean;
import com.logan.net.InterfaceTest;
import com.logan.net.UsuallyData;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_ratecontent)
public class RateContentActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.list)
    private ListView mListView;
    private ArrayList<RateActivityUtil> mArrayList;
    @ViewInject(R.id.btn)
    private Button btn;
    private int inttemp = 0;
    @ViewInject(R.id.teachername)
    private TextView teachername;
    private RateContentAdapter adapter;
    @ViewInject(R.id.layout)
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        titlebar.setTitle("教师评价详情");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initTeachername();
        loadData();
        hidekeyboard();
        //listViewHeight(mListView);
        //addLayoutListener(mListView, btn);
    }

    private void initTeachername() {
        String name = getIntent().getStringExtra("teachername");
        teachername.setText("教师：" + name);
    }

    private void loadData() {
        mArrayList = new ArrayList<>();
        TeacherRateBean bean = (TeacherRateBean) getIntent().getSerializableExtra("teacherrate");

        for (int j = 0; j < bean.getData().size(); j++) {
            if (bean.getData().get(j).getCode() != null) {
                String str1 = bean.getData().get(j).getProject();
                String str2 = bean.getData().get(j).getStandard();
                int str3 = Integer.parseInt(bean.getData().get(j).getScore());
                String str4 = bean.getData().get(j).getCode();
                RateActivityUtil util = new RateActivityUtil(str1, str2, str3, "");
                mArrayList.add(util);
            }
        }
        adapter = new RateContentAdapter(RateContentActivity.this, mArrayList);
        mListView.setAdapter(adapter);
    }

    //提交评分
    @Event(value = R.id.btn)
    private void onBtnClick(View v) {
        TeacherRateBean bean = (TeacherRateBean) getIntent().getSerializableExtra("teacherrate");
        String strpos = "";
        int totalScore = 0;
        First:
        for (int i = 0; i < bean.getData().size(); i++) {
            if (bean.getData().get(i).getCode() != null) {
                if (mArrayList.get(i).getSetscore().equals("")) {
                    Toast.makeText(RateContentActivity.this, "请填完所有题目", Toast.LENGTH_SHORT).show();
                    inttemp = 1;
                    break First;
                } else {
                    strpos += i + "," + mArrayList.get(i).getSetscore() + ";";
                    totalScore += Integer.parseInt(mArrayList.get(i).getSetscore());
                }
            }
        }
        if (inttemp == 0) submitData(strpos, totalScore);
    }

    private void submitData(String strpos, int totalScore) {
        InterfaceTest interfaceTest = new InterfaceTest();
        UsuallyData usuallyData = new UsuallyData();
        String token = interfaceTest.getToken();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentteacherrate();
        String studentid = interfaceTest.getStudentId();
        String teacherid = getIntent().getStringExtra("teacherid");
        //String teacherid = "402888225a555c81015a55c11adf0002";

        String grade = "优秀";
        if (totalScore < 60) grade = "不及格";
        else if (totalScore >= 60 && totalScore < 75) grade = "及格";
        else if (totalScore >= 75 && totalScore < 85) grade = "良好";
        else ;

        String itemScore = strpos;
        String classid = "4028812b5a6a878a015a6a8d04570006";
        ArrayList<String> arrayList = usuallyData.getClazzid();
        if (!usuallyData.getClazzid().equals(null)) classid = arrayList.get(0);

        String valuatorType = "0";
        if (interfaceTest.getRole().equals("学生")) valuatorType = "0";
        else if (interfaceTest.getRole().equals("家长")) valuatorType = "1";

        FormBody formBody = new FormBody.Builder().add("token", token).add("student.id",
                studentid).add("valuatorType", valuatorType).add("totalScore", totalScore + "")
                .add("clazz" + ".id", classid).add("teacher.id", teacherid).add("grade", grade)
                .add("itemScore", itemScore).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("提交评价的result", "请求数据:" + str);
                        final RateContentBean bean = new Gson().fromJson(str, RateContentBean
                                .class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bean.getCode().equals("0")) {
                                    Toast.makeText(RateContentActivity.this, "提交成功 ", Toast
                                            .LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void hidekeyboard() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时
                        //下面是隐藏软键盘的代码
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(RateContentActivity.this.getCurrentFocus
                                        ().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //手指抬起，但是屏幕还在滚动状态
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });
    }

    private void listViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void addLayoutListener(final View layout, final View btn) {
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                layout.getWindowVisibleDisplayFrame(rect);
                int layoutInvisibleDisplayFrame = layout.getRootView().getHeight() - rect.bottom;
                if (layoutInvisibleDisplayFrame > 100) {
                    int[] location = new int[2];
                    btn.getLocationInWindow(location);
                    int btnHeight=(location[1]+btn.getHeight())-rect.bottom;
                    layout.scrollTo(0,btnHeight);
                }else
                    layout.scrollTo(0,100);
            }
        });
    }

}
