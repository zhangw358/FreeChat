package com.example.freechat.ui.activity;

import com.example.freechat.R;
import com.example.freechat.network.FCRegisterProcess;
import com.example.freechat.ui.FCActionBarActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FCRegisterActivity extends FCActionBarActivity {

	private Button m_regButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setActionBarCenterTitle("Get Account");

		initWidgets();
	}

	private void initWidgets() {
		Toast.makeText(this, "initWidget", Toast.LENGTH_SHORT).show();
		Log.e("Halfish", "initWidget");
		m_regButton = (Button) findViewById(R.id.bt_reg);
		m_regButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.e("Halfish", "register Button clicked");
				new FCRegisterProcess(FCRegisterActivity.this, "halfish",
						"halfish", "shirehalfish@gmail.com").start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fcregister, menu);
		return true;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
