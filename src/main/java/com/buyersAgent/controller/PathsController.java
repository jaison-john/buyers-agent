package com.buyersAgent.controller;

import com.buyersAgent.model.Path;
import com.buyersAgent.service.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/path")
public class PathsController {

	@Autowired
	private PathService pathService;

	@GetMapping(value = "/{id}", produces = "application/json")
	public Path getPath(@PathVariable("id") long id) {
		return pathService.getPath(id);
	}

}