package com.example.freechat.ui.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.freechat.R;
import com.example.freechat.network.FCRegisterTask;
import com.example.freechat.network.FCRegisterTask.OnRegisterFinishedCallBack;
import com.example.freechat.ui.FCActionBarActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FCRegisterActivity extends FCActionBarActivity implements OnRegisterFinishedCallBack {

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
				startRegisterTask();
			}
		});
	}
	
	private void startRegisterTask() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "register"));
		params.add(new BasicNameValuePair("username", "halfish"));
		params.add(new BasicNameValuePair("password", "halfish"));
		params.add(new BasicNameValuePair("mail", "shirhalfish@gmail.com"));
		
		new FCRegisterTask(params, this).start();
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

	@Override
	public void onRegisterFinished(int returnCode) {
		
	}

}
