package com.buyersAgent.service;

import com.buyersAgent.model.Path;
import com.buyersAgent.model.Question;

public interface PathService {
    Path getPath(long pathId);

    Question getNexQuestion(long pathId, long currentQuestionId);

}
