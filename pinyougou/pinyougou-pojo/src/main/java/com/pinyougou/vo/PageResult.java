package com.pinyougou.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Date:2018/12/11
 * Author 唐鹏
 * DESC: 处理分页查询的数据 封装总记录数和每页显示的数据
 */
public class PageResult implements Serializable {
    private Long total; //总记录数
    private List<?> rows;  //每页显示的数据

    public PageResult(Long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
