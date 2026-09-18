package com.onclinic.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/heartbeat")
public class HeartbeatController {
    @PostMapping("/measure")
    public Map<String,Object> measure(@RequestBody Map<String,Object> body) {
        // Mobile/device integration point. The server does not fabricate a physiological measurement.
        return Map.of("status", "DEVICE_MEASUREMENT_REQUIRED",
                "message", "Send the measurement captured by a supported mobile sensor/device.",
                "received", body);
    }
}