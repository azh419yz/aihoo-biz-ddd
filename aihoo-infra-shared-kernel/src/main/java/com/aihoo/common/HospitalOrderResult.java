package com.aihoo.common;

import java.util.List;

/**
 * 线下订单报表业务特需特定的返回类型
 * 特殊业务分页结果对象,这里以layui框架的table为标准
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class HospitalOrderResult<T> {

    private int code; // 状态码, 0表示成功

    private String msg;  // 提示信息

    private long count; // 需要数量, bootstrapTable是total

    private long allCount; // 总数居

    private List<T> data; // 当前数据, bootstrapTable是rows

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
