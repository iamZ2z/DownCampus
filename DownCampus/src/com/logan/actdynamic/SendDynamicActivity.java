package com.logan.actdynamic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.logan.adapter.SendDynamicAdapter;
import com.logan.widgets.AlignGridView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.util.title.TitleBar;
import com.util.UILImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

@ContentView(R.layout.find_senddynamic)
public class SendDynamicActivity extends Activity {
    @ViewInject(R.id.img_add)
    private ImageView img_add;
    private Intent mIntent;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;

    @ViewInject(R.id.grid)
    private AlignGridView grid;

    private Bitmap bitmap;
    private ArrayList<HashMap<String, Object>> arrayList;
    private SimpleAdapter simpleAdapter;

    private final int REQUEST_CODE_CAMETA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private List<PhotoInfo> mPhotoList;
    private SendDynamicAdapter mChoosePhotoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        titlebar.setTitle("");
        titlebar.setLeftText("班级动态");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("发表") {
            @Override
            public void performAction(View view) {
                //Toast.makeText(MainActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
            }
        });

        img_add.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "拍照");
                menu.add(0, 1, 0, "从手机相册获取");
            }
        });
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new SendDynamicAdapter(this, mPhotoList, this);
        putBitmap();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if(position==parent.getChildCount()-1){
                    mPhotoList.add(R.mipmap.dynamic_img_add);
                }*/

                img_add.showContextMenu();
                grid.setOnCreateContextMenuListener();
            }
        });
    }

    private void initGalleryFinal() {
        ThemeConfig theme = new ThemeConfig.Builder().setIconBack(R.mipmap.nav_btn_back).setTitleBarBgColor(Color.rgb(55,176,233)).build();

        FunctionConfig.Builder config = new FunctionConfig.Builder();
        config.setMutiSelectMaxSize(9).setEnablePreview(true).setSelected(mPhotoList);
        final FunctionConfig functionConfig = config.build();

        ImageLoader imageLoader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(SendDynamicActivity.this, imageLoader,
                theme).setFunctionConfig(functionConfig).setNoAnimcation(true).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig,
                mOnHanlderResultCallback);
        ImageLoaderConfiguration.Builder ilconfig = new ImageLoaderConfiguration.Builder(this);
        ilconfig.threadPriority(Thread.NORM_PRIORITY - 2);
        ilconfig.denyCacheImageMultipleSizesInMemory();
        ilconfig.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        ilconfig.diskCacheSize(40 * 1024 * 1024);
        ilconfig.tasksProcessingOrder(QueueProcessingType.LIFO);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(ilconfig.build());
    }

    private void putBitmap() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.nav_btn_add);
        arrayList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemimg", bitmap);
        simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.item_senddynamic_gridimg, new
                String[]{"itemImg"}, new int[]{R.id.iv});
    }

    //首次添加照片
    @Event(value = R.id.img_add)
    private void onImg_addClick(View v) {
        img_add.showContextMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                /*mIntent = new Intent();
                mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(mIntent, 1);*/

                ThemeConfig theme = new ThemeConfig.Builder().setTitleBarBgColor(Color.rgb(55,176,233)).setIconBack(R.mipmap.nav_btn_back).build();
                FunctionConfig.Builder config = new FunctionConfig.Builder();
                config.setEnableCamera(true).setSelected(mPhotoList);
                final FunctionConfig functionConfig = config.build();
                ImageLoader imageLoader = new UILImageLoader();
                CoreConfig coreConfig = new CoreConfig.Builder(SendDynamicActivity.this, imageLoader,
                        theme).setFunctionConfig(functionConfig).build();
                GalleryFinal.init(coreConfig);
                GalleryFinal.openCamera(REQUEST_CODE_CAMETA,functionConfig,mOnHanlderResultCallback);
                break;
            case 1:
                initGalleryFinal();
                break;
            default:
                break;
        }
        return true;
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal
            .OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                if(reqeustCode==REQUEST_CODE_GALLERY) mPhotoList.clear();
                mPhotoList.addAll(resultList);
                grid.setAdapter(mChoosePhotoListAdapter);
                mChoosePhotoListAdapter.notifyDataSetChanged();
                //上面按钮消失
                img_add.setVisibility(View.GONE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            //Toast.makeText(SendDynamicActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

}
