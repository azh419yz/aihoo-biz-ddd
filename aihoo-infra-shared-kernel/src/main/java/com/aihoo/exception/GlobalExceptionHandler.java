package com.aihoo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice("com.aihoo")
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public Map<String, Object> bizExceptionHandler(HttpServletResponse response, BizException ex) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", ex.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> exceptionHandler(HttpServletRequest req, Exception ex, HttpServletResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(byteArrayOutputStream));
        log.error("服务器异常: {}", req.getRequestURI());
        log.error(ex.getMessage(), ex);
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "服务器异常");
        return result;
    }
}