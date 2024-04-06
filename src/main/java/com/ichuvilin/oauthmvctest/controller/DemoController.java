package com.ichuvilin.oauthmvctest.controller;

import com.ichuvilin.oauthmvctest.dto.GitlabEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DemoController {

    public static final String URL = "https://gitlab.com/api/v4/users/%s/events?access_token=%s";
    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/gitlab-statistic")
    public String gitlabStatistic(@RequestParam(required = false) String user) {
        var dataForProcessing = getDataForProcessing(user);

        var forEntity = restTemplate
                .getForEntity(URL.formatted(dataForProcessing.get("username"), dataForProcessing.get("token")), GitlabEvent[].class);

        Arrays.stream(forEntity.getBody()).forEach(
                event -> log.info("{}", event)
        );

        return "gitlab-statistic";
    }


    private Map<String, String> getDataForProcessing(String user) {
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();

        String username = Optional.ofNullable(user).orElse(principalName);

        var token = authorizedClientService.loadAuthorizedClient("gitlab", principalName).getAccessToken().getTokenValue();
        return Map.of("username", username, "token", token);
    }
}
