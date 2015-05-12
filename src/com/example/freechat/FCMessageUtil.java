package com.example.freechat;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.freechat.ui.FCMessage;

public class FCMessageUtil {

	// send message to server
	public static JSONArray messageToJson(FCMessage message, String toUser) {

		String content = message.getContent();
		int type = message.getMessageType();

		JSONArray jsonArray = new JSONArray();
		switch (type) {
		case FCMessage.TYPE_TXT:
			jsonArray.put("send_message");
			jsonArray.put(FCConfigure.myName);
			jsonArray.put(toUser);
			jsonArray.put(content);
			break;

		case FCMessage.TYPE_PIC:
			break;

		case FCMessage.TYPE_AUD:
			break;

		default:
			break;
		}

		return jsonArray;
	}

	// get data from server
	public static FCMessage jsonArrayToMessage(JSONArray jsonArray) {
		FCMessage message = null;

		try {
			String content = jsonArray.getString(2);
			message = new FCMessage(content, FCMessage.RECEIVE_MESSAGE);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return message;
	}
	
	public static int byteToInt(byte[] src, int offset) {
		int value = -1;

		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));

		return value;
	}
	
	public static byte[] intToByte(int value) {
		byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);
		return src;    
	}
	
	
}
