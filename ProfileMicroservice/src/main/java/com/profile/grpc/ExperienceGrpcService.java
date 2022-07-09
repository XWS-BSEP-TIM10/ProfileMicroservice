package com.profile.grpc;


import com.profile.dto.NewExperienceDTO;
import com.profile.exception.UserNotFoundException;
import com.profile.model.Event;
import com.profile.model.Experience;
import com.profile.service.EventService;
import com.profile.service.ExperienceService;
import com.profile.service.LoggerService;
import com.profile.service.UserService;
import com.profile.service.impl.LoggerServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import proto.ExperienceGrpcServiceGrpc;
import proto.NewExperienceProto;
import proto.NewExperienceResponseProto;
import proto.RemoveExperienceProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceProto;
import proto.UpdateExperienceResponseProto;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@GrpcService
public class ExperienceGrpcService extends ExperienceGrpcServiceGrpc.ExperienceGrpcServiceImplBase {

    private final ExperienceService service;
    private final LoggerService loggerService;
    private final UserService userService;
    private final EventService eventService;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String OK_STATUS = "Status 200";
    private static final String NOT_FOUND_STATUS = "Status 404";

    @Autowired
    public ExperienceGrpcService(ExperienceService experienceService, UserService userService, EventService eventService) {
        this.service = experienceService;
        this.eventService = eventService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
        this.userService = userService;
    }

    @Override
    public void add(NewExperienceProto request, StreamObserver<NewExperienceResponseProto> responseObserver) {
        NewExperienceResponseProto responseProto;
        try {
            NewExperienceDTO dto = new NewExperienceDTO(request.getUserId(), request.getInstitution(), request.getPosition(), request.getFromDate(), request.getToDate(), request.getDescription(), request.getType());
            Experience newExperience = new Experience(dto);
            newExperience.setFromDate(new SimpleDateFormat(DATE_FORMAT).parse(dto.getFromDate()));
            if (dto.getToDate().equals(""))
                newExperience.setToDate(null);
            else
                newExperience.setToDate(new SimpleDateFormat(DATE_FORMAT).parse(dto.getToDate()));
            Experience addedExperience = service.add(dto.getUserId(), newExperience);
            if (addedExperience == null) {
                responseProto = NewExperienceResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
                loggerService.addExperienceUserNotFound(dto.getUserId());
            } else {
                eventService.save(new Event("User successfully added experience. Username: " + userService.findById(request.getUserId()).get().getUsername()));
                responseProto = NewExperienceResponseProto.newBuilder().setStatus(OK_STATUS).setId(addedExperience.getId()).setPosition(addedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(addedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();

                loggerService.addExperience(userService.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new).getEmail());
            }
        } catch (ParseException e) {
            responseProto = NewExperienceResponseProto.newBuilder().setStatus("Status 400").build();
            loggerService.addExperienceBadDate(request.getUserId());
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void update(UpdateExperienceProto request, StreamObserver<UpdateExperienceResponseProto> responseObserver) {
        UpdateExperienceResponseProto responseProto;

        try {
            NewExperienceDTO dto = new NewExperienceDTO(request.getUserId(), request.getInstitution(), request.getPosition(), request.getFromDate(), request.getToDate(), request.getDescription(), request.getType());
            Experience newExperience = new Experience(dto);
            newExperience.setFromDate(new SimpleDateFormat(DATE_FORMAT).parse(dto.getFromDate()));
            if (dto.getToDate().equals(""))
                newExperience.setToDate(null);
            else
                newExperience.setToDate(new SimpleDateFormat(DATE_FORMAT).parse(dto.getToDate()));
            Experience updatedExperience = service.update(request.getId(), newExperience);
            if (updatedExperience == null) {
                responseProto = UpdateExperienceResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
                loggerService.updateExperienceNotFound(String.valueOf(request.getId()));
            } else {
                eventService.save(new Event("User successfully updated experience. Username: " + userService.findById(request.getUserId()).get().getUsername()));
                responseProto = UpdateExperienceResponseProto.newBuilder().setStatus(OK_STATUS).setId(updatedExperience.getId()).setPosition(updatedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(updatedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();
                loggerService.updateExperience(String.valueOf(request.getId()), request.getUserId());
            }
        } catch (ParseException e) {
            responseProto = UpdateExperienceResponseProto.newBuilder().setStatus("Status 400").build();
            loggerService.addExperienceBadDate(request.getUserId());
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void remove(RemoveExperienceProto request, StreamObserver<RemoveExperienceResponseProto> responseObserver) {
        RemoveExperienceResponseProto responseProto;

        boolean success = service.remove(request.getId());
        if (!success) {
            responseProto = RemoveExperienceResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
            loggerService.deleteExperienceNotFound(String.valueOf(request.getId()));
        } else {
            eventService.save(new Event("User successfully removed experience. Experience: " + request.getId()));
            responseProto = RemoveExperienceResponseProto.newBuilder().setStatus(OK_STATUS).build();
            loggerService.deleteExperience(String.valueOf(request.getId()));
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

}
