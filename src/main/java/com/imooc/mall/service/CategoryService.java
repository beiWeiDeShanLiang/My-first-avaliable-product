package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    void add (AddCategoryReq addCategoryReq) ;

    void update (Category category);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listForCustom(Integer parentId);
}
