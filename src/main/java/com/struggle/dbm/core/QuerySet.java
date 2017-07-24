package com.struggle.dbm.core;

import java.util.List;

public class QuerySet {
	private List<Query> querys;
	private String subject;
	public List<Query> getQuerys() {
		return querys;
	}
	public void setQuerys(List<Query> querys) {
		this.querys = querys;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
