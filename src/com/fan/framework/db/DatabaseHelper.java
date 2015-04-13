package com.fan.framework.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "ormlite.db";
	private static final int DATABASE_VERSION = 1;
	
//	private Dao<RequestImportant,Integer> stuDao = null;
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		onCreate(getWritableDatabase(), getConnectionSource());
	}

	/**
	 * 创建SQLite数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, RequestImportant.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	/**
	 * 更新SQLite数据库
	 */
	@Override
	public void onUpgrade(
			SQLiteDatabase sqliteDatabase, 
			ConnectionSource connectionSource, 
			int oldVer,
			int newVer) {
		try {
			TableUtils.dropTable(connectionSource, RequestImportant.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), 
					"Unable to upgrade database from version " + oldVer + " to new "
					+ newVer, e);
		}
	}
}