package com.moviebooking.auth.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
	
	
	@KafkaListener(topics ="movietopic", groupId="movietopic_group")
	public void listenToTopic(String message) {
		System.out.println(message);
	}
}
