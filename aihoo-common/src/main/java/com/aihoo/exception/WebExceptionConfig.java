package com.aihoo.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebExceptionConfig implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> error() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "服务器异常");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}