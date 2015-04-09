package metadao.impl;

import helper.DBHelper;

public class MetaDaoHelperImpl extends DBHelper{
	
	private static DBHelper baseDao = new MetaDaoHelperImpl();
	
	private MetaDaoHelperImpl(){
      super();
	}
	
	public static DBHelper getBaseDaoInstance(){
		return baseDao;
	}
	
}
