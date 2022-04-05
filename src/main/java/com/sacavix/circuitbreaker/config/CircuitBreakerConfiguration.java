package com.sacavix.circuitbreaker.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.core.IntervalFunction;

@Configuration
public class CircuitBreakerConfiguration {

	@Bean
	public CircuitBreaker circuitBreaker() {
		return CircuitBreaker.of("mi-primer-cb", this.buildConfig4());

	}

	CircuitBreakerConfig buildConfig1() {
		return CircuitBreakerConfig.custom().slidingWindowType(SlidingWindowType.COUNT_BASED).slidingWindowSize(4) // default
																													// 100
				.failureRateThreshold(50f) // default 100%
				.permittedNumberOfCallsInHalfOpenState(2) // default 10
				.waitDurationInOpenState(Duration.ofSeconds(5)) // default 60s (fixed)
				.writableStackTraceEnabled(false).build();
	}

	CircuitBreakerConfig buildConfig2() {
		return CircuitBreakerConfig.custom().slidingWindowType(SlidingWindowType.COUNT_BASED).slidingWindowSize(4) // default
																													// 100
				.failureRateThreshold(50f) // default 100%
				.permittedNumberOfCallsInHalfOpenState(2) // default 10
				.waitIntervalFunctionInOpenState(
						IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(2), 3, Duration.ofSeconds(100)))
				.automaticTransitionFromOpenToHalfOpenEnabled(true).writableStackTraceEnabled(false).build();
	}

	CircuitBreakerConfig buildConfig3() {

		return CircuitBreakerConfig.custom().slidingWindowType(SlidingWindowType.COUNT_BASED).slidingWindowSize(4) // default
																													// 100
				.failureRateThreshold(50f) // default 100%
				.permittedNumberOfCallsInHalfOpenState(2) // default 10
				.waitDurationInOpenState(Duration.ofSeconds(5)) // default 60s (fixed)
				.slowCallRateThreshold(20f) // 20% of calls are slow
				.slowCallDurationThreshold(Duration.ofSeconds(2)) // > 2s is slow
				.writableStackTraceEnabled(false).build();
	}

	CircuitBreakerConfig buildConfig4() {

		return CircuitBreakerConfig.custom().slidingWindowType(SlidingWindowType.TIME_BASED).slidingWindowSize(4) // default
																													// 100
				.failureRateThreshold(50f) // default 100%
				.permittedNumberOfCallsInHalfOpenState(2) // default 10
				.waitDurationInOpenState(Duration.ofSeconds(2)) // default 60s (fixed)
				.writableStackTraceEnabled(false).automaticTransitionFromOpenToHalfOpenEnabled(true)
				.minimumNumberOfCalls(4).build();
	}

}
