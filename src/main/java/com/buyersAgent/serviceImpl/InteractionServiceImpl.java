package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.*;
import com.buyersAgent.service.CustomerActionService;
import com.buyersAgent.service.InteractionService;
import com.buyersAgent.service.PathService;
import com.buyersAgent.service.QuestionService;
import com.buyersAgent.strategy.PathExecutionStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InteractionServiceImpl implements InteractionService {

    private static final Logger logger = LogManager.getLogger(InteractionServiceImpl.class);
    private final Map<Long,Interaction> interactionMap = new HashMap<>();
    private long lastInteractionId=0;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PathService pathService;

    @Autowired
    private CustomerActionService customerActionService;

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
        logger.debug("New Interaction with Id {} started! ",interaction.getInteractionId());

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

        for(AnswerBean answerBean:interactionUpdate.getAnswerBeanList()){
            interaction.setLastHistoryId(interaction.getLastHistoryId() + 1);
            InteractionHistory interactionHistory = new InteractionHistory();
            interactionHistory.setInteractionId(interactionUpdate.getInteractionId());
            interactionHistory.setQuestionId(answerBean.getQuestionId());
            interactionHistory.setPathId(interactionUpdate.getPathId());
            interactionHistory.setAnswer(answerBean.getAnswer());
            interactionHistory.setHistoryId(interaction.getLastHistoryId());

            interaction.getInteractionHistoryList().add(interactionHistory);
        }

        //Check if the current-step ending-strategy would tell the next Path
        if(loadNextPathFromStrategy(currentPath,interaction)){
            return interaction;
        }

        if(currentPath.getNextPathId() != 0){//The path has ended, but next path exist!
            interaction.setCurrentPathId(currentPath.getNextPathId());
            currentPath = pathService.getPath(currentPath.getNextPathId());
            interaction.setCurrentQuestionId(currentPath.getQuestionIdList().get(0));
        }

        //Check by executing the Skip-Logic of the next loaded path - to see if
        //it directs to a different path.
        loadSkipPathFromStrategy(currentPath,interaction);

        return interaction;
    }

    //Executed at the start of a PATH - to see if we need to skip the path. If negative - the
    private boolean loadSkipPathFromStrategy(Path nextPath, Interaction interaction){
        if(nextPath.getPathStrategyProgram() == null || nextPath.getPathStrategyProgram().isEmpty()){
            return false;//No Strategy based flow!
        }

        String programName = nextPath.getPathStrategyProgram();
        logger.info("loadSkipPathFromStrategy - Path strategy program : {} ",programName);
        PathExecutionStrategy pathExecutionStrategy = createObject(programName);
        pathExecutionStrategy.initProgram(nextPath.getPathStrategyProgramInitData());
        pathExecutionStrategy.loadPreviousAnswers(interaction.getInteractionHistoryList());


        //Check if the Strategy suggest skipping the whole path - As part of loading itself!
        long nextNextPathId = pathExecutionStrategy.skipCurrentPathTo();
        if(nextNextPathId != -1){
            interaction.setCurrentPathId(nextNextPathId);
            nextPath = pathService.getPath(nextNextPathId);
            interaction.setCurrentQuestionId(nextPath.getQuestionIdList().get(0));
        }
        else{
            return false;
        }

        return true;
    }

    //Executed at the end of a Path - to find the next Path
    private boolean loadNextPathFromStrategy(Path currentPath, Interaction interaction){
        if(currentPath.getPathStrategyProgram() == null || currentPath.getPathStrategyProgram().isEmpty()){
            return false;//No Strategy based flow!
        }

        String programName = currentPath.getPathStrategyProgram();
        logger.info("loadNextPathFromStrategy - Path strategy program : {} ",programName);
        PathExecutionStrategy pathExecutionStrategy = createObject(programName);
        pathExecutionStrategy.initProgram(currentPath.getPathStrategyProgramInitData());
        pathExecutionStrategy.loadPreviousAnswers(interaction.getInteractionHistoryList());


        //Check if the Strategy gives the next path - As part of loading itself!
        long nextPath = pathExecutionStrategy.getNexPath();
        if(nextPath!= -1){
            interaction.setCurrentPathId(nextPath);
            currentPath = pathService.getPath(nextPath);
        }
        else{
            return false;
        }
        interaction.setCurrentQuestionId(currentPath.getQuestionIdList().get(0));

        return true;
    }

    private PathExecutionStrategy createObject(String whichClass) {
        try {
            Class<?> clazz = Class.forName(whichClass);
            return (PathExecutionStrategy) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error(e);
        }

        return null;
    }

    @Override
    public List<CustomerActionMatchStatus> getCustomerActions(long interactionId) {
        return customerActionService.getPossibleActions(interactionMap.get(interactionId));
    }
}
