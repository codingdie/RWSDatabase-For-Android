package com.codingdie.rwsdatabase.test.orm;

import com.codingdie.rwsdatabase.orm.annotation.Colum;

/**
 * Created by xupen on 2016/10/12.
 */
public class Student {
    @Colum(isKey = true)
    private  int classId;
    @Colum(isKey = true)
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
