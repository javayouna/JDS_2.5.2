package com.eoe.jds.service;

import com.eoe.jds.entity.Answer;
import com.eoe.jds.entity.Question;
import com.eoe.jds.persistent.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content) {
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .build();
        this.answerRepository.save(answer);
    }
}
