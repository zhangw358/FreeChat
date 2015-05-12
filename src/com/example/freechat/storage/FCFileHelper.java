package com.example.freechat.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.freechat.FCConfigure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class FCFileHelper {

	private static final int BUFFLEN = 1024;

	private Context m_context;
	private String m_dataPath; // /data/data/com.example.package/
	private String m_sdPath;
	private SimpleDateFormat formatter;

	public FCFileHelper(Context context) {
		m_context = context;
		m_dataPath = m_context.getFilesDir().getPath() + "/freechat/"
				+ FCConfigure.myName + "/";
		m_sdPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/";
		formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	}

	public boolean isDataFileExist(String fileName) {
		File file = new File(m_dataPath + fileName);
		return file.exists();
	}

	public String generateFileName() {
		// TODO filename;
		Date curDate = new Date(System.currentTimeMillis());
		return m_sdPath + formatter.format(curDate);
	}

	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	public void writeToFile(String fileName, byte[] src) {
		File file = null;
		OutputStream output = null;
		try {
			file = createSDFile(fileName);
			output = new FileOutputStream(file);
			output.write(src);
			output.flush();
			Toast.makeText(m_context, "save!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] loadFile(String fileName) {
		try {
			FileInputStream in = m_context.openFileInput(fileName);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFLEN];
			int length = -1;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			out.close();
			in.close();
			Toast.makeText(m_context, "load!", Toast.LENGTH_LONG).show();
			return out.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
