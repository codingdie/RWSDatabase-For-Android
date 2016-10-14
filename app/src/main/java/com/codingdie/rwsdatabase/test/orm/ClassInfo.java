package com.codingdie.rwsdatabase.test.orm;

import com.codingdie.rwsdatabase.orm.annotation.Table;

import java.util.List;

/**
 * Created by xupen on 2016/10/12.
 */
public class ClassInfo {
    private  int classId;
    private  String className;
    private List<Student> students;
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
