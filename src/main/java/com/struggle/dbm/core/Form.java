package com.struggle.dbm.core;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
*      
* 类名：Form 
* 版权：copyright © 2010 Nantian Software Co., ltd  
* 类描述： 表单  
* 创建人：LIXIAOYU  
* 创建时间：2014-12-30 下午1:17:28   
* 修改人：LIXIAOYU   
* 修改时间：2014-12-30 下午1:17:28   
* 修改备注：   
* @version    
*
 */
@Table(name="aaab")
public class Form implements Cloneable{
	/**
     * 编号
     */
	@Id
	@GeneratedValue
	private String id;
	/**
     * 状态 
     */
	@Column
	private String status;

	/**
     * 描述
     */
	@Column
	private String description;


	/**
     * 提交人
     */
	@Column
	private String submitter;

	/**
     * 创建时间
     */
	@Column
	private String createDate;

	/**
     * 最后修改人
     */
	@Column
	private String lastModifier;

	/**
     * 最后修改时间
     */
	@Column
	private String lastModifyDate;
	
	/**
     * 表单ID
     */
	@Column
	private Integer formId;
	/**
     * 名称
     */
	@Column
	private String name;
	
	/**
     * 显示名称
     */
	@Column
	private String displayName;
	

	/**
     * 修改类型
     */
	private int alterType;
	
	/**
     * 表单类型
     */
	@Column
	private Integer formType;
	
	/**
	 * Member A
	 */
	@Column
	private Integer memberA;
	
	/**
	 * Member B
	 */
	@Column
	private Integer memberB;
	
	/**
	 * Join 方式
	 */
	@Column
	private Integer joinOption;
	
	/**
	 *Join 条件 
	 */
	@Column
	private String joinQualification;
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSubmitter() {
		return submitter;
	}


	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getLastModifier() {
		return lastModifier;
	}


	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}


	public String getLastModifyDate() {
		return lastModifyDate;
	}


	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}


	public Integer getFormId() {
		return formId;
	}


	public void setFormId(Integer formId) {
		this.formId = formId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}




	public Integer getFormType() {
		return formType;
	}


	public void setFormType(Integer formType) {
		this.formType = formType;
	}


	public Integer getMemberA() {
		return memberA;
	}


	public void setMemberA(Integer memberA) {
		this.memberA = memberA;
	}


	public Integer getMemberB() {
		return memberB;
	}


	public void setMemberB(Integer memberB) {
		this.memberB = memberB;
	}

	public Integer getJoinOption() {
		return joinOption;
	}


	public void setJoinOption(Integer joinOption) {
		this.joinOption = joinOption;
	}


	public String getJoinQualification() {
		return joinQualification;
	}


	public void setJoinQualification(String joinQualification) {
		this.joinQualification = joinQualification;
	}


	@Override
	public String toString() {
		return "Form [id=" + id + ", status=" + status + ", description="
				+ description + ", submitter=" + submitter + ", createDate="
				+ createDate + ", lastModifier=" + lastModifier
				+ ", lastModifyDate=" + lastModifyDate + ", formId=" + formId
				+ ", name=" + name + ", displayName=" + displayName
				+ ", alterType=" + alterType + ", formType=" + formType
				+ ", memberA=" + memberA + ", memberB=" + memberB
				+ ", joinOption=" + joinOption + ", joinQualification="
				+ joinQualification + "]";
	}
    





    
	
}
