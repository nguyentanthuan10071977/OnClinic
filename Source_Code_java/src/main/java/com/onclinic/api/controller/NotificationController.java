package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.*;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/notifications") @RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notifications;
    private final AuthService auth;

    @GetMapping
    public List<Notification> list(java.security.Principal p) {
        return notifications.findByUserIdOrderByCreatedAtDesc(auth.current(p.getName()).getId());
    }

    @PutMapping("/{id}/read")
    public Notification read(@PathVariable Long id, java.security.Principal p) {
        Notification n = notifications.findById(id).orElseThrow();
        if (!n.getUser().getId().equals(auth.current(p.getName()).getId())) throw new IllegalStateException("Not your notification");
        n.setRead(true); return notifications.save(n);
    }
}