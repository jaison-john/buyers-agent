package com.buyersAgent.controller;

import com.buyersAgent.model.Question;
import com.buyersAgent.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@GetMapping (value = "/{id}", produces = "application/json")
	public Question getQuestion(@PathVariable("id") long id) {
		return questionService.getQuestion(id);
	}

	@PostMapping
	public Question create(@RequestBody Question question) {
		questionService.putQuestion(question);
		return question;
	}

}