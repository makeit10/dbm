package com.struggle.dbm.dbcp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 时间：2009-3-23
 * @author 
 * 
 * 数据库连接池的管理类。
 * 
 */
public class DbcpManager {
	private final static Logger log = Logger.getLogger(DbcpManager.class);
//	数据源连接集合
	private static ConcurrentHashMap<String, DataSource> dataSourceMap=new ConcurrentHashMap<String, DataSource>();

	/**
     * 构造函数。
     */
    private DbcpManager() {
        super();
    }
    
    /** 
     * 创建数据源连接集合。
     * 
     * @return 
     * @throws 
     */
    public static boolean createDataSource(String dataSourceId,String handlerClass,Map<String, String> params) {
    	try {
    		Class clazz=Class.forName(handlerClass);
			DataSource dataSource= (DataSource) clazz.newInstance();
			for(String name:params.keySet()){
				String value=params.get(name);
				BeanUtils.setProperty(dataSource, name, value);
			}
			dataSourceMap.put(dataSourceId,dataSource);
			return true;
		}
        catch(Exception e) {
            log.error("创建数据库连接失败",e);
            return false;
        }   	
    	
    }
    
    /**
     * @throws SQLException  
     * 从数据源dataSource中获取一个连接。
     * 
     * @return Connection
     * @throws 
     */
    public static Connection getConnection(String dataSourceId){
    	DataSource datasource=dataSourceMap.get(dataSourceId);
    	if(datasource!=null){
    		try {
				Connection connection = datasource.getConnection();
				return connection;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
		return null;
    }
    
    /** 
     * 关闭数据源dataSource的连接。
     * 
     * @return 
     * @throws 
     */
    public static void close(Connection conn)
    {
    	try {
    		if(conn!=null){
    			if(!conn.getAutoCommit()){
        			conn.rollback();
        			conn.setAutoCommit(true);
        		}
    			conn.close();
    		}
    	}
        catch(Exception e) {
            log.error("关闭连接失败",e);
        }
    }

    /**
     * 关闭数据操作中的结果集@see java.sql.ResultSet,
     * 查询过程@see java.sql.Statement,
     * 数据库连接@see java.sql.Connection,
     * 操作中由任何异常都不抛出。
     * 
     * @param conn @see java.sql.Connection
     * @param stat @see java.sql.Statement
     * @param rs @see java.sql.ResultSet
     */
    public synchronized static void close(Connection conn,Statement stat,ResultSet rs){
        close(rs);
        close(stat);
        close(conn);        
    }
    
    public synchronized static void close(Connection conn,ResultSet rs){
        close(rs);
        close(conn);        
    }
    
    /**
     * 查询过程@see java.sql.Statement,
     * 数据库连接@see java.sql.Connection,
     * 操作中由任何异常都不抛出。
     * 
     * @param conn @see java.sql.Connection
     * @param stat @see java.sql.Statement
     */
    public synchronized static void close(Connection conn,Statement stat){
    	close(stat);
        close(conn);
    }
    /**
     * 关闭Statement@see java.sql.Statement,
     * 操作中由任何异常都不抛出。
     * 
     * @param stat @see java.sql.Statement
     */
    public synchronized static void close(Statement stat){
    	try{
            if(stat !=null){
                stat.close();
                stat = null; 
            }
        }
        catch(SQLException e){
            log.error("关闭Statement失败",e);
        }
    }
    /**
     * 关闭数据操作中的结果集@see java.sql.ResultSet,
     * 操作中由任何异常都不抛出。
     * 
     * @param stat @see java.sql.ResultSet
     */
    public synchronized static void close(ResultSet rs){
        try{
            if( rs!=null){
                rs.close();
                rs = null;
            }
        }
        catch(SQLException e){
            log.error("关闭数据结果集失败",e);
        }
    }
    
 /*   public static void destroy(){
    	Iterator dataSources=dataSourceMap.values().iterator();
    	while (dataSources.hasNext()) {
    		DataSource dataSource = (DataSource) dataSources.next();
    		dataSource=null;
    	}
    	log.debug("关闭所有数据源的全部连接！");
    }*/

    
    

    /**
	 * 刷新配置文件
	 */
	public static boolean init() {
		log.info("加载配置文件：dbm.xml");
		try{
		SAXBuilder sb = new SAXBuilder();
		InputStream in = DbcpManager.class.getResourceAsStream("/dbm.xml");
		Document doc = sb.build(in);
		Element root = doc.getRootElement();
		log.info("加载数据库连接");
		// 加载数据库连接
		Element dataSourcesElement = root.getChild("dataSources");
		if(dataSourcesElement!=null)
		{
			List<Element> dataSourceElements=dataSourcesElement.getChildren("datasource");
			if(dataSourceElements!=null)
			{
				for(Element datasourceElement:dataSourceElements)
				{
		            String id=datasourceElement.getAttributeValue("id");
		            String connectorClass = datasourceElement.getAttributeValue("class");
		            if(StringUtils.isBlank(id)||StringUtils.isBlank(connectorClass))
		            {
		            	log.error("加载DataSources datasource配置失败：id或connectorClass没有配置，请检查!");
						return false;
		            }
		            else
		            {
		            	
		            }
		            Map<String, String> params = new HashMap<String, String>();
		            List<Element> paramElements = datasourceElement.getChildren("param");
		            for(Element paramElement:paramElements)
		            {
		            	String paraName=paramElement.getAttributeValue("name");
		            	String paraValue=paramElement.getAttributeValue("value");
		            	if(null!=paraName&&!("").equals(paraName)&&null!=paraValue&&!("").equals(paraValue)){
		            		params.put(paraName, paraValue);
		            	}
		            }
		            if(!DbcpManager.createDataSource(id,connectorClass,params))
		            {
		            	return false;
		            }
				}
			}
			else
			{
				log.error("加载DataSources datasource配置失败：DataSources datasource没有配置，请检查!");
				return false;
			}
		}
		else
		{
			log.error("加载DataSources配置失败：DataSources没有配置，请检查!");
			return false;
		}
		log.info("加载数据库连接 完成");
		return true;
		}catch(Exception e)
		{
			log.info("加载配置文件：ServerConf.xml失败",e);
			return false;
		}
}
	
}
