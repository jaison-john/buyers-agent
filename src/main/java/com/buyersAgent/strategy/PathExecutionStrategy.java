package com.buyersAgent.strategy;

import com.buyersAgent.model.InteractionHistory;

import java.util.ArrayList;
import java.util.List;

public interface PathExecutionStrategy {

    default void initProgram(String initData){
    }

    default public void loadPreviousAnswers(List<InteractionHistory> interactionHistoryList){
    }

    default long skipCurrentPathTo(){
        return -1;
    }

    default List<Long> hideQuestions(){
        return new ArrayList<Long>();
    }

    default long getNexPath(){
        return -1;
    }
}
