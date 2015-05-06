package com.example.freechat.network;

import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.example.freechat.FCConfigure;
import com.example.freechat.network.FCHttpPostResult.OnGetResultCallBack;

public class FCRegisterTask implements OnGetResultCallBack {

	public interface OnRegisterFinishedCallBack {
		public void onRegisterFinished(int returnCode);
	}

	private OnRegisterFinishedCallBack m_callback;
	private List<NameValuePair> m_params;
	private FCHttpPostResult m_result;

	public FCRegisterTask() {
	}

	public FCRegisterTask(List<NameValuePair> params,
			OnRegisterFinishedCallBack callback) {
		m_params = params;
		m_callback = callback;
	}

	public void start() {
		m_result = new FCHttpPostResult(FCConfigure.SERVER_HTTP_ADDR, m_params,
				this);
		m_result.queryBegin(3000);
	}

	@Override
	public void onResult(String result) {
		int resultCode = 110;
		m_callback.onRegisterFinished(resultCode);
		Log.e("Halfish register", result);
	}
}
