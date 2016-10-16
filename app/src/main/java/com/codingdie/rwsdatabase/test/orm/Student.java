package com.codingdie.rwsdatabase.test.orm;

import com.codingdie.rwsdatabase.orm.annotation.RWSColum;

/**
 * Created by xupeng on 2016/10/12.
 */
public class Student {
    @RWSColum(isKey = true)
    private  int classId;
    @RWSColum(isKey = true)
    private  int studentId;
    private  String studentName;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
