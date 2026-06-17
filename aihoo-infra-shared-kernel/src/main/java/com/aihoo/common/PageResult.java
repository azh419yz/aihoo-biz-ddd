package com.aihoo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "分页结果")
public class PageResult<T> {

    @JsonProperty("count")
    @Schema(description = "总数量")
    private long count;

    @JsonProperty("data")
    @Schema(description = "当前页数据")
    private List<T> data;

    private String msg;

    public PageResult() {
    }

    public PageResult(String msg) {
        this.msg = msg;
    }

    public PageResult(List<T> data, long count) {
        this.count = count;
        this.data = data;
    }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
}