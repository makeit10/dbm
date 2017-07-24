package com.struggle.dbm.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.struggle.dbm.core.impl.SessionFactoryImpl;

public class Configuration {
	private static final Logger log = Logger.getLogger(Configuration.class);
	Map<String,Setting> settings=null;
	public Configuration configure(){
		String resource="/dbm.xml";
		return configure(resource);
	} 
	public Configuration configure(String resource){
		log.info("加载配置文件：dbm.xml");
		try{
			settings=new HashMap<String,Setting>();
			SAXBuilder sb = new SAXBuilder();
			InputStream in = Configuration.class.getResourceAsStream(resource);
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
			            Setting setting=new Setting();
						String dataSourceId=datasourceElement.getAttributeValue("id");
			            String connectorClass = datasourceElement.getAttributeValue("class");
			            String dialectClass = datasourceElement.getAttributeValue("dialectClass");
			            if(StringUtils.isBlank(dataSourceId)||StringUtils.isBlank(connectorClass)||StringUtils.isBlank(dialectClass))
			            {
			            	log.error("加载DataSources datasource配置失败：id、connectorClass或dialectClass没有配置，请检查!");
							return null;
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
			            setting.setDataSourceId(dataSourceId);
			            setting.setConnectorClass(connectorClass);
			            setting.setDialectClass(dialectClass);
			            setting.setParams(params);
			            settings.put(setting.getDataSourceId(), setting);
					}
				}
				else
				{
					log.error("加载DataSources datasource配置失败：DataSources datasource没有配置，请检查!");
					return null;
				}
			}
			else
			{
				log.error("加载DataSources配置失败：DataSources没有配置，请检查!");
				return null;
			}
			log.info("加载数据库连接 完成");
			return this;
			}catch(Exception e)
			{
				log.info("加载配置文件：ServerConf.xml失败",e);
				return null;
			}
	} 
	public SessionFactory buildSessionFactory(String dataSourceId){
		Setting setting=settings.get(dataSourceId);
		String handlerClass=setting.getConnectorClass();
		Map<String,String> params=setting.getParams();
		try {
    		Class clazz=Class.forName(handlerClass);
			DataSource dataSource= (DataSource) clazz.newInstance();
			for(String name:params.keySet()){
				String value=params.get(name);
				BeanUtils.setProperty(dataSource, name, value);
			}
			return new SessionFactoryImpl(dataSource,setting);
		}
        catch(Exception e) {
            log.error("创建数据库连接失败",e);
        } 
		return null;
	}
}
