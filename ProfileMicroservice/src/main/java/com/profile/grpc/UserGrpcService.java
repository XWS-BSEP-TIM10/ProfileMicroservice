package com.profile.grpc;

import com.profile.dto.NewUserDTO;
import com.profile.exception.UserNotFoundException;
import com.profile.exception.UsernameAlreadyExists;
import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.service.LoggerService;
import com.profile.service.UserService;
import com.profile.service.impl.LoggerServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import proto.EmailProto;
import proto.EmailResponseProto;
import proto.ExperienceProto;
import proto.FindUserProto;
import proto.FindUserResponseProto;
import proto.IdProto;
import proto.IdResponseProto;
import proto.InterestProto;
import proto.UpdateUserProto;
import proto.UpdateUserResponseProto;
import proto.UserGrpcServiceGrpc;
import proto.UserIdProto;
import proto.UserNamesProto;
import proto.UserNamesResponseProto;
import proto.UserProto;
import proto.UserResponseProto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService
public class UserGrpcService extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String OK_STATUS = "Status 200";
    private static final String BAD_REQUEST_STATUS = "Status 400";
    private static final String NOT_FOUND_STATUS = "Status 404";
    private static final String CONFLICT_STATUS = "Status 409";
    private final UserService service;
    private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat(DATE_FORMAT);
    private final LoggerService loggerService;


    @Autowired
    public UserGrpcService(UserService userService) {
        this.service = userService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @Override
    public void update(UpdateUserProto request, StreamObserver<UpdateUserResponseProto> responseObserver) {
        UpdateUserResponseProto responseProto;

        try {
            NewUserDTO dto = new NewUserDTO(request.getUuid(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getGender(), request.getDateOfBirth(), request.getUsername(), request.getBiography(), request.getProfilePublic());
            User user = new User(dto);
            user.setDateOfBirth(new SimpleDateFormat(DATE_FORMAT).parse(dto.getDateOfBirth()));
            service.updateUser(user);
            responseProto = UpdateUserResponseProto.newBuilder().setStatus(OK_STATUS).setSuccess(true).setMessage("success").build();
            loggerService.updateUser(request.getUuid());
        } catch (ParseException e) {
            responseProto = UpdateUserResponseProto.newBuilder().setStatus(BAD_REQUEST_STATUS).build();
            loggerService.updateUserBadDate(request.getUuid());
        } catch (UserNotFoundException e) {
            responseProto = UpdateUserResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
            loggerService.updateUserNotFound(request.getUuid());
        } catch (UsernameAlreadyExists e) {
            responseProto = UpdateUserResponseProto.newBuilder().setStatus(CONFLICT_STATUS).build();
            loggerService.updateUserUsernameAlreadyExists(request.getUuid(), request.getUsername());
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void find(FindUserProto request, StreamObserver<FindUserResponseProto> responseObserver) {
        FindUserResponseProto responseProto;

        List<User> users = service.findByFirstNameAndLastName(request.getFirstName(), request.getLastName());
        List<UserProto> protoUsers = new ArrayList<>();

        for (User user : users) {
            List<ExperienceProto> experiences = getExperiences(user);
            List<InterestProto> interests = getInterests(user);
            UserProto newUser = UserProto.newBuilder().setUuid(user.getId()).setFirstName(user.getFirstName())
                    .setLastName(user.getLastName()).setEmail(user.getEmail())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setGender(user.getGender().toString())
                    .setDateOfBirth(user.getDateOfBirth() == null ? "": iso8601Formatter.format(user.getDateOfBirth()))
                    .setUsername(user.getUsername()).setBiography(user.getBiography())
                    .addAllExperiences(experiences).addAllInterests(interests)
                    .setProfilePublic(user.isPublicProfile())
                    .build();
            protoUsers.add(newUser);
        }

        responseProto = FindUserResponseProto.newBuilder().addAllUsers(protoUsers).setStatus(OK_STATUS).build();
        loggerService.findUsers(request.getFirstName(), request.getLastName());
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    private List<ExperienceProto> getExperiences(User user) {
        List<ExperienceProto> experiences = new ArrayList<>();
        for (Experience experience : user.getExperiences()) {
            experiences.add(ExperienceProto.newBuilder()
                    .setId(experience.getId()).setInstitution(experience.getInstitution())
                    .setPosition(experience.getPosition()).setFromDate(iso8601Formatter.format(experience.getFromDate()))
                    .setToDate(experience.getToDate() == null ? "Present" : iso8601Formatter.format(experience.getToDate()))
                    .setDescription(experience.getDescription()).
                    setType(experience.getType().toString()).build());
        }
        return experiences;
    }

    private List<InterestProto> getInterests(User user) {
        List<InterestProto> interests = new ArrayList<>();
        for (Interest interest : user.getInterests()) {
            interests.add(InterestProto.newBuilder().setId(interest.getId()).setDescription(interest.getDescription()).build());
        }
        return interests;
    }

    @Override
    public void getFirstAndLastName(UserNamesProto request, StreamObserver<UserNamesResponseProto> responseObserver) {

        Optional<User> user = service.findById(request.getId());
        UserNamesResponseProto responseProto;
        if (user.isEmpty()) {
            responseProto = UserNamesResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
            loggerService.getFirstAndLastNameFailed(request.getId());
        } else {
            responseProto = UserNamesResponseProto.newBuilder().setStatus(OK_STATUS)
                    .setFirstName(user.get().getFirstName()).setLastName(user.get().getLastName()).build();
            loggerService.getFirstAndLastName(user.get().getEmail());
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void getEmail(EmailProto request, StreamObserver<EmailResponseProto> responseObserver) {

        Optional<User> user = service.findById(request.getId());
        EmailResponseProto responseProto;

        if (user.isEmpty()) {
            responseProto = EmailResponseProto.newBuilder().setEmail("").setStatus(NOT_FOUND_STATUS).build();
            loggerService.getEmailFailed(request.getId());
        } else {
            responseProto = EmailResponseProto.newBuilder().setEmail(user.get().getEmail()).setStatus(OK_STATUS).build();
            loggerService.getEmail(user.get().getEmail());
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getId(IdProto request, StreamObserver<IdResponseProto> responseObserver) {

        IdResponseProto responseProto;
        try {
            String id = service.findIdByEmail(request.getEmail());
            responseProto = IdResponseProto.newBuilder().setId(id).setStatus(OK_STATUS).build();
            loggerService.getId(request.getEmail());
        } catch (NullPointerException ex) {
            responseProto = IdResponseProto.newBuilder().setId("").setStatus("Status 400").build();
            loggerService.getIdFailed(request.getEmail());
        }

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void getById(UserIdProto request, StreamObserver<UserResponseProto> responseObserver) {
        Optional<User> user = service.findById(request.getId());
        UserResponseProto responseProto;

        if (user.isPresent()) {
            List<ExperienceProto> experiences = getExperiences(user.get());
            List<InterestProto> interests = getInterests(user.get());
            UserProto userProto = UserProto.newBuilder().setUuid(user.get().getId())
                    .setFirstName(user.get().getFirstName())
                    .setLastName(user.get().getLastName()).setEmail(user.get().getEmail())
                    .setPhoneNumber(user.get().getPhoneNumber())
                    .setGender(user.get().getGender().toString())
                    .setDateOfBirth(user.get().getDateOfBirth()==null?"":iso8601Formatter.format(user.get().getDateOfBirth()))
                    .setUsername(user.get().getUsername()).setBiography(user.get().getBiography())
                    .addAllExperiences(experiences).addAllInterests(interests)
                    .setProfilePublic(user.get().isPublicProfile())
                    .build();
            responseProto = UserResponseProto.newBuilder().setUser(userProto).setStatus(OK_STATUS).build();
            loggerService.getUserById(user.get().getEmail());
        } else {
            responseProto = UserResponseProto.newBuilder().setStatus(NOT_FOUND_STATUS).build();
            loggerService.getUserById(request.getId());
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }


}
