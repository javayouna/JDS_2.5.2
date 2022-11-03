package com.eoe.jds;

import com.eoe.jds.Service.QuestionService;
import com.eoe.jds.entity.Answer;
import com.eoe.jds.entity.Question;
import com.eoe.jds.persistent.AnswerRepository;
import com.eoe.jds.persistent.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdsApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	//데이터베이스 삽입 테스트
	//@Test
	void testJpa_insert() {
		Question q1 = Question.builder()
				.subject("QUESTION TEST1")
				.content("QUESTION TEST1")
				.createDate(LocalDateTime.now())
				.build();
		this.questionRepository.save(q1); //첫번째 질문 저장

		Question q2 = Question.builder()
				.subject("QUESTION TEST2")
				.content("QUESTION TEST2")
				.createDate(LocalDateTime.now())
				.build();
		this.questionRepository.save(q2); //두번째 질문 저장
	}

	//findAll 메서드를 사용한 데이터 조회(테이블의 모든 데이터를 조회한다.)
	//@Test
	void testJpa_select() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(1, all.size());
		/* assertEquals(기대값, 실제값)
		   디비에 저장된 데이터의 사이즈가 (1) all.size() (전체 사이즈) 인지에 대한 테스트*/

		Question q = all.get(0);
		assertEquals("QUESTION TEST1", q.getSubject());
		/* 디비에 저장된 첫번째 데이터의 (0) 제목이
			"DTO와 Entity의 차이를 아시는 분?" 와 일치하는지에 대한 테스트 */
	}

	//findById 메서드를 이용한 데이터 테이블 조회
	//@Test
	void testJpa_select2() {
		Optional<Question> oq = this.questionRepository.findById(2);
		/*findById의 리턴 타입은 Optional로, null을 유연하게 처리하기 위해 사용하는 클래스*/
		if(oq.isPresent()) {
			/*isPresent() => null인지 아닌지를 확인*/
			Question q = oq.get();
			assertEquals("QUESTION TEST2", q.getSubject());
		}
	}

	//findBySubject 이라는 메서드를 생성해서 데이터를 조회
	/*DI에 의해 스프링이 자동으로 QuestionRepository 객체를 생성,
	 리포지터리 객체의 메서드가 실행될때 JPA가 해당 메서드명을 분석하여 쿼리를 만들고 실행한다.
	 즉, findBy + 엔티티의 속성명(예:findBySubject)과 같은 리포지터리 메서드를 작성하면 해당 속성의 값으로 데이터를 조회 가능*/
	//@Test
	void testJpa_select3() {
		Question q = this.questionRepository.findBySubject("QUESTION TEST2");
		assertEquals(2, q.getId());
	}

	/*리포지토리에 findBySubjectAndContent 메서드를 선언 후 두 개의 데이터 값을 조회 */
	//@Test
	void testJpa_select4() {
		Question q = this.questionRepository.findBySubjectAndContent(
				"QUESTION TEST2", "QUESTION TEST2");
		assertEquals(2, q.getId());
	}

	/*특정 단어가 포함된 데이터값 조회를 위한 테스트*/
	//@Test
	void testJpa_select5() {
		List<Question> qList = this.questionRepository.findBySubjectLike("QUESTION%");
		Question q = qList.get(0);
		assertEquals("QUESTION TEST2", q.getSubject());
	}

	/*데이터 수정 테스트*/
	//@Test
	void testJpa_update() {
		Optional<Question> oq = this.questionRepository.findById(2);
		//assertTrue(값) => 값이 true인지 테스트
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.changeSubject("QUESTION UPDATE TEST");
		this.questionRepository.save(q);
	}

	/*데이터 삭제 테스트*/
	//@Test
	void testJpa_delete() {
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		/*리포지터리의 count() 메서드는 해당 리포지터리의 총 데이터건수를 리턴한다*/
		assertEquals(1, this.questionRepository.count());
	}

	/*답변 데이터 생성을 위한 테스트*/
	//@Test
	void testJpa_Answer_insert() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question question = oq.get();
		/*질문 데이터를 먼저 구해야하기 때문에 id가 2인 질문 데이터를 가져옴*/

		Answer answer = Answer.builder()
				.content("ANSWER TEST")
				.question(question)
				/*Answer 엔티티의 question 속성에 가져온 질문 데이터를 대입해 답변 데이터 생성*/
				.createDate(LocalDateTime.now())
				.build();
		this.answerRepository.save(answer);
	}

	/*답변 데이터 조회를 위한 테스트*/
	//@Test
	void testJpa_Answer_select () {
		Optional<Answer> oa = this.answerRepository.findById(1);
		/*id의 값이 1인 답변을 조회*/
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
		/*그 답변의 질문 id가 2인지 테스트*/
	}

	/*질문에 달린 답변을 찾기 위한 테스트*/
	@Transactional
	/*@Transactional => 테스트에서 DB 세션이 끊어져 오류가 나는 것을 막기 위한 애너테이션*/
	//@Test
	void testJpa_Answer_select2() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("ANSWER TEST", answerList.get(0).getContent());

	}

	/*페이징 구현을 위한 테스트 데이터 300개 삽입*/
	//@Test
	void testJpa_sampleData_insert() {
		for(int i=1; i<=300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "test";
			this.questionService.create(subject, content);
		}
	}


}
