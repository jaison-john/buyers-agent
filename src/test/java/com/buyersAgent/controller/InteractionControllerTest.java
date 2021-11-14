package com.buyersAgent.controller;

import com.buyersAgent.BuyersAgentMainApplicationTest;
import com.buyersAgent.model.InteractionUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InteractionControllerTest extends BuyersAgentMainApplicationTest {

    private static final Logger logger = LogManager.getLogger(InteractionControllerTest.class);
    private ObjectWriter ow;
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void testInteractionCreate() throws Exception {

        Long pathId = 11L;

        InteractionUpdate interactionUpdate = createNewInteraction(pathId);
        interactionUpdate.setAnswer("Abracadabra! ");

        interactionUpdate = addAnswerToInteraction(interactionUpdate,"11","1002");
        interactionUpdate.setAnswer("XXYY! ");

        interactionUpdate = addAnswerToInteraction(interactionUpdate,"12","1003");
        interactionUpdate.setAnswer("ZZZ! ");

        interactionUpdate = addAnswerToInteraction(interactionUpdate,"12","1004");

        printInteractionDetails(interactionUpdate.getInteractionId());
    }

    private InteractionUpdate createNewInteraction(Long pathId) throws Exception {

        String requestJson=ow.writeValueAsString(pathId);

        logger.debug("PATH ID IS " + requestJson);

        ResultActions resultActions = mockMvc.perform(post("/interaction/createInteraction").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.interactionId").value("1"))
                .andExpect(jsonPath("$.currentPathId").value("11"))
                .andExpect(jsonPath("$.currentQuestionId").value("1001"));

        MvcResult mvcResult  = resultActions.andReturn();
        return getInteractionUpdateFromResults(mvcResult);
    }

    private InteractionUpdate addAnswerToInteraction(InteractionUpdate interactionUpdate,
                                                     String expectedNewPathId,String expectedNewQuestionId) throws Exception {

        String requestJson=ow.writeValueAsString(interactionUpdate);

        ResultActions resultActions = mockMvc.perform(put("/interaction/updateInteraction").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.interactionId").value("1"))
                .andExpect(jsonPath("$.currentPathId").value(expectedNewPathId))
                .andExpect(jsonPath("$.currentQuestionId").value(expectedNewQuestionId));

        MvcResult mvcResult  = resultActions.andReturn();
        return getInteractionUpdateFromResults(mvcResult);
    }

    private void printInteractionDetails(Long interactionId) throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/interaction/full/"+ interactionId).contentType(MediaType.APPLICATION_JSON));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        MvcResult mvcResult  = resultActions.andReturn();
        logger.debug("Interaction is: " + mvcResult.getResponse().getContentAsString());
    }

    private InteractionUpdate getInteractionUpdateFromResults(MvcResult mvcResult) throws UnsupportedEncodingException {
        DocumentContext docCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        Integer interactionId =docCtx.read(JsonPath.compile("$.interactionId"));
        Integer pathId = docCtx.read(JsonPath.compile("$.currentPathId"));
        Integer questionId =docCtx.read(JsonPath.compile("$.currentQuestionId"));
        logger.debug("interactionId: " + interactionId);
        logger.debug("pathId: " + pathId);
        logger.debug("questionId: " + questionId);

        InteractionUpdate interactionUpdate = new InteractionUpdate();
        interactionUpdate.setInteractionId(interactionId);
        interactionUpdate.setPathId(pathId);
        interactionUpdate.setQuestionId(questionId);

        return interactionUpdate;
    }
}
