package com.pjpz.fragment;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pjpz.R;
import com.pjpz.widget.HackyViewPager;
import com.pjpz.widget.ProgressWheel;

public class ImgArticleFragment extends Fragment {
	private ViewPager viewPager;
	private Context context;
	private TextView tvCount;
	private int position;
	public final static String[] imageUrls = new String[] {
			"http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037148_7104.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037129_8825.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037128_5291.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037128_3531.jpg" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_imgarticle, container,
				false);
		tvCount = (TextView) view.findViewById(R.id.tv_count);
		tvCount.setText((position + 1) + "/" + imageUrls.length);
		context = getActivity();
		viewPager = new HackyViewPager(context);
		RelativeLayout layout_img = (RelativeLayout) view
				.findViewById(R.id.layout_img);
		layout_img.addView(viewPager);
		layout_img.removeView(tvCount);
		layout_img.addView(tvCount);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		// 初始化图片异步加载类
		viewPager.setAdapter(new mPagerAdapter(inflater));
		viewPager.setCurrentItem(position);
		return view;
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			tvCount.setText((position + 1) + "/" + imageUrls.length);
		}

	};

	class mPagerAdapter extends PagerAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true).build();
		private LayoutInflater inflater;

		public mPagerAdapter(LayoutInflater inflater) {
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = inflater.inflate(R.layout.layout_imageview, null);
			PhotoView photoView = (PhotoView) view
					.findViewById(R.id.photoView);
			final ProgressWheel progressWheel = (ProgressWheel) view
					.findViewById(R.id.progressWheel);
			ImageLoader.getInstance().displayImage(imageUrls[position],
					photoView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressWheel.setVisibility(View.GONE);
						}
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressWheel.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							progressWheel.setProgress(360 * current / total);
						}
					});
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			((ViewPager) container).removeView(view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
