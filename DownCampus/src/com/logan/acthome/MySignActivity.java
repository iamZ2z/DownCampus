package com.logan.acthome;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.mobilecampus.R;
import com.logan.acthome.more.FootPrint;
import com.logan.acthome.more.SignActivity;
import com.util.TitleBar;

import java.util.Calendar;

@ContentView(R.layout.home_mysign)
public class MySignActivity extends Activity {
    @ViewInject(R.id.layout)
    private LinearLayout layout;

    @ViewInject(R.id.tv_uptime)
    private TextView tv_uptime;
    @ViewInject(R.id.tv_upsign)
    private TextView tv_upsign;

    /*@ViewInject(R.id.mysign_location)
    private TextView sign_location;*/

    @ViewInject(R.id.mysign_btnlocation)
    private TextView sign_btnlocation;
    @ViewInject(R.id.tv_nowplace)
    private TextView tv_nowplace;

    @ViewInject(R.id.mysign_sign)
    private ImageView mysign_sign;

    private Intent mIntent;

    //高德    声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    public AMapLocationClientOption mLocationOption = null;


    private String str;
    // Thread thread;
    private Handler handler = new Handler();

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.dayofweek)
    private TextView mDayofweek;
    @ViewInject(R.id.dayofyear)
    private TextView mDayofyear;
    private Calendar mCalendar = Calendar.getInstance();

    @ViewInject(R.id.complain)
    private TextView mComplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        initView();
        //initTime();

        initLocate();
    }

    private void initLocate() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //声明AMapLocationClientOption对象
        //AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(10000);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        tv_nowplace.setText("当前位置：" + aMapLocation.getAddress());
                    } else {
                        Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode
                                () + ", errInfo:" + aMapLocation.getErrorInfo());
                        Toast.makeText(MySignActivity.this, "无法定位，请检查网络", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient.isStarted()) mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    private void initView() {
        titlebar.setTitle("我的签到");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("足迹") {
            @Override
            public void performAction(View view) {
                mIntent = new Intent(MySignActivity.this, FootPrint.class);
                startActivity(mIntent);
            }
        });

        //白天和夜晚模式
        if (mCalendar.get(Calendar.HOUR_OF_DAY) >= 12)
            layout.setBackgroundResource(R.mipmap.sign_img_bg_night);

        //星期几
        int i = mCalendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                mDayofweek.setText("星期日");
                break;
            case 2:
                mDayofweek.setText("星期一");
                break;
            case 3:
                mDayofweek.setText("星期二");
                break;
            case 4:
                mDayofweek.setText("星期三");
                break;
            case 5:
                mDayofweek.setText("星期四");
                break;
            case 6:
                mDayofweek.setText("星期五");
                break;
            case 7:
                mDayofweek.setText("星期六");
                break;
            default:
                return;
        }

        mDayofyear.setText(mCalendar.get(Calendar.YEAR) + "-" + (mCalendar.get(Calendar.MONTH) +
                1) + "-" + mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void initTime() {
        tv_uptime.setText("上班：9:00  下班：18:00");
    }

    @Event(value = R.id.mysign_btnlocation)
    private void onLocationClick(View v) {
        initLocate();
    }

    @Event(value = R.id.mysign_sign)
    private void onSignClick(View v) {
        int h = mCalendar.get(Calendar.HOUR_OF_DAY);
        int m = mCalendar.get(Calendar.MINUTE);
        if (h == 9 || (h == 10 && m == 0) || h == 18) {
            mysign_sign.setImageResource(R.mipmap.sign_btn_in);
            tv_upsign.setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar
                    .MINUTE) + " 正常");
            tv_upsign.setTextColor(Color.rgb(99, 180, 255));
            Drawable drawable = getResources().getDrawable(R.mipmap.sign_icon_yes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_upsign.setCompoundDrawables(drawable, null, null, null);
        } else {
            mysign_sign.setImageResource(R.mipmap.sign_btn_none);
            tv_upsign.setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar
                    .MINUTE) + " 迟到");
            tv_upsign.setTextColor(Color.rgb(242, 137, 12));
            Drawable drawable = getResources().getDrawable(R.mipmap.sign_icon_no);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_upsign.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Event(value = R.id.complain)
    private void onComplainClick(View v) {
        mIntent = new Intent(this, SignActivity.class);
        mIntent.putExtra("signlocation", str);
        startActivity(mIntent);
    }

}
