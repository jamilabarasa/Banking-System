package com.backend.banking_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String message;

    @Column(name = "notification_date")
    private LocalDateTime notificationDate;

    public Notification(Long userId, String message, LocalDateTime notificationDate) {
        this.userId = userId;
        this.message = message;
        this.notificationDate = notificationDate;
    }
}
