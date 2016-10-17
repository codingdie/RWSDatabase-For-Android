package com.codingdie.rwsdatabase.demo.orm;

import com.codingdie.rwsdatabase.orm.annotation.RWSColum;
import com.codingdie.rwsdatabase.orm.annotation.RWSTable;

import java.util.List;

/**
 * Created by xupeng on 2016/10/12.
 */
@RWSTable(name = "Class")
public class ClassInfo {
    @RWSColum(isKey = true)
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
