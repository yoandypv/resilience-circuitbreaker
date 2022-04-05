package com.sacavix.circuitbreaker.service;

import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.sacavix.circuitbreaker.mock.ApiMock;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SampleService implements InitializingBean {

	private final ApiMock mock;
	private final CircuitBreaker circuitBreaker;

	public SampleService(ApiMock mock, CircuitBreaker circuitBreaker) {
		this.mock = mock;
		this.circuitBreaker = circuitBreaker;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Using circuitbreaker {}", this.circuitBreaker.getName());

	}

	public int runApiPostCall() {

		this.circuitBreaker.getEventPublisher().onSuccess(e -> {
			log.info("Event {}", e.getEventType());
		}).onError(e -> {
			log.error("Event {}, error {}", e.getEventType(), e.getThrowable().getMessage());
		}).onCallNotPermitted((e) -> {
			log.warn("Event {}, the circuit is OPEN ", e.getEventType());
		}).onStateTransition((e) -> {
			log.info("Event {} to {}", e.getEventType(), e.getStateTransition());
		});

		return Decorators
				.ofSupplier(() -> this.mock.mockPost(4)) // pass a Supplier 
				.withCircuitBreaker(this.circuitBreaker)  // set CB instance to use
				.withFallback(Arrays.asList(RuntimeException.class), this::fallBackRunApiPostCall) // fallback for exceptions
				.get(); // wait for response and get result or fallback

	}
	
	

	public int fallBackRunApiPostCall(Throwable err) {
		log.info("Fallback is called, with error {}", err);
		return 1;
	}
	


}
