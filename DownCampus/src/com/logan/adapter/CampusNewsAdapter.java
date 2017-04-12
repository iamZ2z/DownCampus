package com.logan.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CampusNewsAdapter extends BaseAdapter {	
	private static final int TYPE_BIG = 0;
	private static final int TYPE_NORNAL = 1;
	private Context context;
	// list
	private ArrayList<HashMap<String, Object>> mArrayList=null;
	private int[] layout;
	private String[] from;
	private int[] to;

	public CampusNewsAdapter(Context context, ArrayList<HashMap<String, Object>> arr,
			int[] layout,String[] from ,int[] to) {
		this.context = context;
		this.mArrayList=arr;
		this.layout=layout;
		this.from=from;
		this.to=to;
	}

	/*public int getItemViewType(int position) {
		int result = 0;
		if (mList.get(position) instanceof CampusNewsBigBean) {
			result = TYPE_BIG;
		} else if (mList.get(position) instanceof CampusNewsBean) {
			result = TYPE_NORNAL;
		}
		return result;
	}*/

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		final int who=(Integer)mArrayList.get(position).get("scale");
		convertView=LayoutInflater.from(context).inflate(layout[who==TYPE_BIG?0:1],null );
		viewHolder=new ViewHolder();
		viewHolder.title=(TextView)convertView.findViewById(to[who*2+0]);
		viewHolder.contentType=(TextView)convertView.findViewById(to[who*2+1]);
		viewHolder.dataTime=(TextView)convertView.findViewById(to[who*2+2]);
		viewHolder.img=(ImageView)convertView.findViewById(to[who*2+3]);
		viewHolder.title.setText((mArrayList.get(position).get(from[0]).toString()));
		viewHolder.contentType.setText((mArrayList.get(position).get(from[1]).toString()));
		viewHolder.dataTime.setText((mArrayList.get(position).get(from[2]).toString()));
		viewHolder.img.setBackgroundResource((Integer)mArrayList.get(position).get(from[3]));		
		return convertView;		
	}
	
	private class ViewHolder{
		TextView title;
		TextView contentType;
		TextView dataTime;
		ImageView img;
	}

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderBig holderBig = null;
		ViewHolderNor holderNor = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			holderBig = new ViewHolderBig();
			holderNor = new ViewHolderNor();
			switch (type) {
			case TYPE_BIG:
				convertView = View.inflate(context,
						R.adapter_photo_item.item_campusnews_big, null);
				holderBig.title = (TextView) convertView
						.findViewById(R.id.title);
				holderBig.contentType = (TextView) convertView
						.findViewById(R.id.place);
				holderBig.dataTime = (TextView) convertView
						.findViewById(R.id.time);
				holderBig.img = (ImageView) convertView.findViewById(R.id.img);
				convertView.setTag(R.id.tag_big, holderBig);
				break;
			case TYPE_NORNAL:
				convertView = View.inflate(context, R.adapter_photo_item.item_campusnews,
						null);
				holderNor.title = (TextView) convertView
						.findViewById(R.id.title2);
				holderNor.contentType = (TextView) convertView
						.findViewById(R.id.place2);
				holderNor.dataTime = (TextView) convertView
						.findViewById(R.id.time2);
				holderNor.img = (ImageView) convertView.findViewById(R.id.img2);
				convertView.setTag(R.id.tag_normal, holderNor);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case TYPE_BIG:
				holderBig = (ViewHolderBig) convertView.getTag(R.id.tag_big);
				break;
			case TYPE_NORNAL:
				holderNor = (ViewHolderNor) convertView.getTag(R.id.tag_normal);
				break;
			default:
				break;
			}
		}

		Object obj = mList.get(position);
		switch (type) {
		case TYPE_BIG:
			CampusNewsBigBean bigBean = (CampusNewsBigBean) obj;
			holderBig.title.setText(bigBean.getTitle());
			holderBig.contentType.setText(bigBean.getContentType());
			holderBig.dataTime.setText(bigBean.getDataTime());
			//holderBig.img.setImageURI(Uri.parse(bigBean.getImg_str()));
			holderBig.img.setBackgroundResource(bigBean.getImg_str());
			break;
		case TYPE_NORNAL:
			CampusNewsBean bean = (CampusNewsBean) obj;
			holderNor.title.setText("");
			holderNor.contentType.setText("");
			holderNor.dataTime.setText("");
			holderNor.img.setBackgroundResource(bean.getImg_str());
			break;
		default:
			break;
		}

		return convertView;
	}

	private class ViewHolderBig {
		TextView title;
		TextView contentType;
		TextView dataTime;
		ImageView img;
	}

	private class ViewHolderNor {
		TextView title;
		TextView contentType;
		TextView dataTime;
		ImageView img;
	}*/
}
