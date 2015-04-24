package com.example.freechat.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FCHttpUtil {

	public static String LOG_TAG = FCHttpUtil.class.getName();
	public static final int POST_SUCCESS = 1000;
	public static final int POST_FAILED = 1010;

	private Handler m_handler;
	private String m_uri;
	List<NameValuePair> m_params;

	public FCHttpUtil() {
	}

	public FCHttpUtil(Handler handler) {
		m_handler = handler;
	}

	public void setHandler(Handler handler) {
		m_handler = handler;
	}

	public void post(String uri, List<NameValuePair> params) {
		m_uri = uri;
		
		if (params == null) {
			params = new ArrayList<NameValuePair>();
		}
		m_params = params;

		new Thread(new PostRunnable()).start();
	}
	
	private class PostRunnable implements Runnable {
		@Override
		public void run() {
			Log.e("halfish", "postRunnable run");
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(m_uri);

			try {
				HttpEntity entity = new UrlEncodedFormEntity(m_params, "utf-8");
				request.setEntity(entity);
				Log.e("halfish", "1111111");
				HttpResponse response = client.execute(request);
				Log.e("halfish", "2222222");

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					Log.e("Halfish", "post get response successfully");
					
					String result = EntityUtils.toString(response.getEntity());
					Bundle bundle = new Bundle();
					bundle.putString("postResult", result);
					
					Message msg = new Message();
					msg.what = POST_SUCCESS;
					msg.setData(bundle);
					
					if (m_handler != null) {
						m_handler.sendMessage(msg);
					} else {
						Log.e(LOG_TAG, "post handler is null!");
					}
				} else {
					Log.e("Halfish", "post get response failed");
					
					Bundle bundle = new Bundle();
					bundle.putInt("status code", statusCode);
					
					Message msg = new Message();
					msg.what = POST_FAILED;
					msg.setData(bundle);
					
					if(m_handler != null) {
						m_handler.sendMessage(msg);
					} else {
						Log.e(LOG_TAG, "post handler is null!");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
