package com.example.freechat.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.freechat.FCConfigure;
import com.example.freechat.R;
import com.example.freechat.network.FCLoginTask;
import com.example.freechat.network.FCLoginTask.OnLoginFinishedCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FCLoginActivity extends Activity implements
		OnLoginFinishedCallBack {

	private Button regButton;
	private Button loginButton;
	private EditText et_userName;
	private EditText et_passWord;

	public static final int LOGIN_SUCCESS = 200;
	public static final int LOGIN_WRONG_PASSWD = 210;
	public static final int LOGIN_NO_ACCOUNT = 211;

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
		switch (returnCode) {
		case LOGIN_SUCCESS:
			Toast.makeText(this, "Login Successfully !", Toast.LENGTH_SHORT)
					.show();
			SharedPreferences sharedPreferences = getSharedPreferences("text", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("name", et_userName.getText().toString());
			editor.putString("passcode", et_passWord.getText().toString());
			editor.commit();
			FCConfigure.myName = et_userName.getText().toString();
			String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/freechat/";
			s = s + FCConfigure.myName + "/";
			File Dir = new File(s);
			if (!Dir.exists()) {
				Dir.mkdirs();
			}
			
			Intent intent = new Intent(FCLoginActivity.this,
					FCMainActivity.class);
			startActivity(intent);
			finish();
			break;

		case LOGIN_WRONG_PASSWD:
			Toast.makeText(this, "WRONG PASSWORD!", Toast.LENGTH_SHORT).show();
			break;

		case LOGIN_NO_ACCOUNT:
			Toast.makeText(this, "NO ACCOUNT EXIST!", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			Intent intent2 = new Intent(FCLoginActivity.this,
					FCMainActivity.class);
			startActivity(intent2);
			finish();
			Toast.makeText(this, "Wrong ReturnCode From Server",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
