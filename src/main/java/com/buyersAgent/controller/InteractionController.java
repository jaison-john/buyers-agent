package com.buyersAgent.controller;

import com.buyersAgent.model.*;
import com.buyersAgent.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/interaction")
public class InteractionController {

	@Autowired
	private InteractionService interactionService;

	@GetMapping(value = "/minimal/{id}", produces = "application/json")
	public Interaction getMinimalInteraction(@PathVariable("id") long id) {
		return interactionService.getInteractionMinimal(id);
	}

	@GetMapping(value = "/full/{id}", produces = "application/json")
	public Interaction getDetailedInteraction(@PathVariable("id") long id) {
		return interactionService.getInteractionDetail(id);
	}

	@GetMapping(value = "/actionIdsForInteraction/{id}", produces = "application/json")
	public List<CustomerActionMatchStatus> customerActionsOfInteraction(@PathVariable("id") long id) {
		return interactionService.getCustomerActions(id);
	}

	@PostMapping(value = "/createInteraction.json", produces = "application/json")
	public Interaction createNew(@RequestBody Long pathId) {
		return interactionService.createInteraction(pathId);
	}

	@PutMapping(value = "/updateInteraction")
	public Interaction updateInteractionWithAnswer(@RequestBody InteractionUpdate interactionUpdate) {
		return interactionService.updateInteractionByQuestion(interactionUpdate);
	}

	@GetMapping(value = "/listingsForInteraction/{id}", produces = "application/json")
	public List<MatchingListing> getMatchingListing(@PathVariable("id") long id) {
		return interactionService.getMatchingListings(id);
	}
}