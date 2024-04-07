package com.ichuvilin.oauthmvctest.controller;

import com.ichuvilin.oauthmvctest.service.GitLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("gitlab")
@RequiredArgsConstructor
public class GitLabController {
    private final GitLabService gitLabService;

    @GetMapping("stats")
    public String getStats(@RequestParam(name = "user", required = false) String user, Model model) {
        var gitlabEvents = gitLabService.getGitlabEvents(user);
        model.addAttribute("events", gitlabEvents);
        return "gitlab_events";
    }
}
