package com.profile;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ProfileMicroserviceApplication {

	public static void main(String[] args) {
		try {
			// connect to nats server
			Connection nats = Nats.connect();
			Dispatcher dispatcher = nats.createDispatcher(msg -> {
			});

			// subscribes to nats.demo.service channel
			dispatcher.subscribe("nats.demo.service", msg -> {
				System.out.println("Received : " + new String(msg.getData()));
				nats.publish(msg.getReplyTo(), "Hello Publisher from Profile Microservice!".getBytes());
			});
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// message dispatcher

		SpringApplication.run(ProfileMicroserviceApplication.class, args);
	}

}
