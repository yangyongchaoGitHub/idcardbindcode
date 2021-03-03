package com.dataexpo.cbi.pojo;

import java.util.List;
import java.util.Map;

public class NetResult<T> {
    private Integer code;
    private String msg;

    /**
     * 相关消息
     */
    private List<Map<String, Object>> dataList;
    private Long count;

    /**
     * 返回到移动端的数据对象
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
