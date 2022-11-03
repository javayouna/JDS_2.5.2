package com.eoe.jds.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class QuestionForm {
    /*@NotEmpty => 해당 값이 Null 또는 빈 문자열("")을 허용하지 않음
      검증이 실패할 경우 message 속성으로 오류 메시지 표시*/
    @NotEmpty(message="제목은 필수항목입니다.")
    /*@Size => max=200을 설정했기 때문에 최대 길이가 200 바이트를 넘으면 안됨*/
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
