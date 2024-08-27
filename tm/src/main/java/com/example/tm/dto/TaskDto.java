package com.example.tm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private String status;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("creater")
    Long creater; //creater_id
    @JsonProperty("executor")
    Long executor;//executor_id
}
