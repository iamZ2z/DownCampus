package com.logan.actme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.UploadEmailBean;
import com.logan.bean.UploadiconBean;
import com.logan.constant.InterfaceTest;
import com.logan.constant.UsuallyData;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.util.title.TitleBars;
import com.util.view.CropViewActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ContentView(R.layout.me_editdata)
public class EditDataActivity extends Activity implements OnClickListener, OnMenuItemClickListener {
    /*@ViewInject(R.id.btn_headportrait)
    private ImageView btn_headportrait;*/
    @ViewInject(R.id.iv_headportrait)
    private CircleImageView iv_headportrait;
    @ViewInject(R.id.editdata_role)
    private TextView editdata_role;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.sex)
    private TextView sex;
    @ViewInject(R.id.userid)
    private TextView userid;
    @ViewInject(R.id.place)
    private TextView place;
    @ViewInject(R.id.mobile)
    private TextView mobile;
    @ViewInject(R.id.email)
    private EditText email;
    @ViewInject(R.id.btn_email)
    private ImageView btn_email;
    @ViewInject(R.id.autograph)
    private EditText autograph;
    @ViewInject(R.id.btn_autograph)
    private ImageView btn_autograph;
    private Intent mIntent;
    // 设置图片
    private Bitmap bitMap;
    private static final int CAMERA_WITH_DATA = 1001;
    private static final int PHOTO_PICKED_WITH_DATA = 1002;
    private static final int SDCARD_DISPLAY = 1003;
    // 存储路径
    private static String path = "/sdcard/MobileCampus/";
    @ViewInject(R.id.title_bar)
    private TitleBars titlebar;
    private ImageView mCollectView;
    @ViewInject(R.id.uploadtest)
    private LinearLayout uploadtest;
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        initView();
        initData();
        iv_headportrait.setOnClickListener(this);
        //uploadtest.setOnClickListener(this);

    }

    private void initView() {
        titlebar.setTitle("编辑资料");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //输入法弹出
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue_55x176x233);
    }

    private void initData() {
        name.setText(usuallyData.getFullname());
        if (usuallyData.getSex().equals("male")) sex.setText("男");
        else sex.setText("女");
        userid.setText(interfaceTest.getUser_id());
        place.setText(usuallyData.getOrganizationName());
        mobile.setText(usuallyData.getMobile());
        email.setText(usuallyData.getEmail());
        autograph.setText(usuallyData.getAutograph());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_headportrait:
                popupMenu();
                break;
            default:
                break;
        }
    }

    private void popupMenu() {
        PopupMenu popup = new PopupMenu(this, iv_headportrait);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.usecamera:
                cameraphoto();
                break;
            case R.id.selectphoto:
                selectphoto();
                break;
            case R.id.teacher:
                editdata_role.setText("老师");
                break;
            case R.id.student:
                editdata_role.setText("学生");
                break;
            case R.id.parent:
                editdata_role.setText("家长");
                break;
            case R.id.leader:
                editdata_role.setText("领导");
                break;
            default:
                break;
        }
        return false;
    }

    private void cameraphoto() {
        mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "head.jpg")));
        startActivityForResult(mIntent, CAMERA_WITH_DATA);
    }

    private void selectphoto() {
        mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(mIntent, PHOTO_PICKED_WITH_DATA);
    }

    // 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
                cropPhoto(data.getData());// 裁剪图片
                iv_headportrait.setVisibility(View.VISIBLE);
                break;
            }
            case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
                File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                cropPhoto(Uri.fromFile(temp));// 裁剪图片
                iv_headportrait.setVisibility(View.VISIBLE);
                break;
            }
            case SDCARD_DISPLAY:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    bitMap = extras.getParcelable("data");
                    if (bitMap != null) {
                        // 上传服务器代码
                        setPicToView(bitMap);// 保存在SD卡中
                        iv_headportrait.setImageBitmap(bitMap);// 用ImageView显示出来
                    }
                }
                break;
        }
    }

    // 调用系统的裁剪
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SDCARD_DISPLAY);
        //startResultActivity(uri);
    }

    public void startResultActivity(Uri uri) {
        /*if(isFinishing()) return;
        startActivity(CropViewActivity.createIntent(this,uri));*/
        mIntent = new Intent();
        mIntent.setClass(this, CropViewActivity.class);
        mIntent.putExtra("uri", uri);
        startActivityForResult(mIntent, 1002);
    }

    //自设圆形裁剪
    /*private void cropImage(Uri uri) {
        Intent mIntent = new Intent(this, CropActivity.class);
        mIntent.putExtra("uri", uri.toString());
        Log.v("debug uri", uri.toString());
        startActivityForResult(mIntent, SDCARD_DISPLAY);
    }*/

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Event(value = R.id.uploadtest)
    private void onuploadtestClick(View v) {
        uploadtest();
    }

    private void uploadtest() {
        File file = new File(Environment.getExternalStorageDirectory(), "abc.mp4");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                file);

        String iconpath = "/sdcard/IMG_20170512_011024.jpg";
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
                        .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img" +
                                        ".png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File
                                        (iconpath)))
                        .build())
                .build();

        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getUploadicon();
        String token = interfaceTest.getToken();
        String userid = interfaceTest.getUser_id();

        FormBody formBody = new FormBody.Builder().add("token", token).add("user_id",
                userid).build();
        final Request request = new Request.Builder().url(url).post(formBody).post(body).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("上传图片result", "请求数据:" + str);
                        final UploadiconBean bean = new Gson().fromJson(str, UploadiconBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bean.getCode().equals("0")) {
                                    Toast.makeText(EditDataActivity.this, "上传成功", Toast
                                            .LENGTH_SHORT).show();
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

    @Event(value = {R.id.btn_email, R.id.btn_autograph})
    private void onbtnemailClick(View v) {
        String url="";
        FormBody formBody =null;
        if (v.getId() == R.id.btn_email) {
            String st = email.getText().toString();
            String token = interfaceTest.getToken();
            String userid = interfaceTest.getUser_id();
            url = interfaceTest.getServerurl() + interfaceTest.getUploademail();
            formBody = new FormBody.Builder().add("token", token).add("user_id", userid)
                    .add("email", st).build();
        } else if (v.getId() == R.id.btn_autograph) {
            String st = autograph.getText().toString();
            String token = interfaceTest.getToken();
            String userid = interfaceTest.getUser_id();
            url = interfaceTest.getServerurl() + interfaceTest.getUploadautograph();
            formBody = new FormBody.Builder().add("token", token).add("user_id", userid).add
                    ("autograph", st).build();
        }
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("邮箱的result", "请求数据:" + str);
                        final UploadEmailBean bean = new Gson().fromJson(str,
                                UploadEmailBean
                                        .class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bean.getCode().equals("0"))
                                    Toast.makeText(EditDataActivity.this, bean.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(EditDataActivity.this, bean.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}