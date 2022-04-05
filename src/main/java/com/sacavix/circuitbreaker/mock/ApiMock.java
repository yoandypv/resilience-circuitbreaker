package com.sacavix.circuitbreaker.mock;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiMock {
	
	public int mockPost(int num) {
		if(num%2 == 0) {
			log.error("ERROR, for num {}", num);
			throw new RuntimeException("Error calling post API");
		} 
		return num;
	}
	
	public int mockPostSlow(int num) {
		try {
			Thread.sleep(num*1000);
			return num;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
