package com.struggle.dbm.exception;

import org.apache.log4j.Logger;

/**
* 类名：BasicException 
* 版权：copyright © 2010 Nantian Software Co., ltd  
* 类描述：所有异常类都必须继承此基类   
* 创建人：GaoRongwei
* 创建时间：2015-1-6 上午10:52:14   
* 修改人：GaoRongwei  
* 修改时间：2015-1-6 上午10:52:14   
* 修改备注：   
* @version
 */
public class DbmException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DbmException.class);
	
	public DbmException(Class<?> clazz, String msg){
		super(msg);
		logger.warn(clazz.getName() + " : " +msg);
	}
	
	public DbmException(Class<?> clazz, String msg, Throwable t){
		super(msg, t);
		logger.error(clazz.getName() + " : " + msg + " Cause by:\n" + t.toString() + stackTrace2String(t.getStackTrace()));
	}


	public static String stackTrace2String(StackTraceElement []stackTrace){
		if(stackTrace != null){
			StringBuffer sb = new StringBuffer(32);
			for(int i=0;i<stackTrace.length;i++){
				sb.append("\n").append(stackTrace[i].toString());
			}
			return sb.toString();
		}
		return null;
	}
}
