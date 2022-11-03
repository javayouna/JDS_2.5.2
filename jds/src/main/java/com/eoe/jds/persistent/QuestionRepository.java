package com.eoe.jds.persistent;

import com.eoe.jds.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Repository를 이용해 테이블에 데이터를 저장하거나 조회할 수 있다.
/*엔티티의 타입(Question)과 해당 엔티티의 Primary Key 속성인 id의 타입은 Integer*/
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    //Question 엔티티의 subject 값으로 데이터를 조회하기 위한 메서드 생성
    Question findBySubject(String subject);
    //두 개의 속성을 And 조건으로 조회하기 위한 메서드 생성
    Question findBySubjectAndContent(String subject, String content);
    //특정 문자열이 포함되어 있는 데이터 조회를 위한 메서드 생성
    List<Question> findBySubjectLike(String subject);
    //Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드를 생성
    Page<Question> findAll(Pageable pageable);
}
