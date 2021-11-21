package com.buyersAgent.model;

public class CustomerActionMatchStatus {
    long customerActionId;
    long criteriaCount;
    long foundQuestionCount;
    long foundAnswerMatchCount;

    public long getCustomerActionId() {
        return customerActionId;
    }

    public void setCustomerActionId(long customerActionId) {
        this.customerActionId = customerActionId;
    }

    public long getCriteriaCount() {
        return criteriaCount;
    }

    public void setCriteriaCount(long criteriaCount) {
        this.criteriaCount = criteriaCount;
    }

    public long getFoundQuestionCount() {
        return foundQuestionCount;
    }

    public void setFoundQuestionCount(long foundQuestionCount) {
        this.foundQuestionCount = foundQuestionCount;
    }

    public long getFoundAnswerMatchCount() {
        return foundAnswerMatchCount;
    }

    public void setFoundAnswerMatchCount(long foundAnswerMatchCount) {
        this.foundAnswerMatchCount = foundAnswerMatchCount;
    }
}
