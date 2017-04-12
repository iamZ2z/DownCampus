package com.logan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.bean.ClassDynamicBean;

import java.util.ArrayList;

/**
 * Created by Z2z on 2017/3/19.
 */

public class ClassDynamicAdapter extends BaseAdapter {
    final int TYPE_ONE = 0;
    final int TYPE_MORE = 1;

    Context mContext;
    LinearLayout mLinearLayout;
    LayoutInflater mLayoutInflater;
    TextView mTextView;
    ArrayList<ClassDynamicBean> mArrayList;

    public ClassDynamicAdapter(Context context, ArrayList<ClassDynamicBean> list) {
        mArrayList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public ClassDynamicBean getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if ("0".equals(mArrayList.get(position).getType_pic()))
            return TYPE_ONE;
        else
            return TYPE_MORE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderOne viewHolderOne = null;
        ViewHolderMore viewHolderMore=null;
        int type=getItemViewType(position);
        if(convertView==null){
            mLayoutInflater=LayoutInflater.from(mContext);
            switch (type){
                case TYPE_ONE:
                    convertView=mLayoutInflater.inflate(R.layout.item_classdynamic,parent,false);
                    viewHolderOne=new ViewHolderOne();
                    viewHolderOne.head=(ImageView)convertView.findViewById(R.id.head);
                    viewHolderOne.title=(TextView) convertView.findViewById(R.id.title);
                    viewHolderOne.time= (TextView) convertView.findViewById(R.id.time);
                    viewHolderOne.upload=(ImageView)convertView.findViewById(R.id.upload);
                    viewHolderOne.content= (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(viewHolderOne);
                    break;
                case TYPE_MORE:
                    convertView=mLayoutInflater.inflate(R.layout.item_classdynamic_more,parent,false);
                    viewHolderMore= new ViewHolderMore();
                    viewHolderMore.head=(ImageView)convertView.findViewById(R.id.head);
                    viewHolderMore.title=(TextView) convertView.findViewById(R.id.title);
                    viewHolderMore.time= (TextView) convertView.findViewById(R.id.time);
                    viewHolderMore.grid=(GridView)convertView.findViewById(R.id.upload2);
                    viewHolderMore.content= (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(viewHolderMore);
                    break;
            }
        }else{
            switch (type){
                case TYPE_ONE:
                    viewHolderOne=(ViewHolderOne)convertView.getTag();
                    break;
                case TYPE_MORE:
                    viewHolderMore=(ViewHolderMore)convertView.getTag();
                    break;
            }
        }
        switch (type){
            case TYPE_ONE:
                //viewHolderOne.head.setBackgroundResource(R.drawable.touxiang);
                viewHolderOne.title.setText(mArrayList.get(position).getTitle());
                viewHolderOne.time.setText(mArrayList.get(position).getTime());
                viewHolderOne.upload.setBackgroundResource(R.drawable.upload);
                viewHolderOne.content.setText(mArrayList.get(position).getContent());
                break;
            case TYPE_MORE:
                //viewHolderMore.head.setBackgroundResource(R.drawable.touxiang);
                viewHolderMore.title.setText(mArrayList.get(position).getTitle());
                viewHolderMore.time.setText(mArrayList.get(position).getTime());
                viewHolderMore.content.setText(mArrayList.get(position).getContent());
                //viewHolderMore.grid.setAdapter(mArrayList.get(position).getUpload2());
                final ClassDynamicBean item=getItem(position);
                viewHolderMore.grid.setAdapter(new ClassDynamicGridAdapter(mContext,item.image));
                break;
        }
        return convertView;
    }

    public class ViewHolderOne{
        ImageView head;
        TextView title;
        TextView time;
        ImageView upload;
        TextView content;
    }

    public class ViewHolderMore{
        ImageView head;
        TextView title;
        TextView time;
        TextView content;
        GridView grid;
    }
}
