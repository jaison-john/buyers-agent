package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.*;
import com.buyersAgent.service.InteractionService;
import com.buyersAgent.service.PathService;
import com.buyersAgent.service.QuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InteractionServiceImpl implements InteractionService {

    private static final Logger logger = LogManager.getLogger(InteractionServiceImpl.class);
    private Map<Long,Interaction> interactionMap = new HashMap<>();
    private long lastInteractionId=0;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PathService pathService;

    @Override
    public Interaction createInteraction(long pathId) {
        Interaction interaction = new Interaction();
        lastInteractionId++;
        interaction.setInteractionId(lastInteractionId);
        interaction.setCurrentPathId(pathId);
        interaction.setCurrentQuestionId(pathService.getPath(pathId).getQuestionIdList().get(0));
        interactionMap.put(lastInteractionId,interaction);

        return interaction;
    }

    @Override
    public Interaction getInteractionMinimal(long id) {
        Interaction interaction = interactionMap.get(id);

        //TODO
        //TODO - Clip out the History!

        return interaction;
    }

    @Override
    public Interaction getInteractionDetail(long id) {
        return interactionMap.get(id);
    }

    @Override
    public Interaction updateInteractionByQuestion(InteractionUpdate interactionUpdate) {
        //TODO - Check that path is the current running path of Interaction
        //TODO - check if question is present in Path!


        Interaction interaction = interactionMap.get(interactionUpdate.getInteractionId());
        Path currentPath = pathService.getPath(interaction.getCurrentPathId());

        interaction.setLastHistoryId(interaction.getLastHistoryId() + 1);
        InteractionHistory interactionHistory = new InteractionHistory();
        interactionHistory.setInteractionId(interactionUpdate.getInteractionId());
        interactionHistory.setQuestionId(interactionUpdate.getQuestionId());
        interactionHistory.setPathId(interactionUpdate.getPathId());
        interactionHistory.setAnswer(interactionUpdate.getAnswer());
        interactionHistory.setHistoryId(interaction.getLastHistoryId());

        interaction.getInteractionHistoryList().add(interactionHistory);

        Question nextQuestion = pathService.getNexQuestion(interactionUpdate.getPathId(),interactionUpdate.getQuestionId());

        if(nextQuestion != null)
            interaction.setCurrentQuestionId(nextQuestion.getQuestionId());
        else if(currentPath.getNextPathId() != 0){//The path has ended, but next path exist!
            interaction.setCurrentPathId(currentPath.getNextPathId());
            currentPath = pathService.getPath(currentPath.getNextPathId());
            interaction.setCurrentQuestionId(currentPath.getQuestionIdList().get(0));
        }
        else{
            //TODO - Build the Program-based logic!
        }


        return interaction;
    }
}
