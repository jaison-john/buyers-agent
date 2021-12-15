package com.buyersAgent.model;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private long pathId;
    private List<Long> questionIdList;
    private String pathCaption;
    private String pathStrategyProgram;
    private String pathStrategyProgramInitData;
    private String possibleNextPaths;
    private String possibleSkipToPaths;
    private long nextPathId;
    private List<Question> questionList;

    public Path(){
        questionIdList = new ArrayList<>();
        questionList = new ArrayList<>();
    }

    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public List<Long> getQuestionIdList() {
        return questionIdList;
    }

    public void setQuestionIdList(List<Long> questionIdList) {
        this.questionIdList = questionIdList;
    }

    public String getPathStrategyProgram() {
        return pathStrategyProgram;
    }

    public void setPathStrategyProgram(String pathStrategyProgram) {
        this.pathStrategyProgram = pathStrategyProgram;
    }

    public long getNextPathId() {
        return nextPathId;
    }

    public void setNextPathId(long nextPathId) {
        this.nextPathId = nextPathId;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public String getPathCaption() {
        return pathCaption;
    }

    public void setPathCaption(String pathCaption) {
        this.pathCaption = pathCaption;
    }

    public String getPossibleNextPaths() {
        return possibleNextPaths;
    }

    public void setPossibleNextPaths(String possibleNextPaths) {
        this.possibleNextPaths = possibleNextPaths;
    }

    public String getPossibleSkipToPaths() {
        return possibleSkipToPaths;
    }

    public void setPossibleSkipToPaths(String possibleSkipToPaths) {
        this.possibleSkipToPaths = possibleSkipToPaths;
    }

    public String getPathStrategyProgramInitData() {
        return pathStrategyProgramInitData;
    }

    public void setPathStrategyProgramInitData(String pathStrategyProgramInitData) {
        this.pathStrategyProgramInitData = pathStrategyProgramInitData;
    }
}
