package com.logan.acthome.more;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;

public class RateActivity_Adapter extends BaseAdapter {
	private Context context;
	private List<RateActivity_Util> list;
	private LayoutInflater mLayoutInflater;
	public int score_arr[][]=new int[10][10]; 

	public int[][] getScore_arr() {
		return score_arr;
	}

	public final class ViewHolder {
		public TextView title;
		public TextView content;
		public RadioGroup radioGroup;
		public TextView score;

		ViewHolder(View v) {
			title = (TextView) v.findViewById(R.id.title);
			content = (TextView) v.findViewById(R.id.content);
			radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
			score=(TextView)v.findViewById(R.id.score);
		}
	}

	public RateActivity_Adapter(Context context,
			List<RateActivity_Util> listItems) {
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.list = listItems;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 记录选中分数
	/*
	 * private int checkedChange(int checkId) { return score[checkId]; }
	 */

	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.home_rate_list, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);

			viewHolder.radioGroup.setTag(Integer.valueOf(position));
			viewHolder.title.setText(list.get(position).title);
			viewHolder.content.setText(list.get(position).content);
			int btnNum = (list.get(position).btnNum);
			if (btnNum > 0) {
				String score[] = list.get(position).score.split("#");
				for (int i = 0; i < btnNum; i++) {
					RadioButton rb = (RadioButton) View.inflate(context,
							R.layout.view_group_button, null);
					rb.setId(i);
					rb.setTag(score[i]+"分");
					rb.setText(score[i]+"分");
					Log.v("debug score", position+"行"+score[i]+"分");
					score_arr[position][i]=Integer.parseInt(score[i]);
					viewHolder.radioGroup.addView(rb);
				}
			}

			viewHolder.radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							Log.v("debug1", group.getTag()+"行");
							Log.v("debug",checkedId+"项");
							score_arr[(Integer) group.getTag()][9]=score_arr[(Integer) group.getTag()][checkedId];
							Toast.makeText(context, ""+score_arr[0][9]+"+"+score_arr[1][9]+"+"+score_arr[2][9], Toast.LENGTH_SHORT).show();
							/*RadioButton rbButton = (RadioButton) convertView
									.findViewById(1);*/
							/*if (rbButton.isChecked()) {
								Integer pos = (Integer) group.getTag();
								RateActivity_Util element = (RateActivity_Util) items
										.get(pos);
							String scoreEnd= group.getTag();
							Toast.makeText(context, scoreEnd, Toast.LENGTH_SHORT).show();*/
						}							
					});
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return view;
	}
}
