package com.example.freechat.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FCFileHelper {

	private static final int BUFFLEN = 1024;

	private Context m_context;
	private String m_dataPath; // /data/data/com.example.package/

	public FCFileHelper(Context context) {
		m_context = context;
		m_dataPath = m_context.getFilesDir().getPath() + "/";
	}

	public boolean isDataFileExist(String fileName) {
		File file = new File(m_dataPath + fileName);
		return file.exists();
	}

	public boolean copyFileTo(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory() || destFile.isDirectory())
			return false;// 判断是否是文件
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		int readLen = 0;
		byte[] buf = new byte[BUFFLEN];
		while ((readLen = fis.read(buf)) != -1) {
			fos.write(buf, 0, readLen);
		}
		fos.flush();
		fos.close();
		fis.close();
		return true;
	}
}
