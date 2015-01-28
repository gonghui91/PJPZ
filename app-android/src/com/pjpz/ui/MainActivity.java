package com.pjpz.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pjpz.R;
import com.pjpz.model.Category;
import com.pjpz.ui.fragment.BaseFragmentV4;
import com.pjpz.ui.fragment.PeriodicalFragment;
import com.pjpz.utils.IntentUtils;
import com.pjpz.utils.ToastUtils;

public class MainActivity extends BaseActivity {
	private Button btnImg, btnTxt;
	private Category mCategory;
	private PeriodicalFragment mContentFragment;
	private static boolean isExit = false;
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 11:
				isExit = false;
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setTitle(R.string.app_name);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		setContentView(R.layout.activity_main);
		initView();
	}

	

	private void initView() {
		btnTxt = (Button) findViewById(R.id.btn_txtshow);
		btnImg = (Button) findViewById(R.id.btn_imgshow);
		btnTxt.setOnClickListener(onClickListener);
		btnImg.setOnClickListener(onClickListener);
		btnTxt.performClick();
		// setCategory(Category.text);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_txtshow:
				setCategory(Category.text);
				btnTxt.setBackgroundResource(R.drawable.txt_select_true);
				btnImg.setBackgroundResource(R.drawable.img_select_false);
				btnTxt.setTextColor(getResources().getColor(R.color.white));
				btnImg.setTextColor(getResources().getColor(R.color.main_bg));
				break;

			case R.id.btn_imgshow:
				setCategory(Category.image);
				btnTxt.setBackgroundResource(R.drawable.txt_select_false);
				btnImg.setBackgroundResource(R.drawable.img_select_true);
				btnTxt.setTextColor(getResources().getColor(R.color.main_bg));
				btnImg.setTextColor(getResources().getColor(R.color.white));
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_downloads) {
			IntentUtils.startIntent(MainActivity.this, DownLoadActivity.class);
		}
		return super.onOptionsItemSelected(item);
	}

	public void setCategory(Category category) {
		if (mCategory == category) {
			return;
		}
		mCategory = category;
		setTitle(mCategory.getDisplayName());
		mContentFragment = PeriodicalFragment.newInstance(category);
		replaceFragment(R.id.content, mContentFragment);
	}

	protected void replaceFragment(int viewId, BaseFragmentV4 fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(viewId, fragment).commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			exit();
			return false;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		 if (!isExit) {
	            isExit = true;
	            ToastUtils.showShort("再按一次退出程序");
	            // 利用handler延迟发送更改状态信息
	            mHandler.sendEmptyMessageDelayed(11, 2000);
	        } else {
	            finish();
	            System.exit(0);
	        }
	}
}
