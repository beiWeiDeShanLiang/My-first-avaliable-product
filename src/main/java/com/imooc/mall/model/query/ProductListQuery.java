package com.imooc.mall.model.query;

import com.imooc.mall.model.pojo.Category;

import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 21:15
 * ----------------------
 * ---------类描述--------
 **/
public class ProductListQuery {
    private String keyword;
    private List<Integer> categoryIds;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
