
package com.codingdie.rwsdatabase.test.model;

public class ArchiveRecordItem extends ArchiveTemplateItem {

	private static final long serialVersionUID = -8887644942382342798L;

	private long localArchiveRecordId = -1L;

	private Long archiveRecordSubId = -1L;
	private String value;
	private Float itemScore;
	private int markType;

	public long getLocalArchiveRecordId() {
		return localArchiveRecordId;
	}

	public void setLocalArchiveRecordId(long localArchiveRecordId) {
		this.localArchiveRecordId = localArchiveRecordId;
	}

	public Long getArchiveRecordSubId() {
		return archiveRecordSubId;
	}

	public void setArchiveRecordSubId(Long archiveRecordSubId) {
		this.archiveRecordSubId = archiveRecordSubId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Float getItemScore() {
		return itemScore;
	}

	public void setItemScore(Float itemScore) {
		this.itemScore = itemScore;
	}

	public int getMarkType() {
		return markType;
	}

	public void setMarkType(int markType) {
		this.markType = markType;
	}

}
