package com.buyersAgent.strategy;

import com.buyersAgent.model.InteractionHistory;

import java.util.List;

public class SkipToQuestionIfAnsMatch implements PathExecutionStrategy{
    private long questionId;
    private String expectedAns;
    private long skipToId;

    private String partnerAns;

    @Override
    public void loadPreviousAnswers(List<InteractionHistory> interactionHistoryList){

        for(InteractionHistory interactionHistory : interactionHistoryList){
            if(interactionHistory.getQuestionId() == questionId){
                partnerAns = interactionHistory.getAnswer();
                break;
            }
        }
    }

    @Override
    public long skipCurrentPathTo(){
        if(expectedAns.equals(partnerAns))
            return skipToId;
        else
            return -1;
    }

    @Override
    public void initProgram(String initData){

        String[] initVariables = initData.split(" ");
        questionId = Long.parseLong(initVariables[0]);
        expectedAns = initVariables[1];
        skipToId = Long.parseLong(initVariables[2]);
    }

}
