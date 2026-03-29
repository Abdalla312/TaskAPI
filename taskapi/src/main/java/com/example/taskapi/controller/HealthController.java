package com.example.taskapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller — your first Spring Boot endpoint!
 *
 * Django equivalent:
 *   @api_view(['GET'])
 *   def health(request):
 *       return Response({"status": "UP", "timestamp": str(datetime.now())})
 *
 * Key concepts:
 * - @RestController = @Controller + @ResponseBody
 *   (tells Spring: "this class handles HTTP requests and returns JSON")
 * - @RequestMapping("/api") = like putting path("api/", ...) in urls.py
 * - @GetMapping("/health") = this method handles GET /api/health
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        // Map.of() creates an immutable map — like dict() in Python
        Map<String, Object> response = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "message", "Task API is running! 🚀"
        );
        // ResponseEntity.ok() = Response(data, status=200) in DRF
        return ResponseEntity.ok(response);
    }
}
