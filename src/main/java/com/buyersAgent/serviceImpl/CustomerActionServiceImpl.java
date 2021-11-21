package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.*;
import com.buyersAgent.service.CustomerActionService;
import com.buyersAgent.service.QuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerActionServiceImpl implements CustomerActionService {

    private static final Logger logger = LogManager.getLogger(CustomerActionServiceImpl.class);
    private Map<Long, CustomerAction> customerActionMap = new HashMap<>();

    @Autowired
    private QuestionService questionService;

    @PostConstruct
    private void loadCustomerActions() {
        logger.debug("Loading CustomerActions... ");

        JSONParser parser = new JSONParser();
        try {
            Resource resource = new ClassPathResource("data/CustomerActions.json");
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(resource.getInputStream()));

            for (Object o : array)
            {
                CustomerAction customerActionObj = new CustomerAction();
                JSONObject customerAction = (JSONObject) o;
                customerActionObj.setActionId((Long) customerAction.get("actionId"));
                customerActionObj.setActionDetail((String) customerAction.get("actionDetail"));
                customerActionObj.setActionCode((String) customerAction.get("actionCode"));

                JSONArray customerActionCriteriaList = (JSONArray) customerAction.get("customerActionCriteriaList");

                if(customerActionCriteriaList != null){
                    for (Object c : customerActionCriteriaList) {
                        JSONObject customerActionCriteriaJson = (JSONObject)c;
                        CustomerActionCriteria customerActionCriteria = new CustomerActionCriteria();

                        customerActionCriteria.getCustomerActionCriteriaPk().setActionId((Long) customerActionCriteriaJson.get("actionId"));
                        customerActionCriteria.getCustomerActionCriteriaPk().setCriteriaId((Long) customerActionCriteriaJson.get("criteriaId"));
                        customerActionCriteria.setQuestionId((Long) customerActionCriteriaJson.get("questionId"));
                        if(customerActionCriteriaJson.get("acceptableAnswer") != null)
                            customerActionCriteria.setAcceptableAnswer((String) customerActionCriteriaJson.get("acceptableAnswer"));
                        if(customerActionCriteriaJson.get("acceptableAnswerMin") != null)
                            customerActionCriteria.setAcceptableAnswerMin((Long) customerActionCriteriaJson.get("acceptableAnswerMin"));
                        if(customerActionCriteriaJson.get("acceptableAnswerMax") != null)
                            customerActionCriteria.setAcceptableAnswerMax((Long) customerActionCriteriaJson.get("acceptableAnswerMax"));

                        customerActionObj.getCustomerActionCriteriaList().add(customerActionCriteria);
                    }
                }

                customerActionMap.put(customerActionObj.getActionId(),customerActionObj);
            }
        } catch (IOException|ParseException e) {
            logger.error("Failed parsing",e);
        }

        logger.debug("Loaded {} CustomerActions.. ",customerActionMap.size());

    }

    @Override
    public CustomerAction getCustomerAction(long id) {
        return customerActionMap.get(id);
    }

    @Override
    public List<CustomerActionMatchStatus> getPossibleActions(Interaction interaction) {
        List<CustomerActionMatchStatus> customerActionIdList = new ArrayList<>();

        customerActionMap.forEach((customerActionId, customerAction) -> {

            CustomerActionMatchStatus customerActionMatchStatus = new CustomerActionMatchStatus();
            customerActionMatchStatus.setCustomerActionId(customerActionId);
            customerActionMatchStatus.setCriteriaCount(customerAction.getCustomerActionCriteriaList().size());

            for(CustomerActionCriteria customerActionCriteria : customerAction.getCustomerActionCriteriaList()){

                InteractionHistory interactionHistory = getQuestionIfExistInInteraction(interaction , customerActionCriteria.getQuestionId());
                if(interactionHistory != null){
                    customerActionMatchStatus.setFoundQuestionCount(customerActionMatchStatus.getFoundQuestionCount() + 1);

                    boolean isMatch = checkIfAnswerMatchesCriteria(interactionHistory,customerActionCriteria);

                    if(isMatch){
                        customerActionMatchStatus.setFoundAnswerMatchCount(customerActionMatchStatus.getFoundAnswerMatchCount() + 1);
                    }
                }
            }

            if(customerActionMatchStatus.getFoundAnswerMatchCount() > 0)
                customerActionIdList.add(customerActionMatchStatus);
        });

        return customerActionIdList;
    }

    private InteractionHistory getQuestionIfExistInInteraction(Interaction interaction, long questionId){
        for(InteractionHistory interactionHistory : interaction.getInteractionHistoryList()){

            if(interactionHistory.getQuestionId() == questionId){
                return interactionHistory;
            }

        }

        return null;
    }

    private boolean checkIfAnswerMatchesCriteria(InteractionHistory interactionHistory, CustomerActionCriteria customerActionCriteria){

        Question question = questionService.getQuestion(interactionHistory.getQuestionId());
        //Scenario where acceptable answer is controlled as options..
        if(question.getAnswers().size() > 0){
            if(customerActionCriteria.getAcceptableAnswer().equals(interactionHistory.getAnswer()))
                return true;
            else
                return false;
        }

        if(question.getQuestionType().equals(Question.QuestionType.INTEGER) || question.getQuestionType().equals(Question.QuestionType.FLOAT)){
            double answer = Double.parseDouble(interactionHistory.getAnswer());

            if(answer >= customerActionCriteria.getAcceptableAnswerMin() && answer <= customerActionCriteria.getAcceptableAnswerMax())
                return true;
            else
                return false;
        }

        if(question.getQuestionType().equals(Question.QuestionType.TEXT)){
            ;

            if(interactionHistory.getAnswer().compareToIgnoreCase(customerActionCriteria.getAcceptableAnswer()) == 0)
                return true;
            else
                return false;
        }

        return false;
    }
}
