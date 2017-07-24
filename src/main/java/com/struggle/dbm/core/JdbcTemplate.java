package com.struggle.dbm.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.struggle.dbm.core.Entity;
import com.struggle.dbm.core.Query;

/**
* 类名：CommonDao 
* 版权：copyright © 2010 Nantian Software Co., ltd  
* 类描述：持久化层公共类   
* 创建人：LIXIAOYU  
* 创建时间：2015-6-13 下午1:34:39   
* 修改人：LIXIAOYU   
* 修改时间：2015-6-13 下午1:34:39   
* 修改备注：   
* @version
 */
public interface JdbcTemplate {
	public <T> T insert(T instance);
	public <T> List<T> insertBatch(List<T> instances);
	public <T> void update(T instance);
	public <T> void updateBatch(List<T> instances);
	public <T> void delete(T instance);
	public <T> void deleteBatch(List<T> instances);
	public <T> void deleteByQualification(Class<T> clazz, Query qualification);
	public <T> T selectById(Class<T> clazz,Serializable id);
	public <T> T selectByExample(T instance);
	public <T> T selectByQualification(Class<T> clazz,Query qualification);
	public <T> List<T> selectListById(Class<T> clazz,Serializable... id);
	public <T> List<T> selectListByExample(T instance,int firstRetrieve,int maxRetrieve,Map<String, String> sorts);
	public <T> List<T> selectListByQualification(Class<T> clazz,Query qualification,int firstRetrieve,int maxRetrieve,Map<String,String> sorts);
	public <T> Map<Object,T> selectMapById(Class<T> clazz,String keyFieldName,Serializable... id);
	public <T> Map<Object,T> selectMapByExample(T instance,String keyFieldName,int firstRetrieve,int maxRetrieve,Map<String, String> sorts);
	public <T> Map<Object,T> selectMapByQualification(Class<T> clazz,String keyFieldName,Query qualification,int firstRetrieve,int maxRetrieve,Map<String,String> sorts);
	public <T> int selectCountByExample(T instance);
	public <T> int selectCountByQualification(Class<T> clazz,Query qualification);
}