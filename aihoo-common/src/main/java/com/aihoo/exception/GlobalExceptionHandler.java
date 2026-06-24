package com.aihoo.exception;

import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.aihoo")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BizResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return BizResult.fail(BizResultCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(BizException.class)
    public BizResult<Void> handleBusinessException(BizException ex) {
        log.error(ex.getMessage(), ex);
        if (ex.getResultCode() != null) {
            return BizResult.fail(ex.getResultCode());
        }
        return BizResult.fail(500, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BizResult<Void> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return BizResult.fail(403, "没有访问权限");
    }

    @ExceptionHandler(Exception.class)
    public BizResult<Void> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return BizResult.fail(500, message == null || message.trim().isEmpty() ? "系统繁忙" : message);
    }
}
