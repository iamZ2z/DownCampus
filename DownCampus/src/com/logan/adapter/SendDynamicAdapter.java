package com.logan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.mobilecampus.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Created by Z2z on 2017/3/17.
 */

public class SendDynamicAdapter extends BaseAdapter {
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private int mScreenWidth;
    private Context mContext;
    //可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
    private int maxImages = 10;

    public SendDynamicAdapter(Activity activity, List<PhotoInfo> list, Context context) {
        this.mList = list;
        this.mInflater = LayoutInflater.from(activity);
        this.mScreenWidth = DeviceUtils.getScreenPix(activity).widthPixels;
        this.mContext = context;
    }

    //获取最大上传张数
    public int getMaxImages() {
        return maxImages;
    }

    //设置最大上传张数
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    @Override
    public int getCount() {
        int count = mList == null ? 1 : mList.size() + 1;
        if (count >= maxImages) return mList.size();
        else return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<PhotoInfo> datas) {
        this.mList = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LinearLayout.inflate(mContext, R.layout.adapter_photo_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        if (mList != null && position < mList.size()) {
            /*Glide.with(mContext)
                    .load(R.mipmap.dynamic_img_add).priority(Priority.HIGH)
                    .centerCrop().into(viewHolder.imageView);*/
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(R
                    .drawable.headportrait).showImageForEmptyUri(R.drawable.headportrait)
                    .showImageOnLoading(R.drawable.headportrait).build();

            PhotoInfo photoInfo = mList.get(position);
            ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(),
                    viewHolder.imageView, options);

            viewHolder.btn.setVisibility(View.VISIBLE);
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            Glide.with(mContext).load(R.mipmap.dynamic_img_add).priority(Priority.HIGH)
                    .centerCrop().into(viewHolder.imageView);
            viewHolder.btn.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        public final ImageView imageView;
        public final Button btn;
        public final View root;

        public ViewHolder(View root) {
            imageView = (ImageView) root.findViewById(R.id.iv);
            btn = (Button) root.findViewById(R.id.btn);
            this.root = root;
        }
    }
}
