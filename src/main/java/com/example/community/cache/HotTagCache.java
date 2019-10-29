package com.example.community.cache;

import com.example.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<String> hots;
    private static final int HOT_TAG_LENGTH = 3;

    public void updateTags(Map<String, Integer> tags) {
        Queue priorities = new PriorityQueue(HOT_TAG_LENGTH);
        tags.forEach(
                (name, priority) -> {
                    HotTagDTO hotTagDTO = new HotTagDTO();
                    hotTagDTO.setName(name);
                    hotTagDTO.setPriority(priority);
                    if (priorities.size() < HOT_TAG_LENGTH) {
                        priorities.add(hotTagDTO);
                    } else {
                        HotTagDTO minHotTag = (HotTagDTO) priorities.peek();
                        if (hotTagDTO.compareTo(minHotTag) > 0) {
                            priorities.poll();
                            priorities.add(minHotTag);
                        }
                    }
                }
        );
        List sortedHots = new ArrayList();
        HotTagDTO poll = (HotTagDTO) priorities.poll();
        while (poll != null) {
            sortedHots.add(0, poll.getName());
            poll = (HotTagDTO) priorities.poll();
        }
        this.hots = sortedHots;
    }
}
