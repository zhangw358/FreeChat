package com.example.freechat.ui.activity;

import java.io.File;
import com.example.freechat.FCConfigure;
import com.example.freechat.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

public class FCStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zx_start);
		String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/freechat/";
		File destDir = new File(s);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		SharedPreferences sharedPreferences = getSharedPreferences("text", Context.MODE_PRIVATE);
		String name = sharedPreferences.getString("name", ""); 
		String passcode = sharedPreferences.getString("passcode", ""); 
		if (name == "" || passcode == "") {
			Intent intent = new Intent(FCStartActivity.this, FCLoginActivity.class);
			startActivity(intent);
			finish();
		}
		else {
			FCConfigure.myName = name;
			s = s + name + "/";
			File Dir = new File(s);
			if (!Dir.exists()) {
				Dir.mkdirs();
			}
			Intent intent = new Intent(FCStartActivity.this, FCMainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
