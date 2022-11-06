package com.eoe.jds.service;

import com.eoe.jds.DataNotFoundException;
import com.eoe.jds.entity.Answer;
import com.eoe.jds.entity.Question;
import com.eoe.jds.entity.SiteUser;
import com.eoe.jds.persistent.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = Answer.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .author(author)
                .build();
        this.answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if(answer.isPresent()) {
            return answer.get();
        }else {
            try {
                throw new DataNotFoundException("answer not found");
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void modify(Answer answer, String content) {
        answer.changeAnswerContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
