package com.profile.grpc;

import com.profile.model.Notification;
import com.profile.service.NotificationService;
import io.grpc.stub.StreamObserver;
import proto.NewNotificationProto;
import proto.NotificationGrpcServiceGrpc;
import proto.NotificationResponseProto;

public class NotificationGrpcService extends NotificationGrpcServiceGrpc.NotificationGrpcServiceImplBase {

    private final NotificationService notificationService;

    public NotificationGrpcService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void add(NewNotificationProto request, StreamObserver<NotificationResponseProto> responseObserver) {
        Notification newNotification = new Notification(request.getText());
        notificationService.save(newNotification, request.getUserId());
        NotificationResponseProto responseProto = NotificationResponseProto.newBuilder().setStatus("Status 200").build();
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }


}
