package com.example.freechat.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.freechat.FCConfigure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class FCRegisterProcess {

	private Activity m_mainActivity;
	private RegisterHandler m_handler;
	private List<NameValuePair> m_params;

	public FCRegisterProcess() {
	}

	public FCRegisterProcess(Activity activity, String username, String password,
			String email) {
		m_mainActivity = activity;
		m_params = new ArrayList<NameValuePair>();
		m_params.add(new BasicNameValuePair("action", "register"));
		m_params.add(new BasicNameValuePair("username", username));
		m_params.add(new BasicNameValuePair("password", password));
		m_params.add(new BasicNameValuePair("email", email));
	}

	public void start() {
		FCHttpUtil httpUtil = new FCHttpUtil();
		m_handler = new RegisterHandler();
		httpUtil.setHandler(m_handler);
		httpUtil.post(FCConfigure.SERVER_ADDR, m_params);
	}

	@SuppressLint("HandlerLeak") 
	private class RegisterHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FCHttpUtil.POST_SUCCESS:
				Toast.makeText(m_mainActivity, "Successful", Toast.LENGTH_SHORT)
						.show();
				break;

			case FCHttpUtil.POST_FAILED:
				Toast.makeText(m_mainActivity, "Failed", Toast.LENGTH_SHORT)
				.show();
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	}
}
