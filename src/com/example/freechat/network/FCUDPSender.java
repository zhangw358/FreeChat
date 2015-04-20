package com.example.freechat.network;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by zhangwei on 15-4-7.

 * usage :
 *
 * UDPSender sender = new UDPSender(String ip, int port)
 * sender.send(byte[] data)		// will be execute in another thread
 * sender.close();
 * sender = null;
 *
 */

public class FCUDPSender {
    private static final String LOG_TAG = "UDP Sender";

    protected DatagramSocket m_socket;
    protected int m_destPort = 6650;
    protected InetAddress m_destAddress;

    protected boolean bRunning = false;

    public FCUDPSender (String destIp, int destPort) {
        setupSocket(destIp, destPort);
    }

    public void close() {
        releaseSocket();
    }

    protected void setupSocket(String destIp, int destPort) {
        try {
            if (m_socket == null) {
                m_socket = new DatagramSocket();
            }
            m_destAddress = InetAddress.getByName(destIp);
            m_destPort = destPort;

        } catch(Exception e) {
            e.printStackTrace();
            m_socket = null;
        }
    }

    protected void releaseSocket() {
        if (m_socket != null) {
            m_socket.close();
            m_socket = null;
        }
    }

    public void send(byte [] data) {
        if (m_socket == null) {
            Log.e(LOG_TAG, "socket hasn't been initialize");
        }

        SendThread thread = new SendThread(data);
        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected class SendThread extends Thread {
        byte [] m_data = null;

        public SendThread(byte [] data) {
            m_data = data;
        }

        @Override
        public void run() {
            if (m_data == null) {
                return;
            }

            DatagramPacket packet = new DatagramPacket(m_data, m_data.length, m_destAddress, m_destPort);

            try {
                m_socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
