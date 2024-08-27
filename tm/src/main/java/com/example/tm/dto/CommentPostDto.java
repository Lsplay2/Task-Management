package com.example.tm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentPostDto {
    private String content;
    private String creater; //creator_id

}
