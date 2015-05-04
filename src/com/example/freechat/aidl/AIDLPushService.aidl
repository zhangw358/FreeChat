package com.example.freechat.aidl;
import com.example.freechat.aidl.AIDLChatActivity;

interface AIDLPushService {
	boolean sendMessage(String message);
	void registerToPushService(AIDLChatActivity callback);
}
