package com.ichuvilin.oauthmvctest.controller;

import com.ichuvilin.oauthmvctest.dto.GitlabEvent;
import com.ichuvilin.oauthmvctest.service.GitLabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class GitLabRestController {
    private final GitLabService gitLabService;

    @GetMapping("/gitlab-statistic")
    public List<GitlabEvent> gitlabStatistic(@RequestParam(required = false) String user) {
        var gitlabEvents = gitLabService.getGitlabEvents(user);

        log.info("list size: {}", gitlabEvents.size());

        return gitlabEvents;
    }
}
