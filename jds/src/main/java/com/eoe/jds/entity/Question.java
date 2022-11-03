package com.eoe.jds.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Question {
    //질문의 고유 번호
    /*@Id 애너테이션은 id 속성을 기본 키로 지정한다.*/
    @Id
    /*@GeneratedValue 애너테이션은 데이터를 저장할 때 해당 속성에 값을 따로 세팅하지 않아도
    1씩 자동 증가하여 저장된다. strategy 는 고유번호를 생서하는 옵션으로
    해당 컬럼만의 독립적인 시퀀스를 생성하여 번호를 증가시킬 때 사용한다.*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //질문의 제목
    /*@GeneratedValue 컬럼의 세부 설정을 위한 애너테이션*/
    @Column(length = 200)
    private String subject;

    //질문의 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    //질문을 작성한 일시
    private LocalDateTime createDate;

    //질문 엔티티에서 답변 엔티티를 참조할 때 사용할 속성
    /*Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달*/
    /*cascade = CascadeType.REMOVE
    => 질문 하나에는 여러개의 답변이 작성될 수 있는데, 이때 질문을 삭제하면 그에 달린
    답변들도 모두 함께 삭제하기 위해서 @OneToMany의 속성으로 사용*/
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    //데이터값 수정을 위한 메서드
    public void changeSubject(String subject) {
        this.subject = subject;
    }
}

