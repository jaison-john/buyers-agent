package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class InteractionUpdate {
    private long interactionId;
    private long pathId;
    private List<AnswerBean> answerBeanList;

    public InteractionUpdate(){
        answerBeanList = new ArrayList<>();
    }

    public long getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(long interactionId) {
        this.interactionId = interactionId;
    }

    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public List<AnswerBean> getAnswerBeanList() {
        return answerBeanList;
    }

    public void setAnswerBeanList(List<AnswerBean> answerBeanList) {
        this.answerBeanList = answerBeanList;
    }

}
