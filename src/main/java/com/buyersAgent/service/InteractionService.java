package com.buyersAgent.service;

import com.buyersAgent.model.Interaction;
import com.buyersAgent.model.InteractionUpdate;

public interface InteractionService {
    Interaction createInteraction(long pathId);

    Interaction getInteractionMinimal(long interactionId);

    Interaction getInteractionDetail(long interactionId);

    Interaction updateInteractionByQuestion(InteractionUpdate interactionUpdate);
}
