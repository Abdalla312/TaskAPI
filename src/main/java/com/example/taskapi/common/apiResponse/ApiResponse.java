package com.example.taskapi.common.apiResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String timeStamp;

    public ApiResponse(int status,
                       String message,
                       T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timeStamp = LocalDateTime.now().toString();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(201, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(401, message);
    }

    public static <T> ApiResponse<T> accessDenied(String message) {
        return error(403, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(409, message);
    }
}
