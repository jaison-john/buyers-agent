package com.buyersAgent.service;

import com.buyersAgent.model.CustomerActionMatchStatus;
import com.buyersAgent.model.Interaction;
import com.buyersAgent.model.InteractionUpdate;
import com.buyersAgent.model.MatchingListing;

import java.util.List;

public interface InteractionService {
    Interaction createInteraction(long pathId);

    Interaction getInteractionMinimal(long interactionId);

    Interaction getInteractionDetail(long interactionId);

    Interaction updateInteractionByQuestion(InteractionUpdate interactionUpdate);

    List<CustomerActionMatchStatus> getCustomerActions(long interactionId);

    List<MatchingListing> getMatchingListings(long interactionId);
}
