package com.pjpz.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pjpz.App;
import com.pjpz.R;
import com.pjpz.data.Constant;
import com.pjpz.fragment.ImgFragment;
import com.pjpz.fragment.TxtFragment;
import com.pjpz.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {
	private TxtFragment txtFragment;
	private ImgFragment imgFragment;
	private FragmentManager fragmentManager;
	private Button btnImg, btnTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
	}

	private void initView() {
		btnTxt = (Button) findViewById(R.id.btn_txtshow);
		btnImg = (Button) findViewById(R.id.btn_imgshow);
		btnTxt.setOnClickListener(onClickListener);
		btnImg.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_txtshow:
				App.STATUS = Constant.Text;
				setTabSelection(0);
				break;

			case R.id.btn_imgshow:
				App.STATUS = Constant.Image;
				setTabSelection(1);
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
		case 0: // 当点击了文字版
			btnTxt.setBackgroundResource(R.drawable.txt_select_true);
			if (txtFragment == null) {
				txtFragment = new TxtFragment();
				transaction.add(R.id.content, txtFragment);
			} else {
				transaction.show(txtFragment);
			}
			break;

		case 1: // 当点击了图片版
			btnImg.setBackgroundResource(R.drawable.img_select_true);
			if (imgFragment == null) {
				imgFragment = new ImgFragment();
				transaction.add(R.id.content, imgFragment);
			} else {
				transaction.show(imgFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		btnTxt.setBackgroundResource(R.drawable.txt_select_false);
		btnImg.setBackgroundResource(R.drawable.img_select_false);
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (txtFragment != null) {
			transaction.hide(txtFragment);
		}
		if (imgFragment != null) {
			transaction.hide(imgFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
