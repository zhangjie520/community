package com.example.community.enums;

public enum NotificationTypeEnum {
    QUESTION(1,"回复了问题"),
    COMMENT(2,"回复了评论")
    ;

    public Integer getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    private Integer type;
    private String data;

    NotificationTypeEnum(Integer type, String data) {
        this.type = type;
        this.data = data;
    }
    public static String nameOfType(Integer type){
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType()==type){
                return notificationTypeEnum.getData();
            }
        }
        return "";
    }
}
