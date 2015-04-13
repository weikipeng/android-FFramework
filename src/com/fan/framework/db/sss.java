package com.fan.framework.db;

import java.sql.SQLException;
import java.util.List;

import com.fan.framework.base.FFApplication;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class sss {
	public static void lll() {
		DatabaseHelper h = OpenHelperManager.getHelper(FFApplication.app,
				DatabaseHelper.class);
		try {
			Dao<RequestImportant, Integer> dao = DaoManager.createDao(
					h.getConnectionSource(), RequestImportant.class);
			dao.create(new RequestImportant(0, "a,b", true,
					11.1, (byte) 2, 'c'));
			h.getConnectionSource().close();
			h.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static List<RequestImportant> lllq() {
		DatabaseHelper h = OpenHelperManager.getHelper(FFApplication.app,
				DatabaseHelper.class);
		try {
			Dao<RequestImportant, Integer> dao = DaoManager.createDao(
					h.getConnectionSource(), RequestImportant.class);
			List<RequestImportant> queryForAll = dao.queryForAll();
			h.close();
			return queryForAll;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
