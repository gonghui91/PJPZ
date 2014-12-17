package com.pjpz.fragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
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
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pjpz.R;
import com.pjpz.widget.HackyViewPager;

public class ImgArticleFragment extends Fragment {
	private ViewPager viewPager;
	private Context context;
	private TextView tvCount;

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
		context = getActivity();
		viewPager = new HackyViewPager(context);
		RelativeLayout layout_img = (RelativeLayout) view
				.findViewById(R.id.layout_img);
		layout_img.addView(viewPager);
		layout_img.removeView(tvCount);
		layout_img.addView(tvCount);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		// 初始化图片异步加载类
		viewPager.setAdapter(new mPagerAdapter());
		viewPager.setCurrentItem(0);
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
				.showImageOnLoading(R.drawable.imageloading)
				.showImageForEmptyUri(R.drawable.imageloading)
				.showImageOnFail(R.drawable.imageloading).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			final PhotoView photoView = new PhotoView(context);
			ImageLoader.getInstance().loadImage(imageUrls[position], options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							photoView.setImageResource(R.drawable.imageloading);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							photoView
									.setImageResource(R.drawable.imageloadfail);

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							photoView.setImageBitmap(loadedImage);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
						}
					});
			// photoView.setImageResource(sDrawables[position]);
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return photoView;
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
