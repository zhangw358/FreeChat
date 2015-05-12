package com.example.freechat.network;

import java.util.List;

import org.apache.http.NameValuePair;

import com.example.freechat.FCConfigure;
import com.example.freechat.network.FCHttpPostResult.OnGetResultCallBack;

public class FCLoginTask implements OnGetResultCallBack {

	public interface OnLoginFinishedCallBack {
		public void onLoginFinished(int resultCode);
	}

	private OnLoginFinishedCallBack m_callback;
	private List<NameValuePair> m_params;
	private FCHttpPostResult m_result;

	public FCLoginTask() {
	}

	public FCLoginTask(List<NameValuePair> params,
			OnLoginFinishedCallBack callback) {
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
		int resultCode = 0;
		if (!result.equals("")) {
			resultCode = Integer.parseInt(result.trim());
		}
		m_callback.onLoginFinished(resultCode);
	}
}
