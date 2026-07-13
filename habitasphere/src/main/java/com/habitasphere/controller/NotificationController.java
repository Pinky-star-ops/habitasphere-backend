package com.habitasphere.controller;

import com.habitasphere.dto.NotificationResponseDto;
import com.habitasphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotifications() {

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications()
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notificationService.markAsRead(id)
        );
    }
}