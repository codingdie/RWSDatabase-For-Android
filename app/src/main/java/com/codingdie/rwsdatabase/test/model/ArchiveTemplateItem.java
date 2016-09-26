
package com.codingdie.rwsdatabase.test.model;

import java.io.Serializable;

/**
 * Created by fan on 2014/7/18.
 */
public class ArchiveTemplateItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5492573712568138268L;
	/**
	 * 模版子项ID
	 */
	private Long archiveTemplateSubId;
	private int itemIndex;
	private String displayName;
	private long preconditionIndex;
	private String hintWords;
	private int requiredFlag;
	private int disable;
	private String comments;
	private String itemName;
	private int widgetType;
	private int valueType;
	private String defaultValue;
	private String optionSource;
	private String engAbbr;
	private String valueUnit;
	private String valueValidator;
	private String referenceRange;
	private int itemType;
	private String preconditionValue;
	private int analyseType;
	private String analyseName;
	private String analyseCategory;
	private String fieldValueGenerator;
	// 以下为本地临时字段
	private Long itemIndexCache;
	private String realDefaultValue;
	private String itemNo;

	public ArchiveTemplateItem(Long archiveTemplateSubId, int itemIndex, String displayName, long preconditionIndex, String hintWords, Integer requiredFlag, Integer disable, String comments,
			String itemName, Integer widgetType, Integer valueType, String defaultValue, String optionSource, String engAbbr, String valueUnit, String valueValidator, String referenceRange,
			Integer itemType, String preconditionValue, int analyseType, String analyseName, String analyseCategory, String fieldValueGenerator) {
		super();
		this.archiveTemplateSubId = archiveTemplateSubId;
		this.itemIndex = itemIndex;
		this.displayName = displayName;
		this.preconditionIndex = preconditionIndex;
		this.hintWords = hintWords;
		this.requiredFlag = requiredFlag;
		this.disable = disable;
		this.comments = comments;
		this.itemName = itemName;
		this.widgetType = widgetType;
		this.valueType = valueType;
		this.defaultValue = defaultValue;
		this.optionSource = optionSource;
		this.engAbbr = engAbbr;
		this.valueUnit = valueUnit;
		this.valueValidator = valueValidator;
		this.referenceRange = referenceRange;
		this.itemType = itemType;
		this.preconditionValue = preconditionValue;
		this.analyseType = analyseType;
		this.analyseName = analyseName;
		this.analyseCategory = analyseCategory;
		this.fieldValueGenerator = fieldValueGenerator;
	}

	public String getAnalyseName() {
		return analyseName;
	}

	public void setAnalyseName(String analyseName) {
		this.analyseName = analyseName;
	}

	public String getAnalyseCategory() {
		return analyseCategory;
	}

	public void setAnalyseCategory(String analyseCategory) {
		this.analyseCategory = analyseCategory;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public void setWidgetType(int widgetType) {
		this.widgetType = widgetType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public ArchiveTemplateItem() {
		super();
	}

	public Long getArchiveTemplateSubId() {
		return archiveTemplateSubId;
	}

	public void setArchiveTemplateSubId(Long archiveTemplateSubId) {
		this.archiveTemplateSubId = archiveTemplateSubId;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getPreconditionIndex() {
		return preconditionIndex;
	}

	public void setPreconditionIndex(long preconditionIndex) {
		this.preconditionIndex = preconditionIndex;
	}

	public String getPreconditionValue() {
		return preconditionValue;
	}

	public void setPreconditionValue(String preconditionValue) {
		this.preconditionValue = preconditionValue;
	}

	public String getHintWords() {
		return hintWords;
	}

	public void setHintWords(String hintWords) {
		this.hintWords = hintWords;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(Integer wigitType) {
		widgetType = wigitType;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getOptionSource() {
		return optionSource;
	}

	public void setOptionSource(String optionSource) {
		this.optionSource = optionSource;
	}

	public String getEngAbbr() {
		return engAbbr;
	}

	public void setEngAbbr(String engAbbr) {
		this.engAbbr = engAbbr;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	public String getReferenceRange() {
		if (referenceRange == null) {
			return null;
		}
		return referenceRange.trim();
	}

	public void setReferenceRange(String reference) {
		referenceRange = reference;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public boolean equals(Object o) {
		ArchiveTemplateItem archiveTemplateItem = (ArchiveTemplateItem) o;

		return archiveTemplateItem.itemIndex == itemIndex;
	}

	public String getValueValidator() {
		return valueValidator;
	}

	public void setValueValidator(String valueValidator) {
		this.valueValidator = valueValidator;
	}

	public int getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(int requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public Long getItemIndexCache() {
		return itemIndexCache;
	}

	public void setItemIndexCache(Long itemIndexCache) {
		this.itemIndexCache = itemIndexCache;
	}

	public String getRealDefaultValue() {
		return realDefaultValue;
	}

	public void setRealDefaultValue(String realDefaultValue) {
		this.realDefaultValue = realDefaultValue;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public int getAnalyseType() {
		return analyseType;
	}

	public void setAnalyseType(int analyseType) {
		this.analyseType = analyseType;
	}

	public String getFieldValueGenerator() {
		return fieldValueGenerator;
	}

	public void setFieldValueGenerator(String fieldValueGenerator) {
		this.fieldValueGenerator = fieldValueGenerator;
	}

}
