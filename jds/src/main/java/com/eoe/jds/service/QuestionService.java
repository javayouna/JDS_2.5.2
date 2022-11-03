package com.eoe.jds.service;

import com.eoe.jds.DataNotFoundException;
import com.eoe.jds.entity.Question;
import com.eoe.jds.persistent.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page) {
        /*게시물 역순 조회*/
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10);
        /*PageRequest.of(page, 10) => 조회할 페이지 번호, 한 페이지에 보여줄 게시물의 개수*/
        return this.questionRepository.findAll(pageable);
    }

    /*리포지터리로 얻은 Question 객체는 Optional 객체이기 때문에 위와 같이 isPresent 메서드로 해당 데이터가 존재하는지 검사 해야한다.*/
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            try {
                throw new DataNotFoundException("question not found");
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //제목과 내용을 입력하여 질문 데이터를 저장하는 create 메서드
    public void create(String subject, String content) {
        Question q = Question.builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();
        this.questionRepository.save(q);
    }

}
