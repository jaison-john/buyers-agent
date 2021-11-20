package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.Question;
import com.buyersAgent.service.QuestionService;
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
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LogManager.getLogger(QuestionServiceImpl.class);
    private Map<Long,Question> questionMap = new HashMap<>();

    @PostConstruct
    private void loadQuestions() {
        logger.debug("Loading Questions... ");

        JSONParser parser = new JSONParser();
        try {
            Resource resource = new ClassPathResource("Questions.json");
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(resource.getInputStream()));

            for (Object o : array)
            {
                Question questionObj = new Question();
                JSONObject question = (JSONObject) o;
                questionObj.setQuestionId((Long) question.get("questionId"));

                questionObj.setQuestionDetails((String) question.get("questionDetails"));

                questionObj.setQuestionType(Question.QuestionType.valueOf((String) question.get("questionType")));

                JSONArray answers = (JSONArray) question.get("answers");

                if(answers != null){
                    for (Object c : answers) {
                        questionObj.getAnswers().add((String)c);
                    }
                }

                questionMap.put(questionObj.getQuestionId(),questionObj);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logger.debug("Loaded {} Questions.. ",questionMap.size());

        return;
    }

    @Override
    public Question getQuestion(long id) {
        return questionMap.get(id);
    }

    @Override
    public boolean putQuestion(Question question) {
        questionMap.put(question.getQuestionId(),question);
        return true;
    }
}
