package com.example.freechat.storage;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.ui.FCMessage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHandler {
	private final String TAG   = "DatabaseHandler";
	static final String NAME   = "name";
	static final String ATTR   = "attribute";
	static final String TIME   = "time";
	static final String VALUE  = "value";
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public DatabaseHandler(Context context) {
		dbHelper = new DatabaseHelper(context);
		Log.i(TAG, "Object created.");
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void insertMessage(String name, FCMessage msg) {
		ContentValues cv = new ContentValues();
		
		cv.put(NAME,  name);
		cv.put(TIME, msg.getTimeStamp());
		cv.put(ATTR, msg.getMessageType());
		cv.put(VALUE, msg.getContent());
		database.insert(dbHelper.getTableName(), NAME, cv);
		
		Log.i(TAG, "Contact added successfully.");
	}
	
	public void deleteContact(long id) {
		database.delete(dbHelper.getTableName(), dbHelper.getRowIdName() + "=" + id, null);
	}
	
	public List<FCMessage> selectMessageByName(String name) {
		List<FCMessage> messages = new ArrayList<FCMessage>();
		String [] columns = new String[]{ATTR, TIME, VALUE};
		String selection = new String(NAME + "=?");
		String [] selectionArgs = new String[]{name};
		Cursor cursor = database.query(dbHelper.getTableName(), columns, selection, selectionArgs, null, null, null);
		cursor.moveToFirst();
		while(! cursor.isAfterLast()) {
			FCMessage msg = cursorToMessage(cursor);
			messages.add(msg);
			cursor.moveToNext();	
		}
		return messages;
	}
	
	private FCMessage cursorToMessage(Cursor cursor) {
		int attribute = cursor.getInt(0);
		long timeStamp = cursor.getLong(1);
		String info = cursor.getString(2);
		FCMessage msg = new FCMessage(info, timeStamp, attribute);
		return msg;
	}
}
