package com.eoe.jds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerDTO {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private QuestionDTO question;
}
