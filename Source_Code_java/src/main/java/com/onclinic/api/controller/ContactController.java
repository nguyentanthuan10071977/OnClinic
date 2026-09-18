package com.onclinic.api.controller;

import com.onclinic.api.model.*;
import com.onclinic.api.repository.ContactMessageRepository;
import com.onclinic.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController @RequestMapping("/api/contact") @RequiredArgsConstructor
public class ContactController {
    private final ContactMessageRepository messages;
    private final AuthService auth;

    @PostMapping
    public ContactMessage contact(@RequestBody Map<String,String> body, java.security.Principal p) {
        String text = body.get("message");
        if (text==null || text.isBlank()) throw new IllegalArgumentException("Message is required");
        return messages.save(ContactMessage.builder().user(auth.current(p.getName())).message(text)
                .createdAt(LocalDateTime.now()).build());
    }
}