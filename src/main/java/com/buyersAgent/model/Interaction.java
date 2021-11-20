package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class Interaction {
    private long interactionId;
    private long currentPathId;
    private long currentQuestionId;
    private List<InteractionHistory> interactionHistoryList;
    private long lastHistoryId;

    public Interaction(){
        interactionHistoryList = new ArrayList<>();
        lastHistoryId=0;
    }

    public long getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(long interactionId) {
        this.interactionId = interactionId;
    }

    public long getCurrentPathId() {
        return currentPathId;
    }

    public void setCurrentPathId(long currentPathId) {
        this.currentPathId = currentPathId;
    }

    public long getCurrentQuestionId() {
        return currentQuestionId;
    }

    public void setCurrentQuestionId(long currentQuestionId) {
        this.currentQuestionId = currentQuestionId;
    }

    public List<InteractionHistory> getInteractionHistoryList() {
        return interactionHistoryList;
    }

    public void setInteractionHistoryList(List<InteractionHistory> interactionHistoryList) {
        this.interactionHistoryList = interactionHistoryList;
    }

    public long getLastHistoryId() {
        return lastHistoryId;
    }

    public void setLastHistoryId(long lastHistoryId) {
        this.lastHistoryId = lastHistoryId;
    }
}
