package com.example.freechat.network;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public class FCHttpPostResult {
	/*
	 * get result by implementing this interface
	 */
	public static interface OnGetResultCallBack {
		public void onResult(String result);
	}

	private int m_timeOut;
	private FCHttpPostProcess m_process;
	private OnGetResultCallBack m_callBack;
	private List<NameValuePair> m_params;
	private GetTask m_task = null;
	private String m_uri;

	public FCHttpPostResult(String uri, List<NameValuePair> params, OnGetResultCallBack callback) {
		m_uri = uri;
		m_params = params;
		m_callBack = callback;
		m_process = new FCHttpPostProcess();
	}
	
	public boolean queryBegin(int timeOut) {
		if (isQuerying()) {
			return false;
		}
		m_timeOut = timeOut;
		m_task = new GetTask();
		m_task.execute();
		
		return true;
	}
	
	private boolean isQuerying() {
		return m_task != null;
	}
	
	private class GetTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String result = m_process.getDataFromUri(m_uri, m_params, m_timeOut);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (m_callBack != null) {
				m_callBack.onResult(result);
			}
			super.onPostExecute(result);
			m_task = null;
		}
	}
}
