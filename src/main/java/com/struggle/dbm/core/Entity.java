package com.struggle.dbm.core;


public class Entity {
 private Object instance;
 private Query query;
public Object getInstance() {
	return instance;
}
public void setInstance(Object instance) {
	this.instance = instance;
}
public Query getQuery() {
	return query;
}
public void setQuery(Query query) {
	this.query = query;
}
 
}
