package com.example.community.service;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Integer pageIndex, Integer size) {
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=questionMapper.count();
        //计算总页数
        Integer totalPage;
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }

        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        paginationDTO.setQuestionDTOS(questionDTOS);
        //查询分页数据
        Integer offset=(pageIndex-1)*size;
        List<Question> questions=questionMapper.list(offset,size);

        for (Question question :
                questions) {
            User user =userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userId, Integer pageIndex, Integer size) {
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=questionMapper.countByUserId(userId);

        //计算总页数
        Integer totalPage;
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        paginationDTO.setQuestionDTOS(questionDTOS);

        //查询分页数据
        Integer offset=(pageIndex-1)*size;
        List<Question> questions=questionMapper.listByUserId(userId,offset,size);

        for (Question question :
                questions) {
            User user =userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        return paginationDTO;
    }

    public QuestionDTO queryById(Integer id) {
        Question question=questionMapper.queryById(id);
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user =userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return  questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
