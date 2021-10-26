package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 19:38
 * ----------------------
 * ---------类描述--------
 **/
public class AddCategoryReq {
    @Size(min = 2,max=8)
    @NotNull
    private String name;
    @NotNull(message = "parentId不能为null")
    @Max(3)
    private Integer type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @NotNull
    private Integer  parentId;

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    @NotNull
    private Integer orderNum;
}
