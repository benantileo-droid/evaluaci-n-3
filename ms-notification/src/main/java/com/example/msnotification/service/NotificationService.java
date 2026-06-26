package com.example.msnotification.service;

import com.example.msnotification.model.Notification;
import com.example.msnotification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    public List<Notification> findAll() { return repo.findAll(); }

    public Notification findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada: " + id));
    }

    public Notification save(Notification n) { return repo.save(n); }

    public List<Notification> findByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public Notification changeEstado(Long id, Notification.Estado estado) {
        Notification n = findById(id);
        n.setEstado(estado);
        return repo.save(n);
    }

    public void delete(Long id) { repo.deleteById(id); }
}
