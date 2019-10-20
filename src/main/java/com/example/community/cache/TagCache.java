package com.example.community.cache;

import com.example.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> getCategory() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "go", "lang", "objective-c", "typescript", "shell", "c#", "swift", "sassbash", "ruby", "less", "asp.net", "lua", "scala", "coffee", "script", "actionscript", "rust", "erlang", "perl"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "apach", "ubuntu", "centos", "缓存", "tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOS.add(server);
        return tagDTOS;
    }
    public static String isValid(String inputTag){
        String[] inputTags = inputTag.split(",");
        List<TagDTO> tagDTOS = TagCache.getCategory();
        List<String> tags = tagDTOS.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        String inValid = Arrays.stream(inputTags).filter(tag -> !tags.contains(tag)).collect(Collectors.joining(","));
        return inValid;
    }
}
