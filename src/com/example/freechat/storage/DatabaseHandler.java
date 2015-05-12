package com.example.freechat.storage;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.FCConfigure;
import com.example.freechat.ui.FCMessage;
import com.example.freechat.ui.FCSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 *  key function:
 *  selectAllSession(): 查询全部的会话 返回: List<FCSession>
 *  selectMessageByName(String name): 查询与某人的聊天记录 返回: List<FCMessage>
 */
public class DatabaseHandler {
	private final String TAG   = "DatabaseHandler";
	static final String NAME   = "name";
	static final String ATTR   = "attribute";
	static final String TYPE   = "type";
	static final String TIME   = "timestamp";
	static final String VALUE  = "value";
	static final String USER  = "user";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public DatabaseHandler(Context context) {
		dbHelper = new DatabaseHelper(context);
		Log.i(TAG, "Object created.");
	}
	
	public void openDB() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void insertMessage(String name, FCMessage msg) {
		ContentValues cv = new ContentValues();
		
		cv.put(NAME,  name);
		cv.put(TIME, msg.getTimeStamp());
		cv.put(ATTR, msg.getMessageAttr());
		cv.put(TYPE, msg.getMessageType());
		cv.put(VALUE, msg.getContent());
		cv.put(USER, FCConfigure.myName);
		
		database.insert(dbHelper.getTableName(), NAME, cv);
		
		Log.i(TAG, "Contact added successfully.");
	}
	
	public void deleteContact(long id) {
		database.delete(dbHelper.getTableName(), dbHelper.getRowIdName() + "=" + id, null);
	}
	
	public List<FCMessage> selectMessageByName(String name) {
		
		openDB();
		List<FCMessage> messages = new ArrayList<FCMessage>();
		String [] columns = new String[]{ATTR,TYPE, TIME, VALUE};
		String selection = new String(NAME + "=?" + " AND " + USER + "=?");
		String [] selectionArgs = new String[]{name, FCConfigure.myName};
		Cursor cursor = database.query(dbHelper.getTableName(), columns, selection, selectionArgs, null, null, null);
		cursor.moveToFirst();
		while(! cursor.isAfterLast()) {
			FCMessage msg = cursorToMessage(cursor);
			messages.add(msg);
			cursor.moveToNext();	
		}
		cursor.close();
		return messages;
	}
	
	public List<FCSession> selectAllSession() {
		
		openDB();
		List<FCSession> sessions = new ArrayList<FCSession>();
		
		String [] columns = new String[]{NAME};
		String selection = new String(USER + "=?");
		String [] selectionArgs = new String[]{FCConfigure.myName};
		Cursor cursor = database.query(true, dbHelper.getTableName(), columns, selection, selectionArgs, null, null, null, null, null);
		//Cursor cursor = database.rawQuery("select * from messages", columns);
		cursor.moveToFirst();
		while(! cursor.isAfterLast()) {
			FCSession session = cursorToSession(cursor);
			sessions.add(session);
			cursor.moveToNext();
		}
		cursor.close();
		
		return sessions;
	}
	
	private FCMessage cursorToMessage(Cursor cursor) {
		int attribute = cursor.getInt(0);
		int type = cursor.getInt(1);
		long timeStamp = cursor.getLong(2);
		String info = cursor.getString(3);
		FCMessage msg = new FCMessage(info, attribute, type, timeStamp);
		return msg;
	}
	
	private FCSession cursorToSession(Cursor cursor) {
		String userid = cursor.getString(0);
		FCSession session = new FCSession(userid);
		return session;
	}
}
