package com.buyersAgent.service;

import com.buyersAgent.model.Question;

public interface QuestionService {
    public Question getQuestion(long id);

    public boolean putQuestion(Question question);
}
