package com.pjpz.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.model.Periodical;
import com.pjpz.ui.CatalogActivity;

public class ImgListAdapter extends BaseAdapter {
	private Context context;
	private List<Periodical> periodicals;

	public ImgListAdapter(Context context, List<Periodical> periodicals) {
		this.context = context;
		this.periodicals = periodicals;
	}

	@Override
	public int getCount() {
		return periodicals.size();
	}

	@Override
	public Object getItem(int position) {
		return periodicals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImgViewHolder holder = null;
		final Periodical periodical = periodicals.get(position);
		if (convertView == null) {
			holder = new ImgViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.txt_item, null);
			holder.logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			holder.name = (TextView) convertView.findViewById(R.id.tv_pname);
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.time = (TextView) convertView.findViewById(R.id.tv_syncTime);
			convertView.setTag(holder);
		} else {
			holder = (ImgViewHolder) convertView.getTag();
		}
		holder.logo.setImageResource(R.drawable.zhuye_05);
		holder.name.setText(periodical.name);
		holder.summary.setText(periodical.summary);
		holder.time.setText(periodical.syncTime);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CatalogActivity.class);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ImgViewHolder {
		ImageView logo;
		TextView name;
		TextView summary;
		TextView time;
	}
}
