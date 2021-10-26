package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 19:36
 * ----------------------
 * ---------类描述--------
 * 目录controller
 *
 **/
@Controller
@ResponseBody
public class CatagoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("admin/category/add")
    @ApiOperation("后台添加商品分类")
    public ApiRestResponse addCategory(HttpSession session, @RequestBody @Valid AddCategoryReq addCategoryReq){

        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser==null) {
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_LOGIN));
        }
        //校验是否是管理员
        if (!userService.checkAdminRole(currentUser)) {
            ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NOT_ADMIN));
        }
        categoryService.add(addCategoryReq);
        return ApiRestResponse.success();

    }

    @PostMapping("admin/category/update")
    @ApiOperation("后台更新目录")
    public ApiRestResponse updateCategory(HttpSession session, @RequestBody @Valid UpdateCategoryReq updateCategoryReq){
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser==null) {
            return ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NEED_LOGIN));
        }
        //校验是否是管理员
        if (!userService.checkAdminRole(currentUser)) {
            ApiRestResponse.error(new ImoocMallException(ImoocMallExceptionEnum.NOT_ADMIN));
        }
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq,category);
        categoryService.update(category);
        return ApiRestResponse.success();

    }

    @PostMapping("admin/category/delete")
    @ApiOperation("后台删除目录")
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return ApiRestResponse.success();
    }
    @PostMapping("admin/category/list")
    @ApiOperation("后台查询列表")
    public ApiRestResponse listCateGoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize)
    {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);

    }

    @PostMapping("category/list")
    @ApiOperation("前台目录列表")
    public ApiRestResponse listCateGoryForCustom()
    {
        List<CategoryVO> categoryVOS = categoryService.listForCustom(0);
        return ApiRestResponse.success(categoryVOS);

    }
}
