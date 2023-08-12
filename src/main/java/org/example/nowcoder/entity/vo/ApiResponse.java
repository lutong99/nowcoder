package org.example.nowcoder.entity.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse implements ApiResponseConstant {
    private Integer code;
    private String message;
    private boolean success;
    private Map<String, Object> data;

    private ApiResponse() {
        data = new HashMap<>();
    }

    public static ApiResponse create(Integer code, String message, boolean success) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(code);
        apiResponse.setSuccess(success);
        apiResponse.setMessage(message);
        return apiResponse;
    }


    public static ApiResponse success() {
        return create(SUCCESS_CODE, SUCCESS_MESSAGE, SUCCESS);
    }

    public static ApiResponse success(Integer code) {
        return create(code, SUCCESS_MESSAGE, SUCCESS);
    }

    public static ApiResponse success(Integer code, String message) {
        return create(code, message, SUCCESS);
    }

    public static ApiResponse success(String message) {
        return success(SUCCESS_CODE, message);
    }


    public static ApiResponse failure() {
        return create(FAILURE_CODE, FAILURE_MESSAGE, FAILURE);
    }

    public static ApiResponse failure(Integer code) {
        return create(code, FAILURE_MESSAGE, FAILURE);
    }

    public static ApiResponse failure(Integer code, String message) {
        return create(code, message, FAILURE);
    }

    public static ApiResponse failure(String message) {
        return success(FAILURE_CODE, message);
    }

    public ApiResponse data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }


}

