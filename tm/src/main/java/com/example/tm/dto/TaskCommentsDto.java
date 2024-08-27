package com.example.tm.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TaskCommentsDto {
    private TaskDto taskDto;
    private List<CommentPostDto> commentPostDtos;
}
