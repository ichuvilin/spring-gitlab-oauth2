package com.ichuvilin.oauthmvctest.controller;

import com.ichuvilin.oauthmvctest.dto.GitlabEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class GitLabRestController {
    public static final String URL = "https://gitlab.com/api/v4/users/%s/events?per_page=50&page=%s&access_token=%s";
    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/gitlab-statistic")
    public List<GitlabEvent> gitlabStatistic(@RequestParam(required = false) String user) {
        var dataForProcessing = getDataForProcessing(user);

        List<GitlabEvent> events = new LinkedList<>();
        int page = 1;

        while (true) {
            var forEntity = restTemplate
                    .getForEntity(URL.formatted(dataForProcessing.get("username"), page, dataForProcessing.get("token")), GitlabEvent[].class);
            if (forEntity.getBody() != null && forEntity.getBody().length == 0) {
                break;
            }
            events.addAll(Arrays.asList(forEntity.getBody()));
            page += 1;
        }

        log.info("list size: {}", events.size());

        return events;
    }


    private Map<String, String> getDataForProcessing(String user) {
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();

        String username = Optional.ofNullable(user).orElse(principalName);

        var token = authorizedClientService.loadAuthorizedClient("gitlab", principalName).getAccessToken().getTokenValue();
        return Map.of("username", username, "token", token);
    }
}
