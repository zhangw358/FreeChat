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

public class FCLoginActivity extends Activity implements
		OnLoginFinishedCallBack {

	private Button regButton;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("FreeChat");
		regButton = (Button) findViewById(R.id.bt_goto_reg);
		loginButton = (Button) findViewById(R.id.bt_login);

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
		params.add(new BasicNameValuePair("username", "halfish"));
		params.add(new BasicNameValuePair("password", "halfish"));

		new FCLoginTask(params, this).start();
	}

	@Override
	public void onLoginFinished(int returnCode) {

	}

}
