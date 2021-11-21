package com.buyersAgent.service;

import com.buyersAgent.model.CustomerAction;
import com.buyersAgent.model.CustomerActionMatchStatus;
import com.buyersAgent.model.Interaction;

import java.util.List;

public interface CustomerActionService {
    CustomerAction getCustomerAction(long id);

    List<CustomerActionMatchStatus> getPossibleActions(Interaction interaction);
}
