package com.example.freechat.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *  Created by zhangwei on 15-4-7.
 *
 *  usage:
 *
 * 	UDPReceiver rec = new UDPReceiver(port);
 * 	rec.setCallBack( new UDPReceiver.OnReceiveCallBack() {
 * 		onReceive(byte[] data);
 * 	})
 * 	rec.start();	// will execute in another thread
 * 	rec.stop();
 * 	rec = null;
 *
 */

public class FCUDPReceiver implements Runnable {
    private static final String LOG_TAG = "UDP Receiver";
    protected static int MAX_PACKET_DATA_LENGTH = 1024 * 1024 * 2;

    protected OnReceiveCallback m_callback = null;
    protected int m_port = 6650;
    protected DatagramSocket m_socket;
    protected boolean m_bRunning  = false;
    protected Handler m_handler = null;

    private static final int RECEIVE_DATA = 1;

    public interface OnReceiveCallback {
        public void onReceive(String ip, byte [] data);
    }

    public FCUDPReceiver(int port) {
        m_port = port;
        m_handler = new ReceiveHandler(this);
    }

    public void setCallback(OnReceiveCallback cb) {
        m_callback = cb;
    }

    public OnReceiveCallback getCallback() {
        return m_callback;
    }

    public boolean start() {
        try {
            m_socket = new DatagramSocket(m_port);
            m_bRunning = true;
            new Thread(this).start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "initial socket failed!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stop() {
        m_bRunning = false;
        if (m_socket != null) {
            m_socket.close();
            m_socket = null;
        }
    }

    class ReceiveMessage {
        public String ip;
        public byte [] data;

        public ReceiveMessage(DatagramPacket dp) {
            this.ip = dp.getAddress().getHostAddress();
            byte [] dpData = dp.getData();
            int offset = dp.getOffset();
            int length = dp.getLength();
            this.data = new byte[length];
            System.arraycopy(dpData, offset, this.data, 0, length);
        }
    }

    static class ReceiveHandler extends Handler {
        WeakReference<FCUDPReceiver> m_receive;

        public ReceiveHandler(FCUDPReceiver receiver) {
            m_receive = new WeakReference<FCUDPReceiver>(receiver);
        }

        @Override
        public void handleMessage (Message msg) {
            if (msg.what == RECEIVE_DATA) {
                FCUDPReceiver rec = m_receive.get();
                if (rec != null && rec.getCallback() != null) {
                    ReceiveMessage rcmsg = (ReceiveMessage) msg.obj;
                    rec.getCallback().onReceive(rcmsg.ip, rcmsg.data);
                }
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void run() {
        byte[] data = new byte[MAX_PACKET_DATA_LENGTH];
        while (m_bRunning) {
            DatagramPacket packet = new DatagramPacket(data, 0, data.length);
            try {
                m_socket.receive(packet);
            } catch (IOException e) {
                Log.d(LOG_TAG, e.getMessage());
                continue;
            }

            Message msg = m_handler.obtainMessage(RECEIVE_DATA, new ReceiveMessage(packet));
            m_handler.sendMessage(msg);
        }
        Log.d(LOG_TAG, "receive thread end");

    }

}
