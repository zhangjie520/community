package com.example.community.service;

import com.example.community.mapper.AdMapper;
import com.example.community.model.Ad;
import com.example.community.model.AdExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdService {
    @Autowired
    private AdMapper adMapper;
    public List<Ad> list(String pos){
        AdExample adExample = new AdExample();
        adExample.createCriteria()
                .andStatusEqualTo(1)
                .andPosEqualTo(pos)
                .andGmtStartLessThan(System.currentTimeMillis())
                .andGmtEndGreaterThan(System.currentTimeMillis());
        List<Ad> ads = adMapper.selectByExample(adExample);
        return ads;
    }
}
