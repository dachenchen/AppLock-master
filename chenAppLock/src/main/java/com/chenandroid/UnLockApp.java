package com.chenandroid;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * 程序信息
 *
 */
@Table
public class UnLockApp extends SugarRecord {

    private String packageName;
    private String name;

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    private String sortLetters;  //显示数据拼音的首字母


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String icon;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;



    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
