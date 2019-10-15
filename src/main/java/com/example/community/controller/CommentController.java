package com.example.community.controller;

import com.example.community.dto.CommentDTO;
import com.example.community.dto.ResultDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.model.Comment;
import com.example.community.model.User;
import com.example.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/comment")
    @ResponseBody
    public Object postComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            ResultDTO resultDTO=ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            return resultDTO;
        }
        if (commentDTO==null||StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentor(user.getId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
