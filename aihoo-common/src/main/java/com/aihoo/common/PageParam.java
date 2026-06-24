package com.aihoo.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分页查询参数")
public class PageParam<T> extends Page<T> {

    @Schema(description = "页码", example = "1")
    private long page = 1;

    @Schema(description = "每页数量", example = "10")
    private long limit = 10;

    public PageParam() {
        super();
    }

    public PageParam(long page, long limit) {
        super(page, limit);
        this.page = page;
        this.limit = limit;
    }

    @Override
    public long getCurrent() {
        return this.page;
    }

    @Override
    public long getSize() {
        return this.limit;
    }

    public long getPage() { return page; }
    public void setPage(long page) { this.page = page; }
    public long getLimit() { return limit; }
    public void setLimit(long limit) { this.limit = limit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageParam<?> param)) return false;
        if (!super.equals(o)) return false;
        return page == param.page && limit == param.limit;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Long.hashCode(page);
        result = 31 * result + Long.hashCode(limit);
        return result;
    }
}