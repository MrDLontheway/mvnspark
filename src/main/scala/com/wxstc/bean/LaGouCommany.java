package com.wxstc.bean;

import java.io.Serializable;

public class LaGouCommany implements Serializable{
    public long id;
    public String commany_name=" ";
    public String commany_jianjie=" ";
    public long job_number;
    //public long comments;
    public String company_word=" ";
    public String commany_biaoqian=" ";
    public String commany_area=" ";
    public String rongzi=" ";
    public String commany_member=" ";
    public String address=" ";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommany_name() {
        return commany_name;
    }

    public void setCommany_name(String commany_name) {
        this.commany_name = commany_name;
    }

    public String getCommany_jianjie() {
        return commany_jianjie;
    }

    public void setCommany_jianjie(String commany_jianjie) {
        this.commany_jianjie = commany_jianjie;
    }

    public long getJob_number() {
        return job_number;
    }

    public void setJob_number(long job_number) {
        this.job_number = job_number;
    }

/*    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }*/

    public String getCompany_word() {
        return company_word;
    }

    public void setCompany_word(String company_word) {
        this.company_word = company_word;
    }

    public String getCommany_biaoqian() {
        return commany_biaoqian;
    }

    public void setCommany_biaoqian(String commany_biaoqian) {
        this.commany_biaoqian = commany_biaoqian;
    }

    public String getCommany_area() {
        return commany_area;
    }

    public void setCommany_area(String commany_area) {
        this.commany_area = commany_area;
    }

    public String getRongzi() {
        return rongzi;
    }

    public void setRongzi(String rongzi) {
        this.rongzi = rongzi;
    }

    public String getCommany_member() {
        return commany_member;
    }

    public void setCommany_member(String commany_member) {
        this.commany_member = commany_member;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LaGouCommany{" +
                "id=" + id +
                ", commany_name='" + commany_name + '\'' +
                ", commany_jianjie='" + commany_jianjie + '\'' +
                ", job_number=" + job_number +
                ", company_word='" + company_word + '\'' +
                ", commany_biaoqian='" + commany_biaoqian + '\'' +
                ", commany_area='" + commany_area + '\'' +
                ", rongzi='" + rongzi + '\'' +
                ", commany_member='" + commany_member + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
