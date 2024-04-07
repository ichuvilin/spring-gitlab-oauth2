package com.ichuvilin.oauthmvctest.service;

import com.ichuvilin.oauthmvctest.dto.GitlabEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitLabService {
    public static final String URL = "https://gitlab.com/api/v4/users/%s/events?per_page=50&page=%s&access_token=%s";
    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public List<GitlabEvent> getGitlabEvents(String user) {
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
        return events;
    }

    private Map<String, String> getDataForProcessing(String user) {
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();

        String username = Optional.ofNullable(user).orElse(principalName);

        var token = authorizedClientService.loadAuthorizedClient("gitlab", principalName).getAccessToken().getTokenValue();
        return Map.of("username", username, "token", token);
    }
}
