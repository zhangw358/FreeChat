package com.example.freechat.ui.activity;

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
import android.widget.EditText;
import android.widget.Toast;

public class FCRegisterActivity extends FCActionBarActivity implements OnRegisterFinishedCallBack {

	private Button m_regButton;
	private EditText m_userName;
	private EditText m_passWord;
	private EditText m_confirm_passWord;
	private EditText m_emailAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setActionBarCenterTitle("Get Account");

		initWidgets();
	}

	private void initWidgets() {
		
		m_regButton = (Button) findViewById(R.id.bt_reg);
		m_userName = (EditText) findViewById(R.id.et_newusername);
		m_passWord = (EditText) findViewById(R.id.et_newpassword);
		m_confirm_passWord = (EditText) findViewById(R.id.et_checkpassword);
		m_emailAddress = (EditText) findViewById(R.id.et_emailAddress);
		
		m_regButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String password = m_passWord.getText().toString();
				String confirmPassword = m_confirm_passWord.getText().toString();
				if (password.equals(confirmPassword)) {
					startRegisterTask();
				}
				else {
					Toast.makeText(getApplicationContext(), "wrong Password", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void startRegisterTask() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "register"));
		params.add(new BasicNameValuePair("username", m_userName.getText().toString()));
		params.add(new BasicNameValuePair("password", m_passWord.getText().toString()));
		params.add(new BasicNameValuePair("mail", m_emailAddress.getText().toString()));
		
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
