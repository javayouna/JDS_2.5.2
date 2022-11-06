package com.eoe.jds.controller;

import com.eoe.jds.entity.SiteUser;
import com.eoe.jds.service.QuestionService;
import com.eoe.jds.entity.Question;
import com.eoe.jds.form.AnswerForm;
import com.eoe.jds.form.QuestionForm;
import com.eoe.jds.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

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
    private final UserService userService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        /*URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값은 0으로 설정*/
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
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

    @PreAuthorize("isAuthenticated()")
    //질문 등록하기 버튼(create)을 누르면 question_form.html과 매핑
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal) {
        /*subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩*/
        /*@Valid => QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작*/
        /*BindingResult => @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체*/
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(),
                questionForm.getContent(), siteUser);
        return "redirect:/controller/list"; //질문 저장 후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,
                                 @PathVariable("id") Integer id,
                                 Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/controller/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/controller/detail/%s", id);
    }
}
