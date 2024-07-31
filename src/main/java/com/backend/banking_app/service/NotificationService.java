package com.backend.banking_app.service;

import com.backend.banking_app.model.Notification;

public interface NotificationService {

    Notification getNotificationById(Long id);

    Notification createNotification(Notification notification);

    Notification updateNotification(Notification notification);

    void deleteNotification(Long id);
}
