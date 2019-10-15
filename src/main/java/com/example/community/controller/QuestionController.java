package com.example.community.controller;

import com.example.community.dto.QuestionDTO;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(Model model, @PathVariable("id") Long id){
        QuestionDTO questionDTO=questionService.queryById(id);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }

}
