package com.wxstc.es;

import java.io.Serializable;

public class Sc2 implements Serializable{
    private int id;
    private long number;
    private String schoolName;
    private String province;
    private String level;
    private String schoolnature;
    private String guanwang;

    public Sc2(int id,long number,String schoolName,
        String province,
                String level,
                String schoolnature,
                String guanwang){
        this.id=id;
        this.number=number;
        this.schoolName=schoolName;
        this.province=province;
        this.level=level;
        this.schoolnature=schoolnature;
        this.guanwang=guanwang;
    }
    @Override
    public String toString() {
        return "Sc2{" +
                "id=" + id +
                ", number=" + number +
                ", schoolName='" + schoolName + '\'' +
                ", province='" + province + '\'' +
                ", level='" + level + '\'' +
                ", schoolnature='" + schoolnature + '\'' +
                ", guanwang='" + guanwang + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSchoolnature() {
        return schoolnature;
    }

    public void setSchoolnature(String schoolnature) {
        this.schoolnature = schoolnature;
    }

    public String getGuanwang() {
        return guanwang;
    }

    public void setGuanwang(String guanwang) {
        this.guanwang = guanwang;
    }
}
