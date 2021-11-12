package com.buyersAgent.model;

public class CustomerActionCriteria {

    private CustomerActionCriteriaPk customerActionCriteriaPk;
    private String acceptableAnswer;

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
}
