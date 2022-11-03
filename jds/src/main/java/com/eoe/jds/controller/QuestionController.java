package com.eoe.jds.controller;

import com.eoe.jds.service.QuestionService;
import com.eoe.jds.entity.Question;
import com.eoe.jds.form.AnswerForm;
import com.eoe.jds.form.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*list 메서드의 URL 매핑은 /list 이지만 클래스에 /question이라는 URL 매핑이 있기 때문에
/controller + /list가 되어 최종적인 URL 매핑은 /controller/list가 된다.*/
@RequestMapping("/controller")
/*@RequiredArgsConstructor는 롬복이 제공하는 애너테이션으로
final이 붙은 속성을 포함하는 생성자를 자동으로 생성*/
@RequiredArgsConstructor
@Controller
public class QuestionController {

    /*Controller -> Service -> Repository 구조로 데이터를 처리하기 위해 변경*/
    private final QuestionService questionService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        /*URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값은 0으로 설정*/
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        /*요청 URL http://localhost:8080/controller/detail/2의 숫자 2처럼 변하는 id 값을
        얻을 때에는 위와 같이 @PathVariable 애너테이션을 사용해야 한다.
        이 때 @RequestMapping(value = "/controller/detail/{id}") 에서 사용한 id와
        @PathVariable("id")의 매개변수 이름이 동일해야 한다.*/
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    //질문 등록하기 버튼(create)을 누르면 question_form.html과 매핑
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        /*subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩*/
        /*@Valid => QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작*/
        /*BindingResult => @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체*/
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/controller/list"; //질문 저장 후 질문목록으로 이동
    }

}
