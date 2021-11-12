package com.buyersAgent.controller;

import com.buyersAgent.model.Question;
import com.buyersAgent.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Question getQuestion(@PathVariable("id") long id) {
		return questionService.getQuestion(id);
	}

	@PostMapping
	public Question create(@RequestBody Question question) {
		questionService.putQuestion(question);
		return question;
	}

}