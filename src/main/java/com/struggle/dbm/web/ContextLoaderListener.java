package com.struggle.dbm.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.struggle.dbm.dbcp.DbcpManager;

/**
 * @Project <CL-Allocation tool>
 * @version <1.0>
 * @Author  <LIXIAOYU>
 * @Date    <2015-6-21>
 * @description 
 */
public class ContextLoaderListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
//		DbcpManager.destroy();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		DbcpManager.init();
	}

}
