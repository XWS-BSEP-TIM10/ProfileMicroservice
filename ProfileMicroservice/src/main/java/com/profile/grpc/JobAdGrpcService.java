package com.profile.grpc;

import com.profile.model.Event;
import com.profile.model.JobAd;
import com.profile.model.Requirement;
import com.profile.model.User;
import com.profile.service.*;
import com.profile.service.impl.LoggerServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.GetJobAdsRequestProto;
import proto.GetJobAdsResponseProto;
import proto.JobAdGrpcServiceGrpc;
import proto.JobAdProto;
import proto.JobAdResponseProto;
import proto.SearchJobAdsProto;
import proto.UserJobAdProto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class JobAdGrpcService extends JobAdGrpcServiceGrpc.JobAdGrpcServiceImplBase {

    private final JobAdService jobAdService;
    private final UserService userService;
    private final RequirementService requirementService;
    private final EventService eventService;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final LoggerService loggerService;

    private static final String OK_STATUS = "Status 200";

    public JobAdGrpcService(JobAdService jobAdService, UserService userService, RequirementService requirementService, EventService eventService) {
        this.jobAdService = jobAdService;
        this.userService = userService;
        this.requirementService = requirementService;
        this.eventService = eventService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @Override
    public void addNewJobAd(JobAdProto request, StreamObserver<JobAdResponseProto> responseObserver) {
        JobAdResponseProto responseProto;
        Optional<User> user = userService.findById(request.getUserId());
        if (user.isEmpty()) {
            responseProto = JobAdResponseProto.newBuilder().setStatus("Status 404").build();
            loggerService.addJobAdUserNotFound(request.getUserId());
        } else {
            JobAd jobAd = new JobAd(UUID.randomUUID().toString(),
                    user.get(), request.getTitle(), request.getPosition(),
                    request.getDescription(), new Date(), request.getCompany());
            for (String req : request.getRequirementsList()) {
                Requirement newReq = new Requirement(req);
                if (!jobAd.getRequirements().contains(newReq)) {
                    Requirement savedReq = requirementService.addNewRequirement(newReq);
                    jobAd.getRequirements().add(savedReq);
                }
            }
            JobAd addedJobAd = jobAdService.save(jobAd);
            if (addedJobAd == null) {
                responseProto = JobAdResponseProto.newBuilder().setStatus("Status 500").build();
                loggerService.addJobAdFailed(user.get().getEmail());
            } else {
                eventService.save(new Event("User successfully added job ad. Username: " + userService.findById(request.getUserId()).get().getUsername()));
                responseProto = JobAdResponseProto.newBuilder().setStatus(OK_STATUS)
                        .setId(addedJobAd.getId())
                        .setUserId(addedJobAd.getUser().getId())
                        .setTitle(addedJobAd.getTitle())
                        .setPosition(addedJobAd.getPosition())
                        .setDescription(addedJobAd.getDescription())
                        .setCreationDate(dateFormatter.format(addedJobAd.getCreationDate()))
                        .setCompany(addedJobAd.getCompany())
                        .addAllRequirements(jobAd.getRequirements().stream().map(Requirement::getName).toList())
                        .build();
                loggerService.addJobAd(addedJobAd.getId(), user.get().getEmail());
            }
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserJobAds(GetJobAdsRequestProto request, StreamObserver<GetJobAdsResponseProto> responseObserver) {
        GetJobAdsResponseProto responseProto;
        Optional<User> user = userService.findById(request.getUserId());
        if (user.isEmpty()) {
            responseProto = GetJobAdsResponseProto.newBuilder().setStatus("Status 404").build();
            loggerService.getUserJobAdsFailed(request.getUserId());
        } else {
            List<JobAd> jobAds = jobAdService.findByUser(user.get());
            List<UserJobAdProto> jobAdProtos = getJobAdProtos(jobAds);
            responseProto = GetJobAdsResponseProto.newBuilder().addAllJobAds(jobAdProtos).setStatus(OK_STATUS).build();
            loggerService.getUserJobAds(user.get().getEmail());
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getJobAds(SearchJobAdsProto request, StreamObserver<GetJobAdsResponseProto> responseObserver) {
        List<JobAd> jobAds = jobAdService.searchByPosition(request.getSearchParam());
        List<UserJobAdProto> jobAdProtos = getJobAdProtos(jobAds);
        GetJobAdsResponseProto responseProto = GetJobAdsResponseProto.newBuilder().addAllJobAds(jobAdProtos).setStatus(OK_STATUS).build();
        loggerService.getJobAdsByPosition(request.getSearchParam());
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    private List<UserJobAdProto> getJobAdProtos(List<JobAd> jobAds) {
        List<UserJobAdProto> jobAdProtos = new ArrayList<>();
        for (JobAd jobAd : jobAds) {
            UserJobAdProto jobAdProto = UserJobAdProto.newBuilder()
                    .setUserId(jobAd.getUser().getId())
                    .setFirstName(jobAd.getUser().getFirstName())
                    .setLastName(jobAd.getUser().getLastName())
                    .setTitle(jobAd.getTitle())
                    .setPosition(jobAd.getPosition())
                    .setDescription(jobAd.getDescription())
                    .setCreationDate(dateFormatter.format(jobAd.getCreationDate()))
                    .setCompany(jobAd.getCompany())
                    .addAllRequirements(jobAd.getRequirements().stream().map(Requirement::getName).toList())
                    .build();
            jobAdProtos.add(jobAdProto);
        }
        return jobAdProtos;
    }
}
