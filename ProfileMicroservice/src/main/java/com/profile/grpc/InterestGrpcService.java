package com.profile.grpc;

import org.springframework.beans.factory.annotation.Autowired;

import com.profile.dto.NewInterestDTO;
import com.profile.model.Interest;
import com.profile.service.InterestService;
import com.profile.service.LoggerService;
import com.profile.service.UserService;
import com.profile.service.impl.LoggerServiceImpl;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.InterestGrpcServiceGrpc;
import proto.NewInterestProto;
import proto.NewInterestResponseProto;
import proto.RemoveInterestProto;
import proto.RemoveInterestResponseProto;

@GrpcService
public class InterestGrpcService extends InterestGrpcServiceGrpc.InterestGrpcServiceImplBase{
	
private final InterestService service;
private final LoggerService loggerService;
private final UserService userService;
	
	@Autowired
	public InterestGrpcService(InterestService interestService, UserService userService) {
		this.service = interestService;
		this.loggerService = new LoggerServiceImpl(this.getClass());
		this.userService = userService;
	}
	
	@Override
	public void add(NewInterestProto request, StreamObserver<NewInterestResponseProto> responseObserver) {
			NewInterestResponseProto responseProto;
			
			NewInterestDTO dto= new NewInterestDTO(request.getUserId(),request.getDescription());
			Interest newInterest = new Interest(dto);
	        Interest addedInterest = service.add(dto.getUserId(), newInterest);
	        if(addedInterest == null)
	        {
	        	responseProto= NewInterestResponseProto.newBuilder().setStatus("Status 404").build();
	        	loggerService.addInterestUserNotFound(request.getUserId());
	        }
	        else
	        {
				responseProto= NewInterestResponseProto.newBuilder().setStatus("Status 200").setId(addedInterest.getId()).setDescription(addedInterest.getDescription()).build();
				loggerService.addInterest(String.valueOf(addedInterest.getId()),userService.findById(request.getUserId()).get().getEmail());
	        }
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}
	
	@Override
	public void remove(RemoveInterestProto request, StreamObserver<RemoveInterestResponseProto> responseObserver) {
			RemoveInterestResponseProto responseProto;
			
			 boolean success = service.removeInterest(request.getId(), request.getUserId());
			 if(!success)
			 {
				 responseProto= RemoveInterestResponseProto.newBuilder().setStatus("Status 404").build();
				 loggerService.removeInterestFailed(String.valueOf(request.getId()));
			 }
			 else
			 {
				 responseProto= RemoveInterestResponseProto.newBuilder().setStatus("Status 200").build();
				 loggerService.removeInterest(String.valueOf(request.getId()), userService.findById(request.getUserId()).get().getEmail());
			 }
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}

}
