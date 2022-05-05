package com.profile.grpc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.profile.dto.NewUserDTO;
import com.profile.model.User;
import com.profile.saga.dto.OrchestratorResponseDTO;
import com.profile.service.UserService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.UpdateUserProto;
import proto.UpdateUserResponseProto;
import proto.UserGrpcServiceGrpc;

@GrpcService
public class UserGrpcService extends UserGrpcServiceGrpc.UserGrpcServiceImplBase{
	
	private final UserService service;
	
	@Autowired
	public UserGrpcService(UserService userService) {
		this.service = userService;
	}
	
	@Override
	public void update(UpdateUserProto request, StreamObserver<UpdateUserResponseProto> responseObserver) {
			UpdateUserResponseProto responseProto;
			
			try {
				NewUserDTO dto = new NewUserDTO(request.getUuid(),request.getFirstName(),request.getLastName(),request.getEmail(),request.getPhoneNumber(),request.getGender(),request.getDateOfBirth(),request.getUsername(),request.getPassword(),request.getBiography());
	            User user = new User(dto);
	            user.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
	            OrchestratorResponseDTO response = service.updateUser(user).block();
	            responseProto= UpdateUserResponseProto.newBuilder().setStatus("Status 200").setSuccess(response.isSuccess()).setMessage(response.getMessage()).build();
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto= UpdateUserResponseProto.newBuilder().setStatus("Status 400").build();
	        }
			
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}

}
