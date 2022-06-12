package com.profile.grpc;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.profile.dto.NewExperienceDTO;
import com.profile.model.Experience;
import com.profile.service.ExperienceService;
import com.profile.service.LoggerService;
import com.profile.service.UserService;
import com.profile.service.impl.LoggerServiceImpl;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.ExperienceGrpcServiceGrpc;
import proto.NewExperienceProto;
import proto.NewExperienceResponseProto;
import proto.RemoveExperienceProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceProto;
import proto.UpdateExperienceResponseProto;

@GrpcService
public class ExperienceGrpcService extends ExperienceGrpcServiceGrpc.ExperienceGrpcServiceImplBase{
	
	private final ExperienceService service;
	private final LoggerService loggerService;
	private final UserService userService;
	
	@Autowired
	public ExperienceGrpcService(ExperienceService experienceService, UserService userService) {
		this.service = experienceService;
		this.loggerService = new LoggerServiceImpl(this.getClass());
		this.userService = userService;
	}
	
	@Override
	public void add(NewExperienceProto request, StreamObserver<NewExperienceResponseProto> responseObserver) {
		NewExperienceResponseProto responseProto;
		 try {
			    NewExperienceDTO dto = new NewExperienceDTO(request.getUserId(),request.getInstitution(),request.getPosition(),request.getFromDate(),request.getToDate(),request.getDescription(),request.getType());
	            Experience newExperience = new Experience(dto);
	            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
				if(dto.getToDate().equals(""))
					newExperience.setToDate(null);
				else
					newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
	            Experience addedExperience = service.add(dto.getUserId(), newExperience);
	            if(addedExperience == null) {
	            	responseProto= NewExperienceResponseProto.newBuilder().setStatus("Status 404").build();
	            	loggerService.addExperienceUserNotFound(dto.getUserId());
	            }
	            else {
					responseProto = NewExperienceResponseProto.newBuilder().setStatus("Status 200").setId(addedExperience.getId()).setPosition(addedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(addedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();
					loggerService.addExperience(userService.findById(dto.getUserId()).get().getEmail());
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto= NewExperienceResponseProto.newBuilder().setStatus("Status 400").build();
	            loggerService.addExperienceBadDate(request.getUserId());
	        }
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}
	
	@Override
	public void update(UpdateExperienceProto request, StreamObserver<UpdateExperienceResponseProto> responseObserver) {
			UpdateExperienceResponseProto responseProto;
			
			try {
				NewExperienceDTO dto = new NewExperienceDTO(request.getUserId(),request.getInstitution(),request.getPosition(),request.getFromDate(),request.getToDate(),request.getDescription(),request.getType());
	            Experience newExperience = new Experience(dto);
	            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
				if(dto.getToDate().equals(""))
					newExperience.setToDate(null);
				else
					newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
	            Experience updatedExperience = service.update(request.getId(), newExperience);
	            if (updatedExperience == null) {
	            	responseProto= UpdateExperienceResponseProto.newBuilder().setStatus("Status 404").build();
	            	loggerService.updateExperienceNotFound(String.valueOf(request.getId()));
	            }
				else {
	            	responseProto = UpdateExperienceResponseProto.newBuilder().setStatus("Status 200").setId(updatedExperience.getId()).setPosition(updatedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(updatedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();
	            	loggerService.updateExperience(String.valueOf(request.getId()), request.getUserId());
				}
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto= UpdateExperienceResponseProto.newBuilder().setStatus("Status 400").build();
	            loggerService.addExperienceBadDate(request.getUserId());
	        }
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}
	
	@Override
	public void remove(RemoveExperienceProto request, StreamObserver<RemoveExperienceResponseProto> responseObserver) {
			RemoveExperienceResponseProto responseProto;
			
			boolean success = service.remove(request.getId());
	        if(!success)
	        {
				responseProto= RemoveExperienceResponseProto.newBuilder().setStatus("Status 404").build();
				loggerService.deleteExperienceNotFound(String.valueOf(request.getId()));
	        }
	        else
	        {
				responseProto= RemoveExperienceResponseProto.newBuilder().setStatus("Status 200").build();
				loggerService.deleteExperience(String.valueOf(request.getId()));
	        }
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}

}
