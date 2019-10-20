package com.example.community.service;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.CommentMapper;
import com.example.community.mapper.NotificationMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO<NotificationDTO> listByReceiver(User user, Integer pageIndex, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(user.getId());
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        //计算总页数
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex < 1) {
            pageIndex = 1;
        } else if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);


        //查询分页数据
        Integer offset = (pageIndex - 1) * size;
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOS = notifications.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setNotifierUser(user);
            Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
            notificationDTO.setQuestion(question);
            notificationDTO.setTypeValue(NotificationTypeEnum.nameOfType(notification.getType()));
            return notificationDTO;
        }).collect(Collectors.toList());
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public NotificationDTO read(Long notificationId, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if (notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFIER_NOT_FOUND);
        }
        if (notification.getReceiver()!=user.getId()){
            throw new CustomizeException(CustomizeErrorCode.NOTIFIER_NOT_MATCH);
        }
        Notification updateNotification=new Notification();
        updateNotification.setId(notificationId);
        updateNotification.setStatus(NotificationStatusEnum.READED.getStatus());

        notificationMapper.updateByPrimaryKeySelective(updateNotification);
        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        return notificationDTO;
    }

    public Long unReadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.NOTREADED.getStatus());
        long unReadCount = notificationMapper.countByExample(notificationExample);
        return unReadCount;
    }
}
