package com.example.community.service;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.QuestionExtMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.QuestionExample;
import com.example.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    private QuestionExtMapper questionExtMapper;
    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Integer pageIndex, Integer size) {
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=(int) questionMapper.countByExample(new QuestionExample());
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
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));

        for (Question question :
                questions) {
            User user =userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        return paginationDTO;
    }

    public PaginationDTO listByUserId(Long userId, Integer pageIndex, Integer size) {
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        PaginationDTO paginationDTO=new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount=(int)questionMapper.countByExample(example);

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
        QuestionExample questionExample = new QuestionExample();
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));

        for (Question question :
                questions) {
            User user =userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        return paginationDTO;
    }

    public QuestionDTO queryById(Long id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user =userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return  questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //新增
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        }else {
            //更新
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setId(question.getId());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTitle(question.getTitle());
            Integer updateResult=questionMapper.updateByPrimaryKeySelective(updateQuestion);
            if (updateResult!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incView(updateQuestion);
    }
}
