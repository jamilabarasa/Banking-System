package com.backend.banking_app.service.impl;

import com.backend.banking_app.model.Notification;
import com.backend.banking_app.repository.NotificationRepository;
import com.backend.banking_app.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public Notification getNotificationById(Long id) {
        return null;
    }

    @Override
    public Notification createNotification(Notification notification) {
        log.debug("Request to save notification {}",notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return null;
    }

    @Override
    public void deleteNotification(Long id) {

    }
}
