package com.buyersAgent.model;

public class InteractionHistory {
    private long interactionId;
    private long historyId;
    private long pathId;
    private long questionId;
    private String answer;

    public long getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(long interactionId) {
        this.interactionId = interactionId;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
