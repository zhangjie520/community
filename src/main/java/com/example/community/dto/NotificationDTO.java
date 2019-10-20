package com.example.community.dto;

import com.example.community.model.Question;
import com.example.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Long receiver;
    private Long outerId;//当是回复question时是question的Id，当回复评论时是评论对应question的id
    private Long gmtCreate;
    private Integer type;
    private Integer status;
    private String typeValue;
    private User notifierUser;
    private Question question;
}
