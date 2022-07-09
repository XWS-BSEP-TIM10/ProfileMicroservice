package com.profile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.profile.dto.AuthSagaResponseDTO;
import com.profile.dto.NewUserDto;
import com.profile.dto.UpdateUserDto;
import com.profile.model.User;
import com.profile.saga.dto.OrchestratorResponseDTO;
import com.profile.saga.dto.UpdateUserDTO;
import com.profile.saga.dto.UpdateUserStatusDTO;
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
            String natsURI = System.getenv("NATS_URI") == null ? "localhost" : System.getenv("NATS_URI");
            if (natsURI.equals("localhost")) {
                nats = Nats.connect();
            } else {
                nats = Nats.connect(natsURI);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void subscribeToCreateUser() {
        Dispatcher dispatcher = nats.createDispatcher(msg -> {
        });

        dispatcher.subscribe("nats.profile", msg -> {

            Gson gson = new Gson();
            String json = new String(msg.getData(), StandardCharsets.UTF_8);
            NewUserDto newUserDTO = gson.fromJson(json, NewUserDto.class);
            System.out.println(newUserDTO);

            User user = userService.create(new User(newUserDTO));
            AuthSagaResponseDTO responseDto;
            if (user == null)
                responseDto = new AuthSagaResponseDTO(false, "failed", newUserDTO.getUuid());
            else
                responseDto = new AuthSagaResponseDTO(user.getId(), true, "success", newUserDTO);

            publishResponseForCreateUser(responseDto);
        });
    }

    @PostConstruct
    public void subscribeToRevertCreatedUser() {
        Dispatcher dispatcher = nats.createDispatcher(msg -> {
        });

        dispatcher.subscribe("nats.profile.revert", msg -> {
            String userId = new String(msg.getData());
            userService.deleteById(userId);
        });
    }

    public void publishResponseForCreateUser(AuthSagaResponseDTO responseDto) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(responseDto);
        nats.publish("nats.demo.reply", json.getBytes());
    }

    public void publishUpdateUser(UpdateUserDTO requestDTO, User oldUser, String serviceChannel) {
        requestDTO.setOldUser(oldUser);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(requestDTO);
        nats.publish(serviceChannel, json.getBytes());
    }

    public void publishUpdateUserStatus(UpdateUserStatusDTO updateUserStatusDTO, String serviceChannel) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(updateUserStatusDTO);
        nats.publish(serviceChannel, json.getBytes());
    }

    @PostConstruct
    public void subscribeToUpdateUserResponse() {
        Dispatcher dispatcher = nats.createDispatcher(msg -> {
        });
        dispatcher.subscribe("nats.profile.reply", msg -> {

            Gson gson = new Gson();
            String json = new String(msg.getData(), StandardCharsets.UTF_8);
            OrchestratorResponseDTO responseDTO = gson.fromJson(json, OrchestratorResponseDTO.class);
            if (!responseDTO.isSuccess()) {
                userService.update(responseDTO.getOldUser());
            }
        });
    }
}
