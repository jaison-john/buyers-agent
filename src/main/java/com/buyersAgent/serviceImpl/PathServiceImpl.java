package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.Path;
import com.buyersAgent.model.Question;
import com.buyersAgent.service.PathService;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class PathServiceImpl implements PathService {

    private static final Logger logger = LogManager.getLogger(PathServiceImpl.class);
    private Map<Long,Path> pathMap = new HashMap<>();

    @Autowired
    private QuestionService questionService;

    @PostConstruct
    private void loadPaths() {
        logger.debug("Loading Paths... ");

        JSONParser parser = new JSONParser();
        try {
            Resource resource = new ClassPathResource("Paths.json");
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(resource.getInputStream()));

            for (Object o : array)
            {
                Path pathObj = new Path();
                JSONObject path = (JSONObject) o;
                pathObj.setPathId((Long) path.get("pathId"));
                pathObj.setNextPathId((Long) path.get("nextPathId"));
                pathObj.setPathStrategyProgram((String) path.get("pathStrategyProgram"));
                JSONArray questionIdList = (JSONArray) path.get("questionIdList");
                for (Object c : questionIdList) {
                    pathObj.getQuestionIdList().add((Long) c);
                }

                pathMap.put(pathObj.getPathId(),pathObj);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logger.debug("Loaded {} Paths.. ",pathMap.size());

    }


    @Override
    public Path getPath(long pathId) {
        Path path = pathMap.get(pathId);

        //Link Questions if not done earlier
        synchronized (path) {
            if (!path.getQuestionList().isEmpty())
                return path;

            for (Long questionId : path.getQuestionIdList()) {
                Question question = questionService.getQuestion(questionId);
                path.getQuestionList().add(question);
            }
        }
        return path;
    }

    @Override
    public Question getNexQuestion(long pathId, long currentQuestionId) {
        Path path = pathMap.get(pathId);

        boolean getNextQuestionId = false;
        for (Long questionId : path.getQuestionIdList()) {
            if(getNextQuestionId){
                return questionService.getQuestion(questionId);
            }
            if(currentQuestionId == questionId){
                getNextQuestionId = true;
            }
        }

        return null;//This is case where path has ended!
    }

}
