package com.logan.acthome.more;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.FootPrintAdapter;
import com.logan.bean.FootPrintBean;
import com.logan.constant.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_footprint)
public class FootPrintActivity extends Activity {
    // 列表
    private SimpleAdapter mAdapter;
    @ViewInject(R.id.footprint_list)
    private ListView mListView;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.footprint_search_left)
    private ImageView footprint_search_left;
    @ViewInject(R.id.footprint_search_center)
    private TextView footprint_search_center;
    @ViewInject(R.id.footprint_search_right)
    private ImageView footprint_search_right;
    private Calendar mCalendar = Calendar.getInstance();
    private String st = mCalendar.get(Calendar.YEAR) + "-" + (mCalendar.get(Calendar.MONTH) + 1);
    private List<HashMap<String, Object>> data;
    @ViewInject(R.id.nulldata)
    private ImageView nulldata;

    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();

        footprint_search_center.setText(st);
        dourl(st + "-01");
        swipe();
    }

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("data", "3月30日");
        mMap.put("time", "9:00签到");
        mMap.put("sign", "正常");
        mHashmap.add(mMap);
        return mHashmap;
    }

    @Event(value = R.id.footprint_search_left)
    private void onImg_leftClick(View v) {
        String str[] = st.split("-");
        int str_year = Integer.parseInt(str[0]);
        int str_month = Integer.parseInt(str[1]) - 1;
        if (str_month == 0) {
            str_year--;
            str_month = 12;
        }
        st = str_year + "-" + str_month;
        footprint_search_center.setText(st);
        dourl(st);
    }

    @Event(value = R.id.footprint_search_right)
    private void onImg_rightClick(View v) {
        String str[] = st.split("-");
        int str_year = Integer.parseInt(str[0]);
        int str_month = Integer.parseInt(str[1]) + 1;
        if (str_month == 13) {
            str_year++;
            str_month = 1;
        }
        st = str_year + "-" + str_month;
        footprint_search_center.setText(st);
        dourl(st);
    }

    private void initView() {
        titlebar.setTitle("足迹");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dourl(String styearmonth) {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getTeacherfootprint();
        String token = interfaceTest.getToken();
        String userid = interfaceTest.getUser_id();

        FormBody formBody = new FormBody.Builder().add("token", token).add("user_id", userid).add
                ("time", styearmonth + "-01").build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("footprint的result", "请求数据:" + str);
                        FootPrintBean bean = new Gson().fromJson(str, FootPrintBean.class);
                        if (bean.getCode().equals("0")) {
                            data = getData2(bean);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FootPrintAdapter adapter = new FootPrintAdapter
                                            (FootPrintActivity.this, data);
                                    mListView.setAdapter(adapter);
                                    nulldata.setVisibility(View.GONE);
                                    mListView.setVisibility(View.VISIBLE);

                                    adapter.notifyDataSetChanged();
                                    swiperefresh.setRefreshing(false);
                                }
                            });
                        } else runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setVisibility(View.GONE);
                                nulldata.setVisibility(View.VISIBLE);

                                swiperefresh.setRefreshing(false);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<HashMap<String, Object>> getData2(FootPrintBean bean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j++) {
                    if (!bean.getData().get(j).getTime1().equals("null")) {
                        mMap = new HashMap<>();
                        mMap.put("data", bean.getData().get(j).getDates());
                        mMap.put("time", bean.getData().get(j).getTime1() + "签到");
                        mMap.put("sign", bean.getData().get(j).getState1());
                        mHashmap.add(mMap);
                    }
                    if (!bean.getData().get(j).getTime2().equals("null")) {
                        mMap = new HashMap<>();
                        mMap.put("data", bean.getData().get(j).getDates());
                        mMap.put("time", bean.getData().get(j).getTime2() + "签退");
                        mMap.put("sign", bean.getData().get(j).getState2());
                        mHashmap.add(mMap);
                    }
                }
                return mHashmap;
            }
        }).start();
    }

    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dourl(st + "-01");
            }
        });
    }
}
