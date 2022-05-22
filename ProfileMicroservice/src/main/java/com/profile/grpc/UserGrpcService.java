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
			List<ExperienceProto> experiences = getExperiences(user);
			List<InterestProto> interests = getInterests(user);
			UserProto newUser = UserProto.newBuilder().setUuid(user.getId()).setFirstName(user.getFirstName())
					.setLastName(user.getLastName()).setEmail(user.getEmail())
					.setPhoneNumber(user.getPhoneNumber())
					.setGender(user.getGender().toString())
					.setDateOfBirth(iso8601Formatter.format(user.getDateOfBirth()))
					.setUsername(user.getUsername()).setBiography(user.getBiography())
					.addAllExperiences(experiences).addAllInterests(interests).build();
			protoUsers.add(newUser);
		}
		
		responseProto = FindUserResponseProto.newBuilder().addAllUsers(protoUsers).setStatus("Status 200").build();
		
		responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
	}

	private List<ExperienceProto> getExperiences(User user) {
		List<ExperienceProto> experiences = new ArrayList<>();
		for(Experience experience: user.getExperiences()) {
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
		for(Interest interest: user.getInterests()) {
			interests.add(InterestProto.newBuilder().setId(interest.getId()).setDescription(interest.getDescription()).build());
		}
		return interests;
	}

	@Override
	public void getFirstAndLastName(UserNamesProto request, StreamObserver<UserNamesResponseProto> responseObserver) {

		Optional<User> user = service.findById(request.getId());
		UserNamesResponseProto responseProto;
		if(user.isEmpty())
			responseProto = UserNamesResponseProto.newBuilder().setStatus("Status 404").build();
		else
			responseProto = UserNamesResponseProto.newBuilder().setStatus("Status 200")
													.setFirstName(user.get().getFirstName()).setLastName(user.get().getLastName()).build();

		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();

	}
	public void getEmail(EmailProto request, StreamObserver<EmailResponseProto> responseObserver) {

		Optional<User> user = service.findById(request.getId());
		EmailResponseProto responseProto;
		responseProto = user.map(value -> EmailResponseProto.newBuilder().setEmail(value.getEmail()).setStatus("Status 200").build())
				.orElseGet(() -> EmailResponseProto.newBuilder().setEmail("").setStatus("Status 404").build());

		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();
	}

	@Override
	public void getId(IdProto request, StreamObserver<IdResponseProto> responseObserver) {

		IdResponseProto responseProto;
		try {
			String id = service.findIdByEmail(request.getEmail());
			responseProto = IdResponseProto.newBuilder().setId(id).setStatus("Status 200").build();
		}catch(NullPointerException ex){
			responseProto = IdResponseProto.newBuilder().setId("").setStatus("Status 400").build();
		}

		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();

	}

	@Override
	public void getById(UserIdProto request, StreamObserver<UserResponseProto> responseObserver) {
		Optional<User> user = service.findById(request.getId());
		UserResponseProto responseProto;

		if(user.isPresent()) {
			List<ExperienceProto> experiences = getExperiences(user.get());
			List<InterestProto> interests = getInterests(user.get());
			UserProto userProto = UserProto.newBuilder().setUuid(user.get().getId())
					.setFirstName(user.get().getFirstName())
					.setLastName(user.get().getLastName()).setEmail(user.get().getEmail())
					.setPhoneNumber(user.get().getPhoneNumber())
					.setGender(user.get().getGender().toString())
					.setDateOfBirth(iso8601Formatter.format(user.get().getDateOfBirth()))
					.setUsername(user.get().getUsername()).setBiography(user.get().getBiography())
					.addAllExperiences(experiences).addAllInterests(interests).build();
			responseProto = UserResponseProto.newBuilder().setUser(userProto).setStatus("Status 200").build();
		}
		else
			responseProto = UserResponseProto.newBuilder().setStatus("Status 404").build();

		responseObserver.onNext(responseProto);
		responseObserver.onCompleted();

	}


}
