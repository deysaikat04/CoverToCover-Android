package com.example.cover;

public class Subject  {

    String SubName = null;
    String SubFullForm = null;

    public Subject(String Sname, String SFullForm) {

        super();

        this.SubName = Sname;

        this.SubFullForm = SFullForm;
    }

    public String getSubName() {

        return SubName;

    }
    public void setSubName(String code) {

        this.SubName = code;

    }
    public String getSubFullForm() {

        return SubFullForm;

    }
    public void setSubFullForm(String name) {

        this.SubFullForm = name;

    }

    @Override
    public String toString() {

        return  SubName + " " + SubFullForm ;

    }

}