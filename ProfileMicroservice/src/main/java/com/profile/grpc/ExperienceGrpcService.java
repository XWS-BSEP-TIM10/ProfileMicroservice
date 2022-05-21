package com.profile.grpc;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.profile.dto.NewExperienceDTO;
import com.profile.model.Experience;
import com.profile.service.ExperienceService;

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
	
	@Autowired
	public ExperienceGrpcService(ExperienceService experienceService) {
		this.service = experienceService;
	}
	
	@Override
	public void add(NewExperienceProto request, StreamObserver<NewExperienceResponseProto> responseObserver) {
		NewExperienceResponseProto responseProto;
		 try {
			    NewExperienceDTO dto = new NewExperienceDTO(request.getUserId(),request.getInstitution(),request.getPosition(),request.getFromDate(),request.getToDate(),request.getDescription(),request.getType());
	            Experience newExperience = new Experience(dto);
	            newExperience.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getFromDate()));
				if(!dto.getToDate().equals("Present"))
	            	newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
				else
					newExperience.setToDate(null);
	            Experience addedExperience = service.add(dto.getUserId(), newExperience);
	            if(addedExperience == null)
	            	responseProto= NewExperienceResponseProto.newBuilder().setStatus("Status 404").build();
	            else
					responseProto = NewExperienceResponseProto.newBuilder().setStatus("Status 200").setId(addedExperience.getId()).setPosition(addedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(addedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto= NewExperienceResponseProto.newBuilder().setStatus("Status 400").build();
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
				if(!dto.getToDate().equals("Present"))
					newExperience.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(dto.getToDate()));
				else
					newExperience.setToDate(null);
	            Experience updatedExperience = service.update(request.getId(), newExperience);
	            if (updatedExperience == null)
	            	responseProto= UpdateExperienceResponseProto.newBuilder().setStatus("Status 404").build();
				else
	            	responseProto = UpdateExperienceResponseProto.newBuilder().setStatus("Status 200").setId(updatedExperience.getId()).setPosition(updatedExperience.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(updatedExperience.getDescription()).setType(dto.getType()).setInstitution(dto.getInstitution()).build();
	        } catch (ParseException e) {
	            e.printStackTrace();
	            responseProto= UpdateExperienceResponseProto.newBuilder().setStatus("Status 400").build();
	        }
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}
	
	@Override
	public void remove(RemoveExperienceProto request, StreamObserver<RemoveExperienceResponseProto> responseObserver) {
			RemoveExperienceResponseProto responseProto;
			
			boolean success = service.remove(request.getId());
	        if(!success)
				responseProto= RemoveExperienceResponseProto.newBuilder().setStatus("Status 404").build();
	        else
				responseProto= RemoveExperienceResponseProto.newBuilder().setStatus("Status 200").build();
		 	
		 	responseObserver.onNext(responseProto);
	        responseObserver.onCompleted();
	}

}
