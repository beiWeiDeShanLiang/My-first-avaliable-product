package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import com.imooc.mall.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 7:10
 * ----------------------
 * ---------类描述--------
 * 商品服务实现类
 **/
@Service
public class ProductServiceImpl  implements ProductService {
    @Autowired
    ProductMapper mapper;

    @Autowired
    CategoryService categoryService;
    @Override
    public void add(AddProductReq req)
    {
        Product product = new Product();
        BeanUtils.copyProperties(req,product);
        Product productOld = mapper.selectByname(req.getName());
        if (productOld!=null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.FORBID_SAME_NAME);
        }
        int i = mapper.insertSelective(product);
        if (i==0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct)
    {
//        检测是否有同名
        Product productOld = mapper.selectByname(updateProduct.getName());
        if (productOld!=null &&!productOld.getId().equals(updateProduct.getId())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.FORBID_SAME_NAME);
        }
        int count = mapper.updateByPrimaryKeySelective(updateProduct);
        if (count==0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAIL);

        }
    }

    @Override
    public void delete(Integer id)
    {
//        检测是否有同名
        Product productOld = mapper.selectByPrimaryKey(id);
        if (productOld==null ) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
        int count = mapper.deleteByPrimaryKey(id);
        if (count==0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);

        }
    }
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus)
    {
        mapper.batchUpdateSellStatus(ids,sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage (pageNum,pageSize);
        List<Product> products = mapper.listForAdmin();
        PageInfo<Product> productPageInfo = new PageInfo<>(products);
        return productPageInfo;
    }

    @Override
    public Product detail(Integer id)
    {
        Product product = mapper.selectByPrimaryKey(id);
        return product;

    }

    @Override
    public PageInfo list(ProductListReq productListReq)
    {
        //构建query
        ProductListQuery productListQuery = new ProductListQuery();
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String key = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(key);
        }
        if (productListReq.getCategoryId()!=null)
        {

            List<CategoryVO> categoryVOS = categoryService.listForCustom(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            getCategoryIds(categoryIds,categoryVOS);
            productListQuery.setCategoryIds(categoryIds);
        }
        String orderBy=productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
        }else
        {            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());


        }
        List<Product> products = mapper.selectListForCustom(productListQuery);
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    private void getCategoryIds(ArrayList categoryIds,List<CategoryVO> categoryVOS)
    {
        for (int i = 0; i < categoryVOS.size(); i++) {
            CategoryVO categoryVO = categoryVOS.get(i);
            if (categoryVO!=null)
            {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryIds,categoryVO.getChildCategory());
            }

        }

    }

}
