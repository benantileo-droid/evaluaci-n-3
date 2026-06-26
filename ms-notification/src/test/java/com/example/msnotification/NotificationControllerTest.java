package com.example.msnotification;

import com.example.msnotification.controller.NotificationController;
import com.example.msnotification.model.Notification;
import com.example.msnotification.security.jwt.JwtService;
import com.example.msnotification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService service;

    @MockitoBean
    private JwtService jwtService;

    // ── GET /api/notifications/{id} ──────────────────────────────────────────

    @Test
    void deberiaRetornarNotificationPorId() throws Exception {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");
        notification.setEstado(Notification.Estado.PENDIENTE);

        when(service.findById(1L))
                .thenReturn(notification);

        mockMvc.perform(get("/api/notifications/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.mensaje").value("Tu cita fue confirmada"))
                .andExpect(jsonPath("$.tipo").value("EMAIL"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(service).findById(1L);
    }

    // ── POST /api/notifications ──────────────────────────────────────────────

    @Test
    void deberiaCrearNotification() throws Exception {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");
        notification.setEstado(Notification.Estado.PENDIENTE);

        when(service.save(any(Notification.class)))
                .thenReturn(notification);

        String json = """
                {
                    "userId": 10,
                    "mensaje": "Tu cita fue confirmada",
                    "tipo": "EMAIL"
                }
                """;

        mockMvc.perform(post("/api/notifications")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.mensaje").value("Tu cita fue confirmada"))
                .andExpect(jsonPath("$.tipo").value("EMAIL"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(service).save(any(Notification.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {

        String json = """
                {
                    "tipo": "EMAIL"
                }
                """;

        mockMvc.perform(post("/api/notifications")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/notifications/user/{userId} ─────────────────────────────────

    @Test
    void deberiaRetornarNotificationsPorUserId() throws Exception {

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

        when(service.findByUser(10L))
                .thenReturn(List.of(n1, n2));

        mockMvc.perform(get("/api/notifications/user/10")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].mensaje").value("Mensaje 1"))
                .andExpect(jsonPath("$[1].mensaje").value("Mensaje 2"));

        verify(service).findByUser(10L);
    }

    // ── PATCH /api/notifications/{id}/estado ─────────────────────────────────

    @Test
    void deberiaActualizarEstadoDeNotification() throws Exception {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMensaje("Tu cita fue confirmada");
        notification.setTipo("EMAIL");
        notification.setEstado(Notification.Estado.ENVIADO);

        when(service.changeEstado(eq(1L), eq(Notification.Estado.ENVIADO)))
                .thenReturn(notification);

        mockMvc.perform(patch("/api/notifications/1/estado")
                        .param("estado", "ENVIADO")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ENVIADO"));

        verify(service).changeEstado(1L, Notification.Estado.ENVIADO);
    }

    // ── DELETE /api/notifications/{id} ───────────────────────────────────────

    @Test
    void deberiaEliminarNotification() throws Exception {

        mockMvc.perform(delete("/api/notifications/1")
                        .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
