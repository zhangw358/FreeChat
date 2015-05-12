package com.example.freechat.aidl;
import com.example.freechat.aidl.AIDLChatActivity;

interface AIDLPushService {
	boolean sendMessage(char type, inout byte[] message);
	void registerToPushService(AIDLChatActivity callback);
}
