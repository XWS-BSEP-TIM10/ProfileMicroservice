package com.profile.grpc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.profile.model.User;
import com.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.profile.model.Notification;
import com.profile.model.UserNotification;
import com.profile.service.NotificationService;
import com.profile.service.UserNotificationService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.GetNotificationProto;
import proto.NewNotificationProto;
import proto.NewPostNotificationProto;
import proto.NotificationGrpcServiceGrpc;
import proto.NotificationProto;
import proto.NotificationResponseProto;
import proto.NotificationsProto;

@GrpcService
public class NotificationGrpcService extends NotificationGrpcServiceGrpc.NotificationGrpcServiceImplBase {

    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final UserService userService;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    @Autowired
    public NotificationGrpcService(NotificationService notificationService, UserNotificationService userNotificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }

    @Override
    public void add(NewNotificationProto request, StreamObserver<NotificationResponseProto> responseObserver) {
        Notification newNotification = new Notification(request.getText());
        notificationService.save(newNotification, request.getUserId());
        NotificationResponseProto responseProto = NotificationResponseProto.newBuilder().setStatus("Status 200").build();
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getNotifications(GetNotificationProto request, StreamObserver<NotificationsProto> responseObserver) {
    	List<NotificationProto> notificationProtos = new ArrayList<NotificationProto>();
        Optional<User> user = userService.findById(request.getUserId());
        List<UserNotification> userNotifications = userNotificationService.findByUserId(request.getUserId());
    	for(UserNotification userNotification:userNotifications) {
            if(user.get().isMuteConnectionsNotifications() && userNotification.getNotification().getText().contains("connect"))
                continue;
            if(user.get().isMuteMassageNotifications() && userNotification.getNotification().getText().contains("message"))
                continue;
            if(user.get().isMutePostNotifications() && userNotification.getNotification().getText().contains("post"))
                continue;
    		NotificationProto proto = NotificationProto.newBuilder().setId(userNotification.getNotification().getId().toString()).setRead(userNotification.isRead()).setCreationTime(dateFormatter.format(userNotification.getNotification().getCreationTime())).setText(userNotification.getNotification().getText()).build();
    		notificationProtos.add(proto);
    	}
    	NotificationsProto notificationsProto = NotificationsProto.newBuilder().addAllNotifications(notificationProtos).setStatus("200").build();
        responseObserver.onNext(notificationsProto);
        responseObserver.onCompleted();
    }
    
    @Override
    public void changeNotificationsStatus(GetNotificationProto request, StreamObserver<NotificationResponseProto> responseObserver) {
    	userNotificationService.markNotificationsAsReadForUser(request.getUserId());
    	NotificationResponseProto notificationResponseProto = NotificationResponseProto.newBuilder().setStatus("200").build();
    	responseObserver.onNext(notificationResponseProto);
        responseObserver.onCompleted();
    }
    
    @Override
    public void addPostNotification(NewPostNotificationProto request, StreamObserver<NotificationResponseProto> responseObserver) {
    	Notification newNotification = new Notification(request.getText());
        notificationService.saveMultiple(newNotification, request.getUserIdList());
        NotificationResponseProto responseProto = NotificationResponseProto.newBuilder().setStatus("Status 200").build();
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }
}
