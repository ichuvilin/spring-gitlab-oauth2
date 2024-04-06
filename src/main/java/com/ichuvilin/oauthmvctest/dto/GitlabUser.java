package com.ichuvilin.oauthmvctest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitlabUser {
    private long id;
    private String username;
    private String name;
    private String state;
    private boolean locked;
    private String avatarUrl;
    private String webUrl;
}
