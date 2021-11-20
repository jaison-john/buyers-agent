package com.buyersAgent.model;

public class CustomerActionCriteria {

    private CustomerActionCriteriaPk customerActionCriteriaPk;
    private long questionId;
    private String acceptableAnswer;
    private double acceptableAnswerMin;
    private double acceptableAnswerMax;

    public CustomerActionCriteria(){
        customerActionCriteriaPk = new CustomerActionCriteriaPk();
    }
    public CustomerActionCriteriaPk getCustomerActionCriteriaPk() {
        return customerActionCriteriaPk;
    }

    public void setCustomerActionCriteriaPk(CustomerActionCriteriaPk customerActionCriteriaPk) {
        this.customerActionCriteriaPk = customerActionCriteriaPk;
    }

    public String getAcceptableAnswer() {
        return acceptableAnswer;
    }

    public void setAcceptableAnswer(String acceptableAnswer) {
        this.acceptableAnswer = acceptableAnswer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public double getAcceptableAnswerMin() {
        return acceptableAnswerMin;
    }

    public void setAcceptableAnswerMin(double acceptableAnswerMin) {
        this.acceptableAnswerMin = acceptableAnswerMin;
    }

    public double getAcceptableAnswerMax() {
        return acceptableAnswerMax;
    }

    public void setAcceptableAnswerMax(double acceptableAnswerMax) {
        this.acceptableAnswerMax = acceptableAnswerMax;
    }
}
