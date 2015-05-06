package com.example.freechat.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.freechat.R;
import com.example.freechat.network.FCLoginTask;
import com.example.freechat.network.FCLoginTask.OnLoginFinishedCallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FCLoginActivity extends Activity implements
		OnLoginFinishedCallBack {

	private Button regButton;
	private Button loginButton;
	private EditText et_userName;
	private EditText et_passWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("FreeChat");
		regButton = (Button) findViewById(R.id.bt_goto_reg);
		loginButton = (Button) findViewById(R.id.bt_login);
		et_userName = (EditText) findViewById(R.id.et_username);
		et_passWord = (EditText) findViewById(R.id.et_password);

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FCLoginActivity.this,
						FCMainActivity.class);
				startActivity(intent);
				finish();
				startLoginTask();
			}
		});

		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FCLoginActivity.this,
						FCRegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void startLoginTask() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "login"));
		params.add(new BasicNameValuePair("username", et_userName.getText().toString()));
		params.add(new BasicNameValuePair("password", et_passWord.getText().toString()));

		new FCLoginTask(params, this).start();
	}

	@Override
	public void onLoginFinished(int returnCode) {

	}

}
