package com.ichuvilin.oauthmvctest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushData {
    private int commitCount;
    private String action;
    private String refType;
    private String commitFrom;
    private String commitTo;
    private String ref;
    private String commitTitle;
    private Integer refCount;
}
