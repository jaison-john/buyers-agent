package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class Question {

    public enum QuestionType
    {
        OPTIONS, INTEGER, FLOAT,ATTACH,TEXT,RANK;
    }

    private long questionId;
    private String questionDetails;
    private QuestionType questionType;
    private List<String> answers;

    public Question(){
        answers = new ArrayList<>();
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
    
}
