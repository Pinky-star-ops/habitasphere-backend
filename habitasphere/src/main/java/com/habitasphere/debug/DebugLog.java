package com.habitasphere.debug;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DebugLog {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Path LOG_PATH = Path.of("debug-e93500.log");

    private DebugLog() {}

    public static void log(String runId,
                           String hypothesisId,
                           String location,
                           String message,
                           Map<String, Object> data) {
        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sessionId", "e93500");
            payload.put("runId", runId);
            payload.put("hypothesisId", hypothesisId);
            payload.put("location", location);
            payload.put("message", message);
            payload.put("data", data == null ? Map.of() : data);
            payload.put("timestamp", System.currentTimeMillis());
            Files.writeString(
                    LOG_PATH,
                    MAPPER.writeValueAsString(payload) + "\n",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (Exception ignored) {}
    }
}

