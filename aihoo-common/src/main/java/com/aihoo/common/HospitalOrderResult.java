package com.aihoo.common;

import java.util.List;

public class HospitalOrderResult<T> {

    private int code;

    private String msg;

    private long count;

    private long allCount;

    private List<T> data;

    public HospitalOrderResult() {
    }

    public HospitalOrderResult(List<T> rows) {
        this(rows, rows.size() ,rows.size());
    }

    public HospitalOrderResult(List<T> rows, long total , long allTotal) {
        this.count = total;
        this.allCount = allTotal;
        this.data = rows;
        this.code = 200;
        this.msg = "执行成功";
    }

    public HospitalOrderResult(String error){
        this.code=500;
        this.msg=error;
    }

    public long getAllCount() {
        return allCount;
    }

    public void setAllCount(long allCount) {
        this.allCount = allCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
