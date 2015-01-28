package com.pjpz.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pjpz.R;
import com.pjpz.dao.DownloadsDataHelper;
import com.pjpz.model.DownLoad;
import com.pjpz.ui.adapter.DownLoadAdapter;

public class DownLoadActivity extends BaseActivity {
	private ListView downloadList;
	private DownloadsDataHelper mDataHelper;
	private DownLoadAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		initView();
	}

	private void initView() {
		actionBar.setTitle(R.string.download);
		downloadList = (ListView) findViewById(R.id.downloadList);
		mDataHelper = new DownloadsDataHelper(this);
		ArrayList<DownLoad> list = mDataHelper.list();
		mAdapter = new DownLoadAdapter(this, list,mDataHelper);
		downloadList.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.down_load, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			DownLoadActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
