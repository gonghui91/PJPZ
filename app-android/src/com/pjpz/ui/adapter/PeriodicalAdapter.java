package com.pjpz.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.ImageCacheManager;
import com.pjpz.model.Periodical;
import com.pjpz.utils.DensityUtils;

public class PeriodicalAdapter extends BaseAdapter {
	private Context context;
	private List<Periodical> periodicals;

	private LayoutInflater mLayoutInflater;
	private Resources mResource;
	private Drawable mDefaultImageDrawable;
	private static final int IMAGE_MAX_HEIGHT = 120;

	public PeriodicalAdapter(Context context, List<Periodical> periodicals,
			LayoutInflater mLayoutInflater) {
		this.context = context;
		this.periodicals = periodicals;
		mResource = context.getResources();
		this.mLayoutInflater = mLayoutInflater;
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
		TxtViewHolder holder = null;
		final Periodical periodical = periodicals.get(position);
		if (convertView == null) {
			holder = new TxtViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.txt_item, null);
			holder.logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			holder.name = (TextView) convertView.findViewById(R.id.tv_pname);
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			holder.time = (TextView) convertView.findViewById(R.id.tv_syncTime);
			convertView.setTag(holder);
		} else {
			holder = (TxtViewHolder) convertView.getTag();
		}
		if (holder.imageRequest != null) {
			holder.imageRequest.cancelRequest();
		}
		mDefaultImageDrawable = new ColorDrawable(
				mResource.getColor(Constants.COLORS[position % Constants.COLORS.length]));
		holder.name.setText(periodical.name);
		holder.summary.setText(periodical.summary);
		holder.time.setText(periodical.syncTime);
		holder.imageRequest = ImageCacheManager.loadImage(periodical.logoUrl,
//				holder.imageRequest = ImageCacheManager.loadImage(Constant.imageUrls[position],
				ImageCacheManager.getImageListener(holder.logo,
						mDefaultImageDrawable, mDefaultImageDrawable), 0,
				DensityUtils.dip2px(context, IMAGE_MAX_HEIGHT));

		return convertView;
	}

	class TxtViewHolder {
		ImageView logo;
		TextView name;
		TextView summary;
		TextView time;
		ImageLoader.ImageContainer imageRequest;
	}

	public void set(ArrayList<Periodical> periodicals){
		this.periodicals = periodicals;
		this.notifyDataSetChanged();
	}
}
