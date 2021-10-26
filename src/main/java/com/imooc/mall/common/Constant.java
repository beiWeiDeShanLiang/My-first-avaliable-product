package com.imooc.mall.common;

import com.google.common.collect.Sets;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 7:58
 * ----------------------
 * ---------类描述--------
 **/
@Component
public class Constant {
    public static final  String salt ="wochaojishuai";
    public static final  String MALL_USER="mall_user";
    public static final  Integer ADMIN=2;
    public static final  Integer SALE_STATUS=1;
    public static  String File_upload_dir;
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String file_upload_dir)
    {
        File_upload_dir=file_upload_dir;
    }

    public interface ProductListOrderBy{
       Set<String> PRICE_ASC_DESC= Sets.newHashSet("price desc","price asc");
    }
    public interface Cart{
        String select="1";
        String unSelect="0";
    }

    public enum OrderStatusEnum{

        CANCELED(0,"用户已取消"),
        NOT_PAID(10,"未付款"),
        PAID(20,"已付款"),
        DELIVERED(30,"已发货"),
        FINISHED(40,"交易完成");
        private Integer code;
        private String value;

        OrderStatusEnum(Integer code,String value){
            this.value=value;
            this.code=code;
        }

        public static OrderStatusEnum codeOf(Integer code)
        {
            for (OrderStatusEnum orderStatusEnum :values())
            {
                if (code==orderStatusEnum.getCode())
                {
                    return orderStatusEnum;
                }
            }
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ENUM);

        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
