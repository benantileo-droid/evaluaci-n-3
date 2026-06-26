package com.example.msnotification;

import com.example.msnotification.model.Notification;
import com.example.msnotification.repository.NotificationRepository;
import com.example.msnotification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repo;

    @InjectMocks
    private NotificationService service;

    // ── findById ────────────────────────────────────────────────────────────

    @Test
    void deberiaRetornarNotificationCuandoExiste() {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");
        notification.setEstado(Notification.Estado.PENDIENTE);

        Mockito.when(repo.findById(1L))
                .thenReturn(Optional.of(notification));

        Notification resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tu cita fue confirmada", resultado.getMensaje());
        assertEquals(Notification.Estado.PENDIENTE, resultado.getEstado());

        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoNotificationNoExiste() {

        Mockito.when(repo.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.findById(99L));

        assertTrue(ex.getMessage().contains("99"));

        verify(repo).findById(99L);
    }

    // ── save ────────────────────────────────────────────────────────────────

    @Test
    void deberiaGuardarNotification() {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");

        Mockito.when(repo.save(notification))
                .thenReturn(notification);

        Notification resultado = service.save(notification);

        assertNotNull(resultado);
        assertEquals("Tu cita fue confirmada", resultado.getMensaje());

        verify(repo).save(notification);
    }

    // ── findByUser ───────────────────────────────────────────────────────────

    @Test
    void deberiaRetornarNotificationsPorUserId() {

        Notification n1 = new Notification();
        n1.setId(1L);
        n1.setUserId(10L);
        n1.setMensaje("Mensaje 1");
        n1.setTipo("EMAIL");

        Notification n2 = new Notification();
        n2.setId(2L);
        n2.setUserId(10L);
        n2.setMensaje("Mensaje 2");
        n2.setTipo("SMS");

        Mockito.when(repo.findByUserId(10L))
                .thenReturn(List.of(n1, n2));

        List<Notification> resultado = service.findByUser(10L);

        assertEquals(2, resultado.size());

        verify(repo).findByUserId(10L);
    }

    // ── changeEstado ─────────────────────────────────────────────────────────

    @Test
    void deberiaActualizarEstadoDeNotification() {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");
        notification.setEstado(Notification.Estado.PENDIENTE);

        Mockito.when(repo.findById(1L))
                .thenReturn(Optional.of(notification));

        Mockito.when(repo.save(notification))
                .thenReturn(notification);

        Notification resultado = service.changeEstado(1L, Notification.Estado.ENVIADO);

        assertEquals(Notification.Estado.ENVIADO, resultado.getEstado());

        verify(repo).findById(1L);
        verify(repo).save(notification);
    }
}
