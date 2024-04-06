package com.ichuvilin.oauthmvctest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitlabEvent {
    private long id;
    private long projectId;
    private String actionName;
    private Long targetId;
    private Long targetIid;
    private String targetType;
    private long authorId;
    private String targetTitle;
    private LocalDate createdAt;
    private GitlabUser author;
    private PushData pushData;
    private String authorUsername;
}
