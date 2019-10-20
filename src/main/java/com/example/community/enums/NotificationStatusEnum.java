package com.example.community.enums;

public enum NotificationStatusEnum {
    NOTREADED(0),
    READED(1)
    ;

    public Integer getStatus() {
        return status;
    }

    private Integer status;
    NotificationStatusEnum(Integer status) {
        this.status = status;
    }
}
