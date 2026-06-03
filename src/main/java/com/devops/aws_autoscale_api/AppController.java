package com.devops.aws_autoscale_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        try {
            response.put("message", "Hello from AWS Auto-Scaling Infrastructure!");
            response.put("server", InetAddress.getLocalHost().getHostName());
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("status", "healthy");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            response.put("server", InetAddress.getLocalHost().getHostName());
            response.put("availableProcessors", runtime.availableProcessors());
            response.put("freeMemoryMB", runtime.freeMemory() / (1024 * 1024));
            response.put("totalMemoryMB", runtime.totalMemory() / (1024 * 1024));
            response.put("timestamp", LocalDateTime.now().toString());
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

}