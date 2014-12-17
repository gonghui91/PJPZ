package com.pjpz.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pjpz.R;
import com.pjpz.fragment.CommentFragment;
import com.pjpz.fragment.ImgArticleFragment;
import com.pjpz.ui.base.BaseActivity;
import com.pjpz.widget.MorePopupWindow;

public class ImgArticleActivity extends BaseActivity {
	private ImgArticleFragment articleFragment;
	private CommentFragment commentFragment;
	private FragmentManager fragmentManager;
	private ImageView iv_more;
	private TextView menuName;
	private ImageView btnGoback;
	private String articleName = "白鹿就是我的根";
	private PopupWindow morePopup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		initView();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
	}

	private void initView() {
		iv_more = (ImageView) findViewById(R.id.iv_more);
		iv_more.setOnClickListener(onClickListener);
		menuName = (TextView) findViewById(R.id.tv_acticleName);
		btnGoback = (ImageView) findViewById(R.id.iv_goback);
		btnGoback.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_goback:
				if (articleFragment.isVisible()) {
					// 文章界面是退出文章
					ImgArticleActivity.this.finish();
				} else if (commentFragment.isVisible()) {
					// 退出评论到文章
					setTabSelection(0);
				}
				break;

			case R.id.iv_more:
				showMorePop();
				break;
			case R.id.tv_praise:
				morePopup.dismiss();
				// ToastUtils.showShort(getString(R.string.tv_praise));
				break;
			case R.id.tv_comment:
				morePopup.dismiss();
				setTabSelection(1);
				break;
			case R.id.tv_collect:
				morePopup.dismiss();
				break;
			case R.id.tv_share:
				morePopup.dismiss();
				break;
			}
		}

	};

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。
	 */
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			// 文章内容
			iv_more.setVisibility(View.VISIBLE);
			menuName.setText(articleName);
			if (articleFragment == null) {
				articleFragment = new ImgArticleFragment();
				transaction.add(R.id.content, articleFragment);
			} else {
				transaction.show(articleFragment);
			}
			break;

		case 1:
			// 评论
			iv_more.setVisibility(View.INVISIBLE);
			menuName.setText(getString(R.string.comment));
			if (commentFragment == null) {
				commentFragment = new CommentFragment();
				transaction.add(R.id.content, commentFragment);
			} else {
				transaction.show(commentFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (articleFragment != null) {
			transaction.hide(articleFragment);
		}
		if (commentFragment != null) {
			transaction.hide(commentFragment);
		}
	}

	/**
	 * 展示更多选项的popupwindow
	 */
	private void showMorePop() {
		morePopup = new MorePopupWindow(ImgArticleActivity.this, onClickListener);
		morePopup.showAsDropDown(iv_more);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.article, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
