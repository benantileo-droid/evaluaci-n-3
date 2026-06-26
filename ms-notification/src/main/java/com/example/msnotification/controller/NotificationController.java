package com.example.msnotification.controller;

import com.example.msnotification.model.Notification;
import com.example.msnotification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notifications", description = "Gestión de notificaciones a usuarios")
public class NotificationController {

    private final NotificationService service;

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public List<Notification> getAll() { return service.findAll(); }

    @Operation(summary = "Obtener notificación por ID")
    @GetMapping("/{id}")
    public Notification getById(@PathVariable Long id) { return service.findById(id); }

    @Operation(summary = "Crear nueva notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Notification> create(@Valid @RequestBody Notification n) {
        return ResponseEntity.ok(service.save(n));
    }

    @Operation(summary = "Filtrar notificaciones por usuario")
    @GetMapping("/user/{userId}")
    public List<Notification> byUser(@PathVariable Long userId) {
        return service.findByUser(userId);
    }

    @Operation(summary = "Cambiar estado de una notificación")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Notification> cambiarEstado(@PathVariable Long id, @RequestParam Notification.Estado estado) {
        return ResponseEntity.ok(service.changeEstado(id, estado));
    }

    @Operation(summary = "Eliminar notificación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
