package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.CustomerAction;
import com.buyersAgent.model.CustomerActionCriteria;
import com.buyersAgent.model.Interaction;
import com.buyersAgent.service.CustomerActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerActionServiceImpl implements CustomerActionService {

    private static final Logger logger = LogManager.getLogger(CustomerActionServiceImpl.class);
    private Map<Long, CustomerAction> customerActionMap = new HashMap<>();

    @PostConstruct
    private void loadCustomerActions() {
        logger.debug("Loading CustomerActions... ");

        JSONParser parser = new JSONParser();
        try {
            Resource resource = new ClassPathResource("CustomerActions.json");
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
        return null;
    }

    @Override
    public List<CustomerAction> getPossibleActions(Interaction interaction) {
        return null;
    }
}
