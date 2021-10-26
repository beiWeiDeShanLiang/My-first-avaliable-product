package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/25日 6:49
 * ----------------------
 * ---------类描述--------
 * 后台商品管理Controller
 *
 **/
@Controller
@ResponseBody
public class ProductAdminController {
    @Autowired
    ProductService productService;
    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq req){
        productService.add(req);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/product/uploadFile")
    public ApiRestResponse upload (HttpServletRequest request, @RequestParam("file")
                                   MultipartFile file){
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        File fileDir = new File(Constant.File_upload_dir);
        File destFile = new File(Constant.File_upload_dir + newFileName);
        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAIL);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATED_FAILED);
        }
        //生成URI
        try {
            return ApiRestResponse.success(getHost(new URI(request.getRequestURL()+""))+"/images/"
            +newFileName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ApiRestResponse.error( new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAIL));
        }
    }
    @ApiOperation("后台更新商品")
    @PostMapping("admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.update(product);
        return ApiRestResponse.success();


    }
    @ApiOperation("后台删除商品")
    @PostMapping("admin/product/delete")
    public ApiRestResponse updateProduct(@RequestParam Integer id){

        productService.delete(id);
        return ApiRestResponse.success();


    }

    @ApiOperation("后台批量上下架")
    @PostMapping("admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus( @RequestParam("ids") Integer[] ids,@RequestParam("status") Integer status){

        productService.batchUpdateSellStatus(ids, status);
        return ApiRestResponse.success();


    }

    @ApiOperation("后台商品列表")
    @PostMapping("admin/product/list")
    public ApiRestResponse list( @RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize){

        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);


    }





    private URI getHost(URI uri)
    {
        URI effectiveUri=null;
        try {
             effectiveUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return effectiveUri;
    }
}
