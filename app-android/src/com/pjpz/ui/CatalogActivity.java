package com.pjpz.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.pjpz.R;
import com.pjpz.adapter.CataLogAdapter;
import com.pjpz.ui.base.BaseActivity;

public class CatalogActivity extends BaseActivity {
	private ListView cataList;
	private ImageView goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);
		initView();
	}

	private void initView() {
		goback = (ImageView) findViewById(R.id.iv_goback);
		goback.setOnClickListener(onClickListener);
		cataList = (ListView) findViewById(R.id.catalogList);
		cataList.setAdapter(new CataLogAdapter(CatalogActivity.this));
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_goback:
				CatalogActivity.this.finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.catalog, menu);
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
