package com.eoe.jds.controller;

import com.eoe.jds.service.AnswerService;
import com.eoe.jds.service.QuestionService;
import com.eoe.jds.entity.Question;
import com.eoe.jds.form.AnswerForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/controller")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model,
                               @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult) {
        /*@RequestParam String content => 템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가
          textarea의 name 속성명이 content이기 때문에 여기서도 변수명을 content로 사용해야 한다.
          만약 content 대신 다른 이름으로 사용하면 오류*/
        Question question = this.questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            /*검증에 실패할 경우 다시 답변을 등록할 수 있는 question_detail 템플릿을 렌더링*/
            return "question_detail";
        }
        this.answerService.create(question, answerForm.getContent());
        /*AnswerService의 create 메서드를 호출하여 답변을 저장할수 있다.*/
        return String.format("redirect:/controller/detail/%s", id);
    }
}
