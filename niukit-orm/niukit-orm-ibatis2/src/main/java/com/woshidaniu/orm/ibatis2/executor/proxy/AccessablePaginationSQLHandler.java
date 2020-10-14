package com.woshidaniu.orm.ibatis2.executor.proxy;

import com.woshidaniu.basemodel.BaseModel;
import com.woshidaniu.db.core.dialect.Dialect;
import com.woshidaniu.db.core.dialect.PageBounds;
import com.woshidaniu.db.core.dialect.impl.MySql5Dialect;

public class AccessablePaginationSQLHandler implements SQLHandler {
	
	private static AccessablePaginationSQLHandler instance = null;
	private Dialect dialect = new MySql5Dialect();
	private PageBounds bounds;
	
	private AccessablePaginationSQLHandler(){}
	private AccessablePaginationSQLHandler(Dialect dialect){this.dialect  = dialect;}
	
	public static AccessablePaginationSQLHandler getInstance(String dialect,PageBounds bounds){
		Dialect dialectObject = null;
		if (dialect == null) {
			throw new RuntimeException("the value of the dialect property in com.fastkit.orm.ibatis2.support.AbstractIbatis2SimpleDaoSupport<T, ID> is not defined : "+ dialect);
		}
		try {
			dialectObject = (dialectObject==null)?(Dialect) Class.forName(dialect).newInstance():dialectObject;
		} catch (InstantiationException e) {
			throw new RuntimeException(dialect + " is not defined !",e.getCause());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(dialect + " is IllegalAccess !",e.getCause());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(dialect + " is not found !",e.getCause());
		}
		if(instance == null){
			instance = new AccessablePaginationSQLHandler(dialectObject);
		}
		instance.bounds = bounds;
		return instance;
	}
	
	public static AccessablePaginationSQLHandler getInstance(String dialect){
		return getInstance(dialect,new PageBounds());
	}
	
	public static <T extends BaseModel> AccessablePaginationSQLHandler getInstance(String dialect,T entity){
		return getInstance(dialect,new PageBounds(entity.getQueryModel()));
	}
	
	public static AccessablePaginationSQLHandler getInstance(String dialect,Integer pageNo,Integer pageSize){
		return getInstance(dialect,new PageBounds(pageNo, pageSize));
	}
	
	public static AccessablePaginationSQLHandler getInstance(String dialect,long offset,long limit){
		return getInstance(dialect,new PageBounds(offset, limit));
	}
	
	public String handle(String sql, Object[] params) throws Throwable {
		return getDialect().getLimitSQL(sql, getBounds());
	}
	
	public Dialect getDialect() {
		return dialect;
	}
	
	public PageBounds getBounds() {
		return bounds;
	}
	
}



