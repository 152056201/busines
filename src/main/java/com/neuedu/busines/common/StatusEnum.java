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
    USER_NOT_EXIST(11,"用户名为空"),
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
