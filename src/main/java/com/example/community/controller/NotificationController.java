package com.example.community.controller;

import com.example.community.dto.NotificationDTO;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.model.User;
import com.example.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String notification(HttpServletRequest request, Model model,
                        @PathVariable("id") Long notificationId) {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(notificationId,user);
        if (notificationDTO.getType()==NotificationTypeEnum.COMMENT.getType()
                ||notificationDTO.getType()==NotificationTypeEnum.QUESTION.getType()){
            return "redirect:/question/"+notificationDTO.getOuterId()+"";
        }
        return "redirect:/";
    }
}
