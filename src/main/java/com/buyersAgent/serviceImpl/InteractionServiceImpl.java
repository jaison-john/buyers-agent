package com.buyersAgent.serviceImpl;

import com.buyersAgent.model.*;
import com.buyersAgent.service.CustomerActionService;
import com.buyersAgent.service.InteractionService;
import com.buyersAgent.service.PathService;
import com.buyersAgent.service.QuestionService;
import com.buyersAgent.strategy.PathExecutionStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ListingsApi;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.joda.time.format.DateTimeFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InteractionServiceImpl implements InteractionService {

    private static final Logger logger = LogManager.getLogger(InteractionServiceImpl.class);
    private final Map<Long,Interaction> interactionMap = new HashMap<>();
    private long lastInteractionId=0;
    private final DateTimeFormatter inputFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PathService pathService;

    @Autowired
    private CustomerActionService customerActionService;

    @Override
    public Interaction createInteraction(long pathId) {
        Interaction interaction = new Interaction();
        lastInteractionId++;
        interaction.setInteractionId(lastInteractionId);
        interaction.setCurrentPathId(pathId);
        interaction.setCurrentQuestionId(pathService.getPath(pathId).getQuestionIdList().get(0));
        interactionMap.put(lastInteractionId,interaction);

        return interaction;
    }

    @Override
    public Interaction getInteractionMinimal(long id) {
        Interaction interaction = interactionMap.get(id);

        //TODO
        //TODO - Clip out the History!
        logger.debug("New Interaction with Id {} started! ",interaction.getInteractionId());

        return interaction;
    }

    @Override
    public Interaction getInteractionDetail(long id) {
        return interactionMap.get(id);
    }

    @Override
    public Interaction updateInteractionByQuestion(InteractionUpdate interactionUpdate) {
        //TODO - Check that path is the current running path of Interaction
        //TODO - check if question is present in Path!


        Interaction interaction = interactionMap.get(interactionUpdate.getInteractionId());
        Path currentPath = pathService.getPath(interaction.getCurrentPathId());

        for(AnswerBean answerBean:interactionUpdate.getAnswerBeanList()){
            interaction.setLastHistoryId(interaction.getLastHistoryId() + 1);
            InteractionHistory interactionHistory = new InteractionHistory();
            interactionHistory.setInteractionId(interactionUpdate.getInteractionId());
            interactionHistory.setQuestionId(answerBean.getQuestionId());
            interactionHistory.setPathId(interactionUpdate.getPathId());
            interactionHistory.setAnswer(answerBean.getAnswer());
            interactionHistory.setHistoryId(interaction.getLastHistoryId());

            interaction.getInteractionHistoryList().add(interactionHistory);
        }

        //Check if the current-step ending-strategy would tell the next Path
        if(loadNextPathFromStrategy(currentPath,interaction)){
            return interaction;
        }

        if(currentPath.getNextPathId() != 0){//The path has ended, but next path exist!
            interaction.setCurrentPathId(currentPath.getNextPathId());
            currentPath = pathService.getPath(currentPath.getNextPathId());
            interaction.setCurrentQuestionId(currentPath.getQuestionIdList().get(0));
        }

        //Check by executing the Skip-Logic of the next loaded path - to see if
        //it directs to a different path.
        loadSkipPathFromStrategy(currentPath,interaction);

        return interaction;
    }

    //Executed at the start of a PATH - to see if we need to skip the path. If negative - the
    private boolean loadSkipPathFromStrategy(Path nextPath, Interaction interaction){
        if(nextPath.getPathStrategyProgram() == null || nextPath.getPathStrategyProgram().isEmpty()){
            return false;//No Strategy based flow!
        }

        String programName = nextPath.getPathStrategyProgram();
        logger.info("loadSkipPathFromStrategy - Path strategy program : {} ",programName);
        PathExecutionStrategy pathExecutionStrategy = createObject(programName);
        pathExecutionStrategy.initProgram(nextPath.getPathStrategyProgramInitData());
        pathExecutionStrategy.loadPreviousAnswers(interaction.getInteractionHistoryList());


        //Check if the Strategy suggest skipping the whole path - As part of loading itself!
        long nextNextPathId = pathExecutionStrategy.skipCurrentPathTo();
        if(nextNextPathId != -1){
            interaction.setCurrentPathId(nextNextPathId);
            nextPath = pathService.getPath(nextNextPathId);
            interaction.setCurrentQuestionId(nextPath.getQuestionIdList().get(0));
        }
        else{
            return false;
        }

        return true;
    }

    //Executed at the end of a Path - to find the next Path
    private boolean loadNextPathFromStrategy(Path currentPath, Interaction interaction){
        if(currentPath.getPathStrategyProgram() == null || currentPath.getPathStrategyProgram().isEmpty()){
            return false;//No Strategy based flow!
        }

        String programName = currentPath.getPathStrategyProgram();
        logger.info("loadNextPathFromStrategy - Path strategy program : {} ",programName);
        PathExecutionStrategy pathExecutionStrategy = createObject(programName);
        pathExecutionStrategy.initProgram(currentPath.getPathStrategyProgramInitData());
        pathExecutionStrategy.loadPreviousAnswers(interaction.getInteractionHistoryList());


        //Check if the Strategy gives the next path - As part of loading itself!
        long nextPath = pathExecutionStrategy.getNexPath();
        if(nextPath!= -1){
            interaction.setCurrentPathId(nextPath);
            currentPath = pathService.getPath(nextPath);
        }
        else{
            return false;
        }
        interaction.setCurrentQuestionId(currentPath.getQuestionIdList().get(0));

        return true;
    }

    private PathExecutionStrategy createObject(String whichClass) {
        try {
            Class<?> clazz = Class.forName(whichClass);
            return (PathExecutionStrategy) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error(e);
        }

        return null;
    }

    @Override
    public List<CustomerActionMatchStatus> getCustomerActions(long interactionId) {
        return customerActionService.getPossibleActions(interactionMap.get(interactionId));
    }


    @Override
    public List<MatchingListing> getMatchingListings(long interactionId) {
        return getListingDetails(interactionId);
    }

    private List<MatchingListing> getListingDetails(long interactionId){
        Interaction interaction = interactionMap.get(interactionId);

        String suburbPostCode = "2762";
        for(InteractionHistory history: interaction.getInteractionHistoryList()){
            if(history.getQuestionId() == 1009) {//Suburb Question
                suburbPostCode = history.getAnswer();
            }
        }
        List<MatchingListing> matchingListings = new ArrayList<>();
        ApiClient apiClient= createApiClient();
        ListingsApi listingsApi = new ListingsApi();
        listingsApi.setApiClient(apiClient);

        /*
        ListingsV1Listing listingsV1Listing = listingsApi.listingsGet(2017412596);
        logger.error("ListingsV1Listing " + listingsV1Listing.toString());
         */
        DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters domainSearchParameters = new DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchParameters();
        DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation locationsItem = new DomainSearchServiceV2ModelDomainSearchWebApiV2ModelsSearchLocation();
        locationsItem.setPostCode(suburbPostCode);
        domainSearchParameters.addLocationsItem(locationsItem);
        ResponseEntity<List<DomainSearchServiceV2ModelDomainSearchContractsV2SearchResult>> responseEntity =
                listingsApi.listingsDetailedResidentialSearchWithHttpInfo(domainSearchParameters);

        logger.error("DomainSearchServiceV2ModelDomainSearchContractsV2SearchResult " + responseEntity.getBody().toString());

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            for(DomainSearchServiceV2ModelDomainSearchContractsV2SearchResult result : responseEntity.getBody()){

                if(result.getListing() != null){
                    DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing domainListing = result.getListing();
                    buildMatchingListing(matchingListings,domainListing);
                }

                if(result.getListings() != null){
                    List<DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing> domainListings = result.getListings();
                    for(DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing domainListing:domainListings){
                        buildMatchingListing(matchingListings,domainListing);
                    }
                }

            }
        }
        
        return matchingListings;
    }

    private void buildMatchingListing(List<MatchingListing> matchingListings,DomainSearchServiceV2ModelDomainSearchContractsV2PropertyListing domainListing){
        MatchingListing matchingListing = new MatchingListing();
        matchingListing.setListingId(domainListing.getId().toString());
        if(domainListing.getSummaryDescription().startsWith("<b></b><br />") == false)
            matchingListing.setDescription(domainListing.getSummaryDescription());
        else
            matchingListing.setDescription(domainListing.getSummaryDescription().substring(13));

        matchingListing.setHeadline(domainListing.getHeadline());
        if(domainListing.getInspectionSchedule() != null && domainListing.getInspectionSchedule().getTimes()!=null){
            for(DomainSearchServiceV2ModelDomainSearchContractsV2Inspection inspectionTime : domainListing.getInspectionSchedule().getTimes()){
                AppointmentTime appointmentTime = new AppointmentTime();
                //appointmentTime.setStartDateTime(inspectionTime.getOpeningTime().toString(inputFormat));
                appointmentTime.setStartDateTime(inputFormatter.print(inspectionTime.getOpeningTime()));
                appointmentTime.setEndDateTime(inputFormatter.print(inspectionTime.getClosingTime()));
                matchingListing.getAppointmentTimeList().add(appointmentTime);
            }
        }

        matchingListings.add(matchingListing);
    }

    private ApiClient createApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("https://api.domain.com.au/");
        apiClient.setApiKey("key_6a7a40bcbcd676098ce9280256e98b45");
        return apiClient;
    }
}
