package com.example.msnotification.repository;

import com.example.msnotification.model.Notification;
import com.example.msnotification.model.Notification.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByEstado(Estado estado);
}
