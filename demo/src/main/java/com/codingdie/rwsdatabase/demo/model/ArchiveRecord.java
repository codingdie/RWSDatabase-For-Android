
package com.codingdie.rwsdatabase.demo.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ArchiveRecord implements java.io.Serializable {

	private static final long serialVersionUID = 6459927264927942109L;

	private long localArchiveRecordId = -1L; // 本地字段

	private Long archiveRecordId = -1L;
	private String archiveRecordName;
	private String archiveRecordTitle;

	private Long archiveTemplateId;
	private int archiveTemplateVersion;
	private String archiveTemplateCategoryName;
	private int archiveTemplateCategoryId;
	private String archiveTemplateName;

	private String recordGroup;
	private int followUpFlag;
	private int privateFlag;
	private String creatorId;
	private String patientId;
	private int templateType;

	private int delFlag = 0;

	private String occurrenceTime;
	private String localOpTime = "1970-01-01 00:00:00.0";
	private String updateTime = "1970-01-01 00:00:00.0";
	private String createTime;

	private String pictures;
	private String summary;
	private int scoreFlag;
	private float totalScore;
	private long crfFolderId;
	private long crfFolderItemId;

	private List<ArchiveRecordItem> itemList;
	private long groupId;


	private ArrayList<String> analyseItems;

	// 本地字段，不会传到服务器上
	private int bagId = -1;
	private int commitStatus;

	private String htmlContent;
	private String titleContent;

	private boolean needDisplayTime = true;
	private boolean isOpen = false;// 是否展开
	private boolean hasPic = false;// 是否含有图片
	private boolean isPlanRecords = false;// 是不是需要一次生成多份记录
	private Boolean isChecked = false;

	public static ArchiveRecord fillBaseRecordInfo(Cursor cursor) {

		ArchiveRecord archiveRecord = new ArchiveRecord();
		archiveRecord.setArchiveRecordId(cursor.getLong(cursor.getColumnIndex("ARCHIVERECORDID")));
//		archiveRecord.setLocalArchiveRecordId(cursor.getLong(cursor.getColumnIndex("RECORDROWID")));
		archiveRecord.setArchiveRecordName(cursor.getString(cursor.getColumnIndex("ARCHIVERECORDNAME")));
		archiveRecord.setArchiveRecordTitle(cursor.getString(cursor.getColumnIndex("ARCHIVERECORDTITLE")));
		archiveRecord.setArchiveTemplateId(cursor.getLong(cursor.getColumnIndex("ARCHIVETEMPLATEID")));
		archiveRecord.setArchiveTemplateCategoryId(cursor.getInt(cursor.getColumnIndex("ARCHIVETEMPLATECATEGORYID")));
		archiveRecord.setArchiveTemplateName(cursor.getString(cursor.getColumnIndex("ARCHIVETEMPLATENAME")));
		archiveRecord.setArchiveTemplateVersion(cursor.getInt(cursor.getColumnIndex("ARCHIVETEMPLATEVERSION")));
		archiveRecord.setPatientId(cursor.getString(cursor.getColumnIndex("PATIENTID")));
		archiveRecord.setCreatorId(cursor.getString(cursor.getColumnIndex("CREATORID")));
		archiveRecord.setGroupId(cursor.getLong(cursor.getColumnIndex("GROUPID")));
		archiveRecord.setOccurrenceTime(cursor.getString(cursor.getColumnIndex("OCCURRENCETIME")));
		archiveRecord.setPrivateFlag(cursor.getInt(cursor.getColumnIndex("PRIVATEFLAG")));
		archiveRecord.setFollowUpFlag(cursor.getInt(cursor.getColumnIndex("FOLLOWUPFLAG")));
		archiveRecord.setItemList(new ArrayList<ArchiveRecordItem>());
		return archiveRecord;
	}

	public long getLocalArchiveRecordId() {
		return localArchiveRecordId;
	}

	public void setLocalArchiveRecordId(long localArchiveRecordId) {
		this.localArchiveRecordId = localArchiveRecordId;
	}

	public Long getArchiveRecordId() {
		return archiveRecordId;
	}

	public void setArchiveRecordId(Long archiveRecordId) {
		this.archiveRecordId = archiveRecordId;
	}

	public String getArchiveRecordName() {
		return archiveRecordName;
	}

	public void setArchiveRecordName(String archiveRecordName) {
		this.archiveRecordName = archiveRecordName;
	}

	public String getArchiveRecordTitle() {
		return archiveRecordTitle;
	}

	public void setArchiveRecordTitle(String archiveRecordTitle) {
		this.archiveRecordTitle = archiveRecordTitle;
	}

	public Long getArchiveTemplateId() {
		return archiveTemplateId;
	}

	public void setArchiveTemplateId(Long archiveTemplateId) {
		this.archiveTemplateId = archiveTemplateId;
	}

	public int getArchiveTemplateVersion() {
		return archiveTemplateVersion;
	}

	public void setArchiveTemplateVersion(int archiveTemplateVersion) {
		this.archiveTemplateVersion = archiveTemplateVersion;
	}

	public String getArchiveTemplateCategoryName() {
		return archiveTemplateCategoryName;
	}

	public void setArchiveTemplateCategoryName(String archiveTemplateCategoryName) {
		this.archiveTemplateCategoryName = archiveTemplateCategoryName;
	}

	public int getArchiveTemplateCategoryId() {
		return archiveTemplateCategoryId;
	}

	public void setArchiveTemplateCategoryId(int archiveTemplateCategoryId) {
		this.archiveTemplateCategoryId = archiveTemplateCategoryId;
	}

	public String getArchiveTemplateName() {
		return archiveTemplateName;
	}

	public void setArchiveTemplateName(String archiveTemplateName) {
		this.archiveTemplateName = archiveTemplateName;
	}

	public String getRecordGroup() {
		return recordGroup;
	}

	public void setRecordGroup(String recordGroup) {
		this.recordGroup = recordGroup;
	}

	public int getFollowUpFlag() {
		return followUpFlag;
	}

	public void setFollowUpFlag(int followUpFlag) {
		this.followUpFlag = followUpFlag;
	}

	public int getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(int privateFlag) {
		this.privateFlag = privateFlag;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public String getOccurrenceTime() {
		return occurrenceTime;
	}

	public void setOccurrenceTime(String occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

	public String getLocalOpTime() {
		return localOpTime;
	}

	public void setLocalOpTime(String localOpTime) {
		this.localOpTime = localOpTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getScoreFlag() {
		return scoreFlag;
	}

	public void setScoreFlag(int scoreFlag) {
		this.scoreFlag = scoreFlag;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public long getCrfFolderId() {
		return crfFolderId;
	}

	public void setCrfFolderId(long crfFolderId) {
		this.crfFolderId = crfFolderId;
	}

	public long getCrfFolderItemId() {
		return crfFolderItemId;
	}

	public void setCrfFolderItemId(long crfFolderItemId) {
		this.crfFolderItemId = crfFolderItemId;
	}

	public List<ArchiveRecordItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ArchiveRecordItem> itemList) {
		this.itemList = itemList;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public ArrayList<String> getAnalyseItems() {
		return analyseItems;
	}

	public void setAnalyseItems(ArrayList<String> analyseItems) {
		this.analyseItems = analyseItems;
	}

	public int getBagId() {
		return bagId;
	}

	public void setBagId(int bagId) {
		this.bagId = bagId;
	}

	public int getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(int commitStatus) {
		this.commitStatus = commitStatus;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getTitleContent() {
		return titleContent;
	}

	public void setTitleContent(String titleContent) {
		this.titleContent = titleContent;
	}

	public boolean isNeedDisplayTime() {
		return needDisplayTime;
	}

	public void setNeedDisplayTime(boolean needDisplayTime) {
		this.needDisplayTime = needDisplayTime;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean open) {
		isOpen = open;
	}

	public boolean isHasPic() {
		return hasPic;
	}

	public void setHasPic(boolean hasPic) {
		this.hasPic = hasPic;
	}

	public boolean isPlanRecords() {
		return isPlanRecords;
	}

	public void setPlanRecords(boolean planRecords) {
		isPlanRecords = planRecords;
	}

	public Boolean getChecked() {
		return isChecked;
	}

	public void setChecked(Boolean checked) {
		isChecked = checked;
	}
}