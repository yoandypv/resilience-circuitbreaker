package com.sacavix.circuitbreaker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sacavix.circuitbreaker.service.SampleService;

@RestController
@RequestMapping("/sample")
public class SampleController {

	public final SampleService sampleService;
	
	public SampleController(SampleService sampleService) {
		this.sampleService = sampleService;
	}
	
	@GetMapping
	public int testCall() {
		return this.sampleService.runApiPostCall();
	}
	
}
