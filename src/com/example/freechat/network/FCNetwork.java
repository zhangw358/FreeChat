package com.example.freechat.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * 网络检测工具
 */
public class FCNetwork {
	
	/*
	 * 目前是通过IP地址来检测是否上网，不能检测是否能上到外网（当连在一个没接外网的WIFI上时）
	 */
	public static boolean isConnectingToInternet(Context context) {
		 ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		 if (connectivity != null) {
			 NetworkInfo[] info = connectivity.getAllNetworkInfo();
			 if (info != null) {
				 for (int i = 0; i < info.length; i++) {
					 if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						 return true;
					 }
				 }
			 }
				 
		 }
		 return false;
	}
	
	/*
	 * 获取IPv4的网络地址
	 */
 	public static String getIPv4Address() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase(Locale.ENGLISH);
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (isIPv4) 
                            return sAddr;
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "0.0.0.0";
    }
}
