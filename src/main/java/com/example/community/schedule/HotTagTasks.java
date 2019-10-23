package com.example.community.schedule;

import com.example.community.cache.HotTagCache;
import com.example.community.mapper.QuestionMapper;
import com.example.community.model.Question;
import com.example.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;
    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime(){
        int offset=0;
        int limit=5;
        List<Question> list=new ArrayList();
        log.info("This start time is now {}",new Date());
        Map<String ,Integer> properties=new HashMap<>();
        while (offset==0||list.size()==limit){
            list=questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,limit));
            for (Question question : list) {
                log.info("list question id {}",question.getId());
                String[] tags = question.getTag().split(",");
                for (String tag : tags) {
                    Integer property = properties.get(tag);
                    if (property!=null){
                        properties.put(tag,property+5+question.getCommentCount());
                    }else {
                        properties.put(tag,5+question.getCommentCount());
                    }
                }
            }
            offset+=limit;
        }
        hotTagCache.updateTags(properties);
        log.info("This end time is now {}",new Date());
    }
}
