package com.example.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND( 2001,"您请求的问题不存在"),
    TARGET_PARAM_NOT_FOUND( 2002,"您请求的问题或评论不存在了"),
    NO_LOGIN( 2003,"未登录,请登录后重试"),
    SYS_ERROR( 2004,"服务冒烟，请您稍后试试"),
    TYPE_PARAM_NOT_FOUND( 2005,"您回复评论的类型不存在"),
    COMMENT_NOT_FOUND( 2006,"宁回复的评论不存在"),
    CONTENT_IS_EMPTY(2007,"您评论的内容不能为空"),
    NOTIFIER_NOT_FOUND(2008,"您访问的通知不见了！"),
    NOTIFIER_NOT_MATCH(2009,"这不是你的通知"),
    FILE_UPLOAD_FAIL(2010,"文件上传失败" +
            ""),
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
