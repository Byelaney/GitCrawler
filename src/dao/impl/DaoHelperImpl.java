package dao.impl;

import helper.DBHelper;

public class DaoHelperImpl extends DBHelper{
		
	private static DBHelper baseDao = new DaoHelperImpl();
		
	private DaoHelperImpl(){
      super();
	}
	
	public static DBHelper getBaseDaoInstance(){
		return baseDao;
	}
		
}
