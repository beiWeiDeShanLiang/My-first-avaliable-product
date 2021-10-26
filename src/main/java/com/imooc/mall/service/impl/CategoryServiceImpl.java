package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 20:19
 * ----------------------
 * ---------类描述--------
 * 目录分类实现类
 **/
@Service
public class CategoryServiceImpl  implements CategoryService {
    @Autowired
    CategoryMapper mapper;
    @Override
    public void add(AddCategoryReq addCategoryReq)  {
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = mapper.selectByName(category.getName());
        if (categoryOld!=null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.FORBID_SAME_NAME);
        }
        int i = mapper.insertSelective(category);
        if (i==0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATED_FAILED);
        }
    }

    @Override
    public void update(Category category)
    {
        Category categoryOld = mapper.selectByName(category.getName());
        if (categoryOld!=null && categoryOld.getId().equals(category.getId())) {
            throw new ImoocMallException( ImoocMallExceptionEnum.FORBID_SAME_NAME);
        }
        int count = mapper.updateByPrimaryKeySelective(category);
        if (count ==0)
        {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }


    }

    @Override
    public void delete(Integer id){
        Category cateOld = mapper.selectByPrimaryKey(id);
        if (cateOld==null) {
            throw new  ImoocMallException(ImoocMallExceptionEnum.NOT_EXISTENT);
        }
        int count = mapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_EXISTENT);
        }

    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categories = mapper.selectList();
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categories);
        return categoryPageInfo;
    }

    @Override
    @Cacheable("listForCustom")
    public List<CategoryVO> listForCustom(Integer parentId){
        ArrayList<CategoryVO> categoryVOS = new ArrayList<>();
        recursivelyFindCategories(categoryVOS,parentId);
        return categoryVOS;
    }



    //调用同样的方法获取 用递归

    private void recursivelyFindCategories( List<CategoryVO> categoryVOS,Integer parentId){
        //递归获取所有子类别，并组合为一个目录树
        List<Category> categories = mapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOS.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }

    }
//


}
