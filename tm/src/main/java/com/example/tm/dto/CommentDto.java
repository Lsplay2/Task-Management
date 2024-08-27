package com.example.tm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Long creater; //creator_id
    private Long task; //task_id
}
