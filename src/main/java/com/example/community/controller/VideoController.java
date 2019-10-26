package com.example.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VideoController {
    @GetMapping("/videos")
    public String videos() {
        return "videos";
    }
    @GetMapping("/video")
    public String video(@RequestParam("aid") Integer aid,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        Model model) {
        model.addAttribute("aid",aid);
        model.addAttribute("page",page);
        return "video";
    }
}
