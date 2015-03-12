package com.pjpz.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pjpz.R;
import com.pjpz.data.ImageCacheManager;
import com.pjpz.model.Category;
import com.pjpz.model.Periodical;
import com.pjpz.utils.DensityUtils;

public class PeriodicalAdapter1 extends CursorAdapter {
	private static final int[] COLORS = { R.color.holo_blue_light,
			R.color.holo_green_light, R.color.holo_orange_light,
			R.color.holo_purple_light, R.color.holo_red_light };

	private static final int IMAGE_MAX_HEIGHT = 140;

	private LayoutInflater mLayoutInflater;

	private Drawable mDefaultImageDrawable;

	private Resources mResource;
	private Category category;

	public PeriodicalAdapter1(Context context, LayoutInflater mInflater,
			Category category) {
		super(context, null, false);
		mResource = context.getResources();
		mLayoutInflater = mInflater;
		this.category = category;
	}

	public Periodical getItem(int position) {
		mCursor.moveToPosition(position);
		return Periodical.fromCursor(category,mCursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return mLayoutInflater.inflate(R.layout.txt_item, null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.txt_item, null);
			holder.logo = (ImageView) view.findViewById(R.id.iv_logo);
			holder.name = (TextView) view.findViewById(R.id.tv_pname);
			holder.summary = (TextView) view.findViewById(R.id.tv_summary);
			holder.time = (TextView) view.findViewById(R.id.tv_syncTime);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (holder.imageRequest != null) {
			holder.imageRequest.cancelRequest();
		}
		// view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
		// + mListView.getHeaderViewsCount()));
		Periodical periodical = Periodical.fromCursor(category,cursor);
		mDefaultImageDrawable = new ColorDrawable(
				mResource.getColor(COLORS[cursor.getPosition() % COLORS.length]));
		holder.name.setText(periodical.name);
		holder.summary.setText(periodical.summary);
		holder.time.setText(periodical.syncTime);
		holder.imageRequest = ImageCacheManager.loadImage(periodical.logoUrl,
				ImageCacheManager.getImageListener(holder.logo,
						mDefaultImageDrawable, mDefaultImageDrawable), 0,
				DensityUtils.dip2px(context, IMAGE_MAX_HEIGHT));

	}

	class ViewHolder {
		ImageView logo;
		TextView name;
		TextView summary;
		TextView time;
		ImageLoader.ImageContainer imageRequest;
	}
}
