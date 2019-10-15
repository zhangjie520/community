package com.example.community.mapper;

import com.example.community.model.Comment;
import com.example.community.model.Question;
import com.example.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
   Integer incView(Question question);
   Integer incCommentCount(Question question
   );

}