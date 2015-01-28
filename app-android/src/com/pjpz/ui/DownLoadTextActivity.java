package com.pjpz.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pjpz.R;
import com.pjpz.model.DownLoad;

public class DownLoadTextActivity extends BaseActivity {
	private TextView tv_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_load_text);
		initView();
	}

	private void initView() {
		DownLoad downLoad = (DownLoad) getIntent().getSerializableExtra("downLoad");
		actionBar.setTitle(downLoad.articleName);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(downLoad.content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.down_load_text, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			DownLoadTextActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
