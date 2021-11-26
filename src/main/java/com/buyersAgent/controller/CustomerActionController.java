package com.buyersAgent.controller;

import com.buyersAgent.model.CustomerAction;
import com.buyersAgent.service.CustomerActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/customerAction")
public class CustomerActionController {

	@Autowired
	private CustomerActionService customerActionService;

	@GetMapping (value = "/{id}", produces = "application/json")
	public CustomerAction getQuestion(@PathVariable("id") long id) {
		return customerActionService.getCustomerAction(id);
	}



}