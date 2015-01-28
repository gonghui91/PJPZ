package com.pjpz.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.utils.SPUtils;

public class PreferenceActivity extends Activity {
	private EditText etIp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preference);
		etIp = (EditText) findViewById(R.id.etx_ip);
		etIp.setText(Constants.url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preference, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Constants.url = etIp.getText().toString();
		SPUtils.put(PreferenceActivity.this, "url", etIp.getText().toString());
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
