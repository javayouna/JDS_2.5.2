package com.eoe.jds.controller;

import com.eoe.jds.Service.QuestionService;
import com.eoe.jds.entity.Question;
import com.eoe.jds.persistent.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/controller")
/*@RequiredArgsConstructor는 롬복이 제공하는 애너테이션으로
final이 붙은 속성을 포함하는 생성자를 자동으로 생성*/
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

}
