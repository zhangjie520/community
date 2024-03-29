package com.example.community.controller;

import com.example.community.cache.TagCache;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import com.example.community.service.QuestionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Resource
    QuestionMapper questionMapper;
    @Resource
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("categories",TagCache.getCategory());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublic(@RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam(value = "tag", required = false) String tag,
                           @RequestParam(value = "id",required = false) Long id,
                           HttpServletRequest request,
                           Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("categories",TagCache.getCategory());
        if (title == null || title=="") {
            model.addAttribute("error", "问题标题不能为空");
            return "publish";
        }
        if (description == null || description=="") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag=="") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        if (StringUtils.isNotBlank(TagCache.isValid(tag))){
            model.addAttribute("error", "标签输入不合法");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);


        User user=(User)request.getSession().getAttribute("user");
        if (user == null) {
//            request.getSession().setAttribute("error","用户未登录");
            model.addAttribute("error", "用户未登录");
            return "redirect:/";
        }
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
//        questionMapper.insert(question);
        return "redirect:/";
    }
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        QuestionDTO question=questionService.queryById(id);
        model.addAttribute("id",question.getId());
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("categories",TagCache.getCategory());
        return "publish";
    }
}
