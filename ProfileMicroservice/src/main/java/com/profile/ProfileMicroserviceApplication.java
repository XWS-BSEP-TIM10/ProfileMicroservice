package com.profile;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class ProfileMicroserviceApplication {

	public static void main(String[] args) {
		try {
			// connect to nats server
			Connection nats = Nats.connect();
			Dispatcher dispatcher = nats.createDispatcher(msg -> {
			});

			// subscribes to nats.demo.service channel
			/*dispatcher.subscribe("nats.demo.service", msg -> {
				System.out.println("Received : " + new String(msg.getData()));
				nats.publish(msg.getReplyTo(), "Hello Publisher from Profile Microservice!".getBytes());
			});*/
			// subscribes to nats.demo.service channel
			AtomicReference<String> message = null;
			dispatcher.subscribe("nats.demo.service", msg -> {
				System.out.println("Received : " + new String(msg.getData()));
				nats.publish("nats.demo.reply", "Message received, Profile".getBytes());
			});



		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// message dispatcher

		SpringApplication.run(ProfileMicroserviceApplication.class, args);
	}

}
