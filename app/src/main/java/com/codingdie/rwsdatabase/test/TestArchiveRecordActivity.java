package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.test.model.ArchiveRecord;
import com.codingdie.rwsdatabase.test.model.ArchiveRecordItem;
import com.codingdie.rwsdatabase.version.VersionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupen on 2016/8/28.
 */
public class TestArchiveRecordActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list);
        String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.sqlite";
        String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test2.sqlite";
        final SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(path1, null);
        final SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(path2, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                System.out.println("db1 begin");

                for (int i = 0; i < 10; i++) {
                    Cursor cursor = db1.rawQuery("select * from archiveRecord   where archiveRecordId=210924 ", new String[]{});
                    cursor.moveToNext();
                    ArchiveRecord archiveRecord = ArchiveRecord.fillBaseRecordInfo(cursor);
                    cursor = db1.rawQuery("select * from archiveRecordItem   where archiveRecordId=210924 order by ARCHIVETEMPLATESUBID", new String[]{});

                    while (cursor.moveToNext()) {
                        archiveRecord.getItemList().add(fillRecordSimplyItem(cursor));
                    }
                    System.out.println("archiveTemplateId:" + archiveRecord.getArchiveTemplateId());
                    cursor = db1.rawQuery("select * from archiveTemplateItem   where archiveTemplateId=? order by ARCHIVETEMPLATESUBID", new String[]{String.valueOf(archiveRecord.getArchiveTemplateId())});
                    int j = 0;
                    while (cursor.moveToNext()) {
                        fillRecordItemTemplateInfo(cursor, archiveRecord.getItemList().get(j));
                        j++;
                    }
                    if (i == 0) {
                        System.out.println("data:" + new Gson().toJson(archiveRecord));
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("db1 query:" + (end - begin) * 1.0 / 10);

            }
        }).start();
        ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                System.out.println("db2 begin");
                for (int i = 0; i < 10; i++) {

                    Cursor cursor = db2.rawQuery("select * from archiveRecord   where archiveRecordId=210924 ", new String[]{});
                    cursor.moveToNext();
                    ArchiveRecord archiveRecord = ArchiveRecord.fillBaseRecordInfo(cursor);
                    cursor = db2.rawQuery("select * from archiveRecordItem   where archiveRecordId=210924 ", new String[]{});
                    while (cursor.moveToNext()) {
                        archiveRecord.getItemList().add(fillRecordItem(cursor));
                    }
                    if (i == 0) {
                        System.out.println("data:" + new Gson().toJson(archiveRecord));
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("db2 query:" + (end - begin) * 1.0 / 10);

            }
        });
    }

    private ArchiveRecord setOneRecordValuesFromCursor(Cursor cursor) {
        ArchiveRecord archiveRecord = null;
        int itemIndex = -1;
        while (cursor.moveToNext()) {
            ArchiveRecordItem archiveRecordItem = fillRecordItem(cursor);
            if (archiveRecord == null) {
                archiveRecord = ArchiveRecord.fillBaseRecordInfo(cursor);
                List<ArchiveRecordItem> list = new ArrayList<ArchiveRecordItem>();
                archiveRecord.setItemList(list);
            }
            if (itemIndex != archiveRecordItem.getItemIndex()) {
                archiveRecord.getItemList().add(archiveRecordItem);
                itemIndex = archiveRecordItem.getItemIndex();
            }
        }

        return archiveRecord;
    }

    private ContentValues buildRecordItemValue(ArchiveRecordItem archiveRecordItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("archiveRecordId", 1);
        contentValues.put("archiveRecordRowId", archiveRecordItem.getArchiveRecordSubId());
        contentValues.put("archiveRecordSubId", archiveRecordItem.getArchiveRecordSubId());
        contentValues.put("archiveTemplateSubId", archiveRecordItem.getArchiveTemplateSubId());
        contentValues.put("itemIndex", archiveRecordItem.getItemIndex());
        contentValues.put("displayName", archiveRecordItem.getDisplayName());
        contentValues.put("preconditionIndex", archiveRecordItem.getPreconditionIndex());
        contentValues.put("preconditionValue", archiveRecordItem.getPreconditionValue());
        contentValues.put("hintWords", archiveRecordItem.getHintWords());
        contentValues.put("requiredFlag", archiveRecordItem.getRequiredFlag());
        contentValues.put("disable", archiveRecordItem.getDisable());
        contentValues.put("comments", archiveRecordItem.getComments());
        contentValues.put("itemName", archiveRecordItem.getItemName());
        contentValues.put("widgetType", archiveRecordItem.getWidgetType());
        contentValues.put("valueType", archiveRecordItem.getValueType());
        contentValues.put("defaultValue", archiveRecordItem.getDefaultValue());
        contentValues.put("optionSource", archiveRecordItem.getOptionSource());
        contentValues.put("engAbbr", archiveRecordItem.getEngAbbr());
//        contentValues.put("valueUnit", archiveRecordItem.getValueUnit());
        contentValues.put("referenceRange", archiveRecordItem.getReferenceRange());
        contentValues.put("valueValidator", archiveRecordItem.getValueValidator());
        contentValues.put("itemType", archiveRecordItem.getItemType());
        contentValues.put("value", archiveRecordItem.getValue());
        contentValues.put("markType", archiveRecordItem.getMarkType());
        contentValues.put("analyseType", archiveRecordItem.getAnalyseType());
        contentValues.put("analyseName", archiveRecordItem.getAnalyseName());
        contentValues.put("analyseCategory", archiveRecordItem.getAnalyseCategory());
        contentValues.put("fieldValueGenerator", archiveRecordItem.getFieldValueGenerator());
        contentValues.put("itemScore", archiveRecordItem.getItemScore());
        return contentValues;
    }

    private ContentValues buildRecordSimplyItemValue(ArchiveRecordItem archiveRecordItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("archiveRecordId", 0L);
        contentValues.put("value", archiveRecordItem.getValue());
        contentValues.put("archiveRecordSubId", archiveRecordItem.getArchiveRecordSubId());
        contentValues.put("archiveTemplateSubId", archiveRecordItem.getArchiveTemplateSubId());
        contentValues.put("itemScore", archiveRecordItem.getItemScore());
        return contentValues;
    }

    private ContentValues buildRecordValue(ArchiveRecord archiveRecord, int bagId, int isRead) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("archiveRecordId", archiveRecord.getArchiveRecordId());
        contentValues.put("archiveRecordName", archiveRecord.getArchiveRecordName());
        contentValues.put("archiveTemplateId", archiveRecord.getArchiveTemplateId());
        contentValues.put("archiveTemplateVersion", archiveRecord.getArchiveTemplateVersion());
        contentValues.put("patientId", archiveRecord.getPatientId());
        contentValues.put("creatorId", archiveRecord.getCreatorId());
        contentValues.put("groupId", archiveRecord.getGroupId());
        contentValues.put("updateTime", archiveRecord.getUpdateTime());
        contentValues.put("occurrenceTime", archiveRecord.getOccurrenceTime());
        contentValues.put("archiveTemplateCategoryId", archiveRecord.getArchiveTemplateCategoryId());
        contentValues.put("archiveTemplateName", archiveRecord.getArchiveTemplateName());
        contentValues.put("archiveRecordTitle", archiveRecord.getArchiveRecordTitle());
        contentValues.put("createTime", archiveRecord.getCreateTime());
        contentValues.put("followUpFlag", archiveRecord.getFollowUpFlag());
        contentValues.put("delFlag", archiveRecord.getDelFlag());
        contentValues.put("privateFlag", archiveRecord.getPrivateFlag());
        contentValues.put("bagId", bagId);
        contentValues.put("localOpTime", archiveRecord.getLocalOpTime());
        contentValues.put("commitStatus", archiveRecord.getCommitStatus());
        contentValues.put("recordGroup", archiveRecord.getRecordGroup());
        contentValues.put("templateType", archiveRecord.getTemplateType());
        contentValues.put("pictures", archiveRecord.getPictures());
        contentValues.put("summary", archiveRecord.getSummary());
        contentValues.put("scoreFlag", archiveRecord.getScoreFlag());
        contentValues.put("totalScore", archiveRecord.getTotalScore());
        return contentValues;
    }

    public ArchiveRecordItem fillRecordSimplyItem(Cursor cursor) {
        ArchiveRecordItem archiveRecordItem = new ArchiveRecordItem();
        if (cursor.getColumnIndex("archiveRecordRowId") != -1) {
            archiveRecordItem.setLocalArchiveRecordId(cursor.getLong(cursor.getColumnIndex("ARCHIVERECORDROWID")));
        }
        archiveRecordItem.setArchiveRecordSubId(cursor.getLong(cursor.getColumnIndex("ARCHIVERECORDSUBID")));
        archiveRecordItem.setArchiveTemplateSubId(cursor.getLong(cursor.getColumnIndex("ARCHIVETEMPLATESUBID")));
        archiveRecordItem.setValue(cursor.getString(cursor.getColumnIndex("VALUE")));
        archiveRecordItem.setItemScore(cursor.getFloat(cursor.getColumnIndex("ITEMSCORE")));
        return archiveRecordItem;
    }

    public void fillRecordItemTemplateInfo(Cursor cursor, ArchiveRecordItem archiveRecordItem) {
        archiveRecordItem.setDisable(cursor.getInt(cursor.getColumnIndex("DISABLE")));
        archiveRecordItem.setDisplayName(cursor.getString(cursor.getColumnIndex("DISPLAYNAME")));
        archiveRecordItem.setEngAbbr(cursor.getString(cursor.getColumnIndex("ENGABBR")));
        archiveRecordItem.setHintWords(cursor.getString(cursor.getColumnIndex("HINTWORDS")));
        archiveRecordItem.setItemIndex(cursor.getInt(cursor.getColumnIndex("ITEMINDEX")));
        archiveRecordItem.setItemName(cursor.getString(cursor.getColumnIndex("ITEMNAME")));
        archiveRecordItem.setItemType(cursor.getInt(cursor.getColumnIndex("ITEMTYPE")));
        archiveRecordItem.setOptionSource(cursor.getString(cursor.getColumnIndex("OPTIONSOURCE")));
        archiveRecordItem.setPreconditionIndex(cursor.getLong(cursor.getColumnIndex("PRECONDITIONINDEX")));
        archiveRecordItem.setPreconditionValue(cursor.getString(cursor.getColumnIndex("PRECONDITIONVALUE")));
        archiveRecordItem.setReferenceRange(cursor.getString(cursor.getColumnIndex("REFERENCERANGE")));
        archiveRecordItem.setRequiredFlag(cursor.getInt(cursor.getColumnIndex("REQUIREDFLAG")));
        archiveRecordItem.setValueType(cursor.getInt(cursor.getColumnIndex("VALUETYPE")));
        archiveRecordItem.setValueUnit(cursor.getString(cursor.getColumnIndex("VALUEUNIT")));
        archiveRecordItem.setValueValidator(cursor.getString(cursor.getColumnIndex("VALUEVALIDATOR")));
        archiveRecordItem.setWidgetType(cursor.getInt(cursor.getColumnIndex("WIDGETTYPE")));
        archiveRecordItem.setAnalyseType(cursor.getInt(cursor.getColumnIndex("ANALYSETYPE")));
        archiveRecordItem.setAnalyseName(cursor.getString(cursor.getColumnIndex("ANALYSENAME")));
        archiveRecordItem.setAnalyseCategory(cursor.getString(cursor.getColumnIndex("ANALYSECATEGORY")));
        archiveRecordItem.setFieldValueGenerator(cursor.getString(cursor.getColumnIndex("FIELDVALUEGENERATOR")));
    }

    public ArchiveRecordItem fillRecordItem(Cursor cursor) {
        ArchiveRecordItem archiveRecordItem = new ArchiveRecordItem();
        if (cursor.getColumnIndex("archiveRecordRowId") != -1) {
            archiveRecordItem.setLocalArchiveRecordId(cursor.getLong(cursor.getColumnIndex("ARCHIVERECORDROWID")));
        }
        archiveRecordItem.setArchiveRecordSubId(cursor.getLong(cursor.getColumnIndex("ARCHIVERECORDSUBID")));
        archiveRecordItem.setArchiveTemplateSubId(cursor.getLong(cursor.getColumnIndex("ARCHIVETEMPLATESUBID")));
        archiveRecordItem.setComments(cursor.getString(cursor.getColumnIndex("COMMENTS")));
//        archiveRecordItem.setDefaultValue(cursor.getString(cursor.getColumnIndex("DEFAULTVALUE")));
        archiveRecordItem.setDisable(cursor.getInt(cursor.getColumnIndex("DISABLE")));
        archiveRecordItem.setDisplayName(cursor.getString(cursor.getColumnIndex("DISPLAYNAME")));
        archiveRecordItem.setEngAbbr(cursor.getString(cursor.getColumnIndex("ENGABBR")));
        archiveRecordItem.setHintWords(cursor.getString(cursor.getColumnIndex("HINTWORDS")));
        archiveRecordItem.setItemIndex(cursor.getInt(cursor.getColumnIndex("ITEMINDEX")));
        archiveRecordItem.setItemName(cursor.getString(cursor.getColumnIndex("ITEMNAME")));
        archiveRecordItem.setItemType(cursor.getInt(cursor.getColumnIndex("ITEMTYPE")));
        archiveRecordItem.setOptionSource(cursor.getString(cursor.getColumnIndex("OPTIONSOURCE")));
        archiveRecordItem.setPreconditionIndex(cursor.getLong(cursor.getColumnIndex("PRECONDITIONINDEX")));
        archiveRecordItem.setPreconditionValue(cursor.getString(cursor.getColumnIndex("PRECONDITIONVALUE")));
        archiveRecordItem.setReferenceRange(cursor.getString(cursor.getColumnIndex("REFERENCERANGE")));
        archiveRecordItem.setRequiredFlag(cursor.getInt(cursor.getColumnIndex("REQUIREDFLAG")));
        archiveRecordItem.setValue(cursor.getString(cursor.getColumnIndex("VALUE")));
        archiveRecordItem.setValueType(cursor.getInt(cursor.getColumnIndex("VALUETYPE")));
        archiveRecordItem.setValueUnit(cursor.getString(cursor.getColumnIndex("VALUEUNIT")));
        archiveRecordItem.setValueValidator(cursor.getString(cursor.getColumnIndex("VALUEVALIDATOR")));
        archiveRecordItem.setWidgetType(cursor.getInt(cursor.getColumnIndex("WIDGETTYPE")));
//        archiveRecordItem.setMarkType(cursor.getInt(cursor.getColumnIndex("MARKTYPE")));
        archiveRecordItem.setAnalyseType(cursor.getInt(cursor.getColumnIndex("ANALYSETYPE")));
        archiveRecordItem.setAnalyseName(cursor.getString(cursor.getColumnIndex("ANALYSENAME")));
        archiveRecordItem.setAnalyseCategory(cursor.getString(cursor.getColumnIndex("ANALYSECATEGORY")));
        archiveRecordItem.setFieldValueGenerator(cursor.getString(cursor.getColumnIndex("FIELDVALUEGENERATOR")));
        archiveRecordItem.setItemScore(cursor.getFloat(cursor.getColumnIndex("ITEMSCORE")));

        return archiveRecordItem;
    }

    public void testMultipleRead(View v) {
        startActivity(new Intent(this, MultipleReadActivity.class));
    }

    public void testMultipleReadAndWrite(View v) {
        startActivity(new Intent(this, MultipleReadAndWrite.class));
    }
}
