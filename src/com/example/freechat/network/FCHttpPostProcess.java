package com.example.freechat.network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

public class FCHttpPostProcess {
	
	public String getDataFromUri(String uri, List<NameValuePair> params, int timeOut) {
		String result = null;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(uri);
		HttpResponse response = null;
		if (timeOut > 0) {
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					timeOut);
			HttpConnectionParams.setSoTimeout(client.getParams(), timeOut);
		}

		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			request.setEntity(entity);
			
			response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (result == null) {
			result = "";
		}
		return result;
	}
}
