package com.example.freechat.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * 数据库db
 * 表：messages (name, time, attr, value);
 * name: 联系人, 类型 text
 * time: 信息时间戳, 类型 integer(1970至今的秒数) System.getcurtimemills();
 * attr: 信息类型, 类型 integer : 0 发送的, 1 接受的
 * value: 信息内容, 类型 text
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// private final String TAG = "DatabaseHelper";
	private static final String DATABASE_NAME = "db";	
	private static final String TABLE_NAME    = "messages";
	private static final String COLUMN_ID     = "_id";
	private static final String COLUMN_NAME	  = "name";
	private static final String COLUMN_TIME   = "timestamp";
	private static final String COLUMN_VALUE  = "value";
	private static final String COLUMN_ATTR   = "attribute";
	private static final String COLUMN_TYPE   = "type";
	private static final String COLUMN_USER   = "user";
	
	private static final String CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME + " (" + 
		COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		COLUMN_NAME + " TEXT," +
		COLUMN_TIME + " INTEGER," +
		COLUMN_ATTR + " INTEGER," +
		COLUMN_TYPE + " INTEGER," +
		COLUMN_VALUE + " TEXT," +
		COLUMN_USER + " TEXT" +
		");";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("database", CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("Drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

	public String getTableName() {
		return TABLE_NAME;
	}
	
	public String getRowIdName() {
		return COLUMN_ID;
	}
	
}
