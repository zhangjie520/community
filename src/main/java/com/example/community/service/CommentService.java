package com.example.community.service;

import com.example.community.dto.CommentDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.*;
import com.example.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            //回复的问题不存在,通过处理异常的方式直接返回json,免得返回controller返回json,多一层依赖
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || CommentTypeEnum.isEmpty(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_FOUND);
        }
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0L);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            createNotification(comment,question.getCreator(), NotificationTypeEnum.QUESTION.getType(), comment.getParentId());

        } else {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            createNotification(comment, dbComment.getCommentor(), NotificationTypeEnum.COMMENT.getType(), question.getId());
        }
    }

    /**
     * 在回复问题和评论时写入一条通知
     * @param comment
     * @param receiver
     * @param type
     * @param questionId
     */
    private void createNotification(Comment comment, Long receiver, Integer type, Long questionId) {
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentor());
        notification.setReceiver(receiver);
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setOuterId(questionId);
        notification.setStatus(NotificationStatusEnum.NOTREADED.getStatus());
        notification.setType(type);
        notificationMapper.insert(notification);
    }


    public List<CommentDTO> listByQuestionId(Long id, CommentTypeEnum type) {
        CommentExample commentExamplele = new CommentExample();
        commentExamplele.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExamplele.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExamplele);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
//        List<CommentDTO> commentDTOS=new ArrayList<>();
//        for (Comment comment : comments) {
//            User user = userMapper.selectByPrimaryKey(comment.getCommentor());
//            CommentDTO commentDTO=new CommentDTO();
//            commentDTO.setUser(user);
//            BeanUtils.copyProperties(comment,commentDTO);
//            commentDTOS.add(commentDTO);
//        }
        //优化，因为有些评论user是一个，所以用lombda,set获取去重后的userIds
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentor()).distinct().collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //将users转换为usermap，避免用for判断user.id==comment.commentator造成n平方时间复杂度
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentor()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        String[] tags = queryDTO.getTag().split(",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question queryQuestion = new Question();
        queryQuestion.setId(queryDTO.getId());
        queryQuestion.setTag(regexpTag);
        List<Question> questionRelated = questionExtMapper.selectRelated(queryQuestion);
        List<QuestionDTO> questionDTOS = questionRelated.stream().map(question -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
