package com.neuedu.busines.common;

public enum StatusEnum {
    USERNAME_NULL(1,"用户名为空"),
    PASSWORD_NULL(2,"密码为空"),
    EMAIL_NULL(3,"邮箱为空"),
    PHONE_NULL(4,"电话为空"),
    QUESTION_NULL(5,"问题为空"),
    ANSWER_NULL(5,"答案为空"),
    USER_NOT_ONLY(6,"用户名存在"),
    EMAIL_NOT_ONLY(7,"邮箱存在"),
    RESGISTER_FAILED(8,"注册失败"),
    USER_NOT_EMPTY(9,"用户名为空"),
    PASSWORD_NOT_EMPTY(10,"密码为空"),
    USER_NOT_EXIST(11,"用户不存在"),
    USERINFO_UPDATA_FAIL(16,"用户信息更新失败"),
    PARENTID_NOT_NULL(12,"parentId为空"),
    CATEGORYNAME_NOT_NULL(13,"名称为空"),
    CATEGORY_INSERT_FAIL(14,"类别添加失败"),
    CATEGORYID_NOT_NULL(15,"CATEGORYID为空"),
    USER_OUT_LOGIN(100,"用户未登录"),
    NO_AUTHORITY(101,"无访问权限"),
    CATEGORY_NOT_FIND(103,"该分类不存在"),
    CATEGORY_UPDATE_FAIL(104,"名称更新失败"),
    UPLOAD_NAME_NOT_NULL(105,"上传文件名称不能为空"),
    PARAM_NOT_NULL(106,"参数不能为空"),
    PRODUCT_ADD_FAIL(107,"商品添加失败"),
    PRODUCT_NOT_EXISTS(108,"该商品不存在"),
    PRODUCT_UPDATE_FAIL(109,"商品更新失败"),
    PRODUCT_ADD_CART_FAIL(111,"商品添加购物车失败"),
    PRODUCT_UPDATE_CART_FAIL(112,"更新购物车失败"),
    ;
    private int code;

    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
