package com.buyersAgent.model;

public class CustomerAction {

    private long actionId;
    private long actionTitle;
    private long actionDetail;
    private long actionCode;

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(long actionTitle) {
        this.actionTitle = actionTitle;
    }

    public long getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(long actionDetail) {
        this.actionDetail = actionDetail;
    }

    public long getActionCode() {
        return actionCode;
    }

    public void setActionCode(long actionCode) {
        this.actionCode = actionCode;
    }
}
