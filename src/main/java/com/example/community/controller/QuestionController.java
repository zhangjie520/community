package com.example.community.controller;

import com.example.community.dto.CommentDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.service.CommentService;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(Model model, @PathVariable("id") Long id){
        QuestionDTO questionDTO=questionService.queryById(id);
        questionService.incView(id);
        List<QuestionDTO> relatedQuestions=commentService.selectRelated(questionDTO);
        List<CommentDTO> comments=commentService.listByQuestionId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments",comments);
        model.addAttribute("question",questionDTO);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }

}
