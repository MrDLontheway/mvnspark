package com.wxstc.bean;

import java.io.Serializable;

public class LaGouJob implements Serializable,Comparable<LaGouJob>{
    public long id;
    public String job_name;
    public String salary;
    public String address;
    public String experience;
    public String education;
    public String job_type;
    public String label;
    public String date;
    public String job_desc;
    public String commany;
    public String commany_jianjie;
    public String job_youhuo;

    public String getJob_youhuo() {
        return job_youhuo;
    }

    public void setJob_youhuo(String job_youhuo) {
        this.job_youhuo = job_youhuo;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getCommany() {
        return commany;
    }

    public void setCommany(String commany) {
        this.commany = commany;
    }

    public String getCommany_jianjie() {
        return commany_jianjie;
    }

    public void setCommany_jianjie(String commany_jianjie) {
        this.commany_jianjie = commany_jianjie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LaGouJob{" +
                "id=" + id +
                ", job_name='" + job_name + '\'' +
                ", salary='" + salary + '\'' +
                ", address='" + address + '\'' +
                ", experience='" + experience + '\'' +
                ", education='" + education + '\'' +
                ", job_type='" + job_type + '\'' +
                ", label='" + label + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(LaGouJob o) {
        return 0;
    }
}
