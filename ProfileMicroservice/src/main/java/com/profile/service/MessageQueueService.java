package com.profile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.profile.dto.AuthSagaResponseDTO;
import com.profile.dto.NewUserDTO;
import com.profile.model.User;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MessageQueueService {

    private Connection nats;

    private final UserService userService;

    public MessageQueueService(UserService userService) {
        this.userService = userService;
        try {
            this.nats = Nats.connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void subscribe() {
        Dispatcher dispatcher = nats.createDispatcher(msg -> {
        });

        dispatcher.subscribe("nats.demo.service", msg -> {

            Gson gson = new Gson();
            String json = new String(msg.getData(), StandardCharsets.UTF_8);
            NewUserDTO newUserDTO = gson.fromJson(json, NewUserDTO.class);
            System.out.println(newUserDTO);

            User user = userService.create(new User(newUserDTO));
            AuthSagaResponseDTO responseDto;
            if (user == null)
                responseDto = new AuthSagaResponseDTO(false, "failed");
            else
                responseDto = new AuthSagaResponseDTO(user.getId(), true, "sucess");

            publish(responseDto);
        });
    }

    public void publish(AuthSagaResponseDTO responseDto) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(responseDto);
        nats.publish("nats.demo.reply", json.getBytes());
    }
}
