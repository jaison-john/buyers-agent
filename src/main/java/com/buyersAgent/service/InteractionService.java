package com.buyersAgent.service;

import com.buyersAgent.model.CustomerAction;
import com.buyersAgent.model.Interaction;
import com.buyersAgent.model.InteractionUpdate;

import java.util.List;

public interface InteractionService {
    Interaction createInteraction(long pathId);

    Interaction getInteractionMinimal(long interactionId);

    Interaction getInteractionDetail(long interactionId);

    Interaction updateInteractionByQuestion(InteractionUpdate interactionUpdate);

    List<CustomerAction> getCustomerActions(long interactionId);
}
