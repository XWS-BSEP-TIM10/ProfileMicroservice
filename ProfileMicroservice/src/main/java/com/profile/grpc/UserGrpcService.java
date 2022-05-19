package com.profile.grpc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.profile.exception.UserNotFoundException;
import com.profile.exception.UsernameAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;

import com.profile.dto.NewUserDTO;
import com.profile.model.Experience;
import com.profile.model.Interest;
import com.profile.model.User;
import com.profile.saga.dto.OrchestratorResponseDTO;
import com.profile.service.UserService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.*;

@GrpcService
public class UserGrpcService extends UserGrpcServiceGrpc.UserGrpcServiceImplBase{
	
	private final UserService service;
	private static final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	public UserGrpcService(UserService userService) {
		this.service = userService;
	}
	
	@Override
	public void update(UpdateUserProto request, StreamObserver<UpdateUserResponseProto> responseObserver) {
			UpdateUserResponseProto responseProto;
			
			try {
				NewUserDTO dto = new NewUserDTO(request.getUuid(),request.getFirstName(),request.getLastName(),request.getEmail(),request.getPhoneNumber(),request.getGender(),request.getDateOfBirth(),request.getUsername(),request.getBiography());
	            User user = new User(dto);
	            user.setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getDateOfBirth()));
	            OrchestratorResponseDTO response = service.updateUser(user).block();
	            responseProto= UpdateUserResponseProto.newBuilder().setStatus("Status 200").setSuccess(response.isSuccess()).setMessage(response.getMessage()).build();
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto = UpdateUserResponseProto.newBuilder().setStatus("Status 400").build();
	        } catch (UserNotFoundException e){
				e.printStackTrace();
				responseProto = UpdateUserResponseProto.newBuilder().setStatus("Status 404").build();
			} catch (UsernameAlreadyExists e) {
				e.printStackTrace();
				responseProto = UpdateUserResponseProto.newBuilder().setStatus("Status 409").build();
			}


		responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}
	
	@Override
	public void find(FindUserProto request, StreamObserver<FindUserResponseProto> responseObserver) {
		FindUserResponseProto responseProto;
		
		List<User> users = service.findByFirstNameAndLastName(request.getFirstName(), request.getLastName());
		List<UserProto> protoUsers = new ArrayList<>();
		
		
		for(User user:users) {
			List<ExperienceProto> experiences = new ArrayList<>();
			List<InterestProto> interests = new ArrayList<>();
			for(Experience experience: user.getExperiences()) {
				experiences.add(ExperienceProto.newBuilder().setId(experience.getId()).setInstitution(experience.getInstitution()).setPosition(experience.getPosition()).setFromDate(iso8601Formatter.format(experience.getFromDate())).setToDate(iso8601Formatter.format(experience.getToDate())).setDescription(experience.getDescription()).setType(experience.getType().toString()).build());
			}
			for(Interest interest: user.getInterests()) {
				interests.add(InterestProto.newBuilder().setId(interest.getId()).setDescription(interest.getDescription()).build());
			}
			UserProto newUser = UserProto.newBuilder().setUuid(user.getId()).setFirstName(user.getFirstName()).setLastName(user.getLastName()).setEmail(user.getEmail()).setPhoneNumber(user.getPhoneNumber()).setGender(user.getGender().toString()).setDateOfBirth(iso8601Formatter.format(user.getDateOfBirth())).setUsername(user.getUsername()).setBiography(user.getBiography()).addAllExperiences(experiences).addAllInterests(interests).build();
			protoUsers.add(newUser);
			
		}
		
		responseProto = FindUserResponseProto.newBuilder().addAllUsers(protoUsers).setStatus("Status 200").build();
		
		responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
	}

	@Override
	public void getEmail(EmailProto request, StreamObserver<EmailResponseProto> responseObserver) {

		User user = service.findById(request.getId()).get();
		EmailResponseProto responseProto;
		if(user != null) {
			responseProto = EmailResponseProto.newBuilder().setEmail(user.getEmail()).setStatus("Status 200").build();
		}else{
			responseProto = EmailResponseProto.newBuilder().setEmail("").setStatus("Status 404").build();
		}

		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();
	}

	@Override
	public void getId(IdProto request, StreamObserver<IdResponseProto> responseObserver) {

		String id = service.findIdByEmail(request.getEmail());
		IdResponseProto responseProto;

		if(id != null) {
			responseProto = IdResponseProto.newBuilder().setId(id).setStatus("Status 200").build();
		}else{
			responseProto = IdResponseProto.newBuilder().setId("").setStatus("Status 400").build();
		}
		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();

	}

}
