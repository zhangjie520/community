package com.example.community.controller;

import com.example.community.dto.PaginationDTO;
import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
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

@Controller
public class ProfileController {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          Model model,
                          @RequestParam(value = "page",defaultValue = "1") Integer pageIndex,
                          @RequestParam(value = "size",defaultValue = "2") Integer  size,
                          HttpServletRequest request){
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","我的回复");
        }
        User user=(User)request.getSession().getAttribute("user");
        if (user == null) {
//            request.getSession().setAttribute("error","用户未登录");
            model.addAttribute("error", "用户未登录");
            return "redirect:/";
        }
        //将查询到的我的question放入model
        PaginationDTO paginationDTOS =questionService.listByUserId(user.getId(),pageIndex,size);
        model.addAttribute("paginationS",paginationDTOS);
        return "profile";
    }
}
