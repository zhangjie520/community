package com.example.community.controller;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.mapper.NotificationMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Notification;
import com.example.community.model.User;
import com.example.community.service.NotificationService;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1") Integer pageIndex,
                          @RequestParam(value = "size",defaultValue = "5") Integer  size,
                          HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if (user == null) {
//            request.getSession().setAttribute("error","用户未登录");
            model.addAttribute("error", "用户未登录");
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");

            //将查询到的我的question放入model
            PaginationDTO paginationDTOS =questionService.listByUserId(user.getId(),pageIndex,size);
            model.addAttribute("paginationS",paginationDTOS);
        }
        if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");

            PaginationDTO<NotificationDTO> paginationDTO = notificationService.listByReceiver(user, pageIndex, size);
            int unreadCount = paginationDTO.getData().stream().filter(notificationDTO -> notificationDTO.getStatus() == 0).collect(Collectors.toList()).size();
            model.addAttribute("paginationS",paginationDTO);
            model.addAttribute("unreadCount",unreadCount);
        }

        return "profile";
    }
}
