package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class CustomerAction {

    private long actionId;
    private String actionTitle;
    private String actionDetail;
    private String actionCode;
    private List<CustomerActionCriteria> customerActionCriteriaList = new ArrayList<>();

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public List<CustomerActionCriteria> getCustomerActionCriteriaList() {
        return customerActionCriteriaList;
    }

    public void setCustomerActionCriteriaList(List<CustomerActionCriteria> customerActionCriteriaList) {
        this.customerActionCriteriaList = customerActionCriteriaList;
    }
}
