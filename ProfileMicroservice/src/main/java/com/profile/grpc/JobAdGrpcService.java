package com.profile.grpc;

import com.profile.model.JobAd;
import com.profile.model.Requirement;
import com.profile.model.User;
import com.profile.service.JobAdService;
import com.profile.service.RequirementService;
import com.profile.service.UserService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class JobAdGrpcService extends JobAdGrpcServiceGrpc.JobAdGrpcServiceImplBase{

    private final JobAdService jobAdService;
    private final UserService userService;
    private final RequirementService requirementService;

    public JobAdGrpcService(JobAdService jobAdService, UserService userService, RequirementService requirementService) {
        this.jobAdService = jobAdService;
        this.userService = userService;
        this.requirementService = requirementService;
    }

    @Override
    public void addNewJobAd(JobAdProto request, StreamObserver<JobAdResponseProto> responseObserver) {
        JobAdResponseProto responseProto;
        Optional<User> user = userService.findById(request.getUserId());
        if(user.isEmpty()) {
            responseProto = JobAdResponseProto.newBuilder().setStatus("Status 404").build();
        } else {
            JobAd jobAd = new JobAd(UUID.randomUUID().toString(),
                    user.get(), request.getTitle(), request.getPosition(),
                    request.getDescription(), request.getCompany());
            for(String req : request.getRequirementsList()) {
                Requirement newReq = new Requirement(req);
                if(!jobAd.getRequirements().contains(newReq)) {
                    Requirement savedReq = requirementService.addNewRequirement(newReq);
                    jobAd.getRequirements().add(savedReq);
                }
            }
            JobAd addedJobAd = jobAdService.save(jobAd);
            if (addedJobAd == null)
                responseProto = JobAdResponseProto.newBuilder().setStatus("Status 500").build();
            else
                responseProto = JobAdResponseProto.newBuilder().setStatus("Status 200")
                        .setId(addedJobAd.getId())
                        .setUserId(addedJobAd.getUser().getId())
                        .setTitle(addedJobAd.getTitle())
                        .setPosition(addedJobAd.getPosition())
                        .setDescription(addedJobAd.getDescription())
                        .setCompany(addedJobAd.getCompany())
                        .addAllRequirements(jobAd.getRequirements().stream().map(Requirement::getName).toList())
                        .build();
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserJobAds(GetJobAdsRequestProto request, StreamObserver<GetJobAdsResponseProto> responseObserver) {
        GetJobAdsResponseProto responseProto;
        Optional<User> user = userService.findById(request.getUserId());
        if(user.isEmpty())
            responseProto = GetJobAdsResponseProto.newBuilder().setStatus("Status 404").build();
        else {
            List<JobAd> jobAds = jobAdService.findByUser(user.get());
            List<UserJobAdProto> jobAdProtos = new ArrayList<>();
            for(JobAd jobAd : jobAds){
                UserJobAdProto jobAdProto = UserJobAdProto.newBuilder()
                        .setUserId(jobAd.getUser().getId())
                        .setFirstName(jobAd.getUser().getFirstName())
                        .setLastName(jobAd.getUser().getLastName())
                        .setTitle(jobAd.getTitle())
                        .setPosition(jobAd.getPosition())
                        .setDescription(jobAd.getDescription())
                        .setCompany(jobAd.getCompany())
                        .addAllRequirements(jobAd.getRequirements().stream().map(Requirement::getName).toList())
                        .build();
                jobAdProtos.add(jobAdProto);
            }

            responseProto = GetJobAdsResponseProto.newBuilder().addAllJobAds(jobAdProtos).setStatus("Status 200").build();
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }


}
