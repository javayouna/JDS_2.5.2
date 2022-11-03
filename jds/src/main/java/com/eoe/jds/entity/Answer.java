package com.eoe.jds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    //답변의 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //답변의 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    //답변을 작성한 일시
    private LocalDateTime createDate;

    //질문(어떤 질문의 답변인지에 대한 질문 속성)
    /*답변은 하나의 질문에 여러개가 달릴 수 있는 구조기 때문에
    * 답변 Many : 질문 One 관계에 따라서 @ManyToOne 애너테이션을 사용한다.*/
    @ManyToOne
    private Question question;


}
