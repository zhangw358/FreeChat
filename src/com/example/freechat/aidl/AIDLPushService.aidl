package com.example.freechat.aidl;
import com.example.freechat.aidl.AIDLChatActivity;

interface AIDLPushService {
	boolean sendMessage(String from, String to, int timeStamp, String content);
	void registerToPushService(AIDLChatActivity callback);
}
