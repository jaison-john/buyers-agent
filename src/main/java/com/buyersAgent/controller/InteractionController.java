package com.buyersAgent.controller;

import com.buyersAgent.model.CustomerAction;
import com.buyersAgent.model.Interaction;
import com.buyersAgent.model.InteractionUpdate;
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

	@GetMapping(value = "/customerActionsOfInteraction/{id}", produces = "application/json")
	public List<CustomerAction> customerActionsOfInteraction(@PathVariable("id") long id) {
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
}