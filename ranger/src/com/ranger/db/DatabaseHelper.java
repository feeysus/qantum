package com.ranger.db;

 
 
import com.google.gson.Gson;
 
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; 

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "argis.db";
	private static final int VERSION = 1;
	public static final String TABLENAME = "sys_db";
	public static final String USER = "sys_user";
	private static String SDE_ATTR = "for_sde_att";
	private static String ROUTE_RECORD = "for_route_record";
	private static String LAYERS_INFO = "for_layers_info";
	private static String LAYERS = "for_layers";
	private static SQLiteDatabase db;
	private static Gson gson;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		if (db != null && db.isOpen())
			db.close();
		db = this.getWritableDatabase();
		gson = new Gson();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建sys_user的表结构
		String sql0 = "CREATE TABLE IF NOT EXISTS  "
				+ USER
				+ "(ID INTEGER,LOGIN_NAME VARCHAR,LOGIN_PWD VARCHAR,USER_NAME VARCHAR,ROLE_ID INTEGER,ADCD_ID VARCHAR,EXTENT)";
		db.execSQL(sql0);
		// 创建sys_db的表结构
		String sql = "CREATE TABLE IF NOT EXISTS  "
				+ TABLENAME
				+ "(ID INTEGER,DB_KEY VARCHAR,DB_VALUE VARCHAR,DB_VERSION INTEGER)";
		db.execSQL(sql);
		// 创建SDE数据的附件表
		String sql1 = "CREATE TABLE IF NOT EXISTS  "
				+ SDE_ATTR
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,SDE_NAME VARCHAR,LAYER_NAME VARCHAR,TYPE VARCHAR,OBJECTID INTEGER,FILE_NAME VARCHAR,PATH VARCHAR)";
		db.execSQL(sql1);
		// 创建路径坐标记录表
		String sql2 = "CREATE TABLE IF NOT EXISTS  "
				+ ROUTE_RECORD
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE VARCHAR,LAT VARCHAR,LNG VARCHAR,TM INTEGER)";
		db.execSQL(sql2);
		// 创建图层信息表
		String sql3 = "CREATE TABLE IF NOT EXISTS  "
				+ LAYERS_INFO
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,LAYER_ID,FULL_EXTENT,INITIAL_EXTENT,SR,SCALE,RES,ORIGIN,LEVELS,DPI,TILE_WEIGHT,TILE_HEIGHT)";
		db.execSQL(sql3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	 

	/**
	 * 判断某张表是否存在
	 */
	public static boolean tabbleIsExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {

		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
}