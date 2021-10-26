package com.imooc.mall.exception;

/**
 * 异常枚举
 */
public enum ImoocMallExceptionEnum {
    NEED_USERNAME(10001,"用户名不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码不能过短"),
    NOT_EXISTENT(10013,"项目不存在"),
    MKDIR_FAIL(10014,"文件夹创建失败"),
    UPLOAD_FAIL(10015,"文件上传失败"),
    DELETE_FAILED(10016,"删除失败"),
    NOT_ON_SALE(10017,"商品状态异常"),
    NOT_ENOUGH(10018,"商品库存不足"),
    CART_EMPTY(10019,"购物车商品为空"),
    NO_ENUM(10020,"未找到对应的枚举类"),
    ORDER_NOT_EXIST(10021,"订单不存在"),
    WRONG_ORDER_STATUS(10022,"订单状态错误"),
    FORBID_SAME_NAME(10004,"不能重名"),
    INSERT_FAILED(10005,"插入失败"),
    WRONG_PASSWORD(10006,"账号密码错误"),
    NEED_LOGIN(10007,"需要登陆"),
    UPDATE_FAILED(10008,"更新失败"),
    NOT_ADMIN(10009,"不是管理员"),
    PARAM_NOTNULL(10010,"参数不能为空"),
    PARAM_WRONG(10012,"参数错误"),
    CREATED_FAILED(10011,"新增失败"),
    SYSTEM_ERROR(20000,"系统异常"),
    NEED_PASSWORD(10002,"密码不能为空");


    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

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

    ImoocMallExceptionEnum(int i, String s) {
        this.code=i;
        this.msg=s;
    }
}
