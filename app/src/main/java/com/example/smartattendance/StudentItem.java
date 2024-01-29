package com.example.smartattendance;

public class StudentItem {
    private long sid;
    private int StudentId;
    private String StudentName;
    private String status;

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int StudentId) {
        this.StudentId = StudentId;
    }

    public String getStudentName() {
        return StudentName;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public long getSid() {
        return sid;
    }
    public void setSid(long sid) {
        this.sid = sid;
    }
    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
    }
    public StudentItem(long sid,int StudentId,String StudentName){
        this.sid=sid;
        this.StudentId=StudentId;
        this.StudentName=StudentName;
        status="";
    }
}
