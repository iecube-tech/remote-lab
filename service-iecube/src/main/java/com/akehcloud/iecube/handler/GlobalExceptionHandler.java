package com.akehcloud.iecube.handler;

import com.akehcloud.exception.AbstractBusinessException;
import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultModel<Object> handler(Exception e, HttpServletResponse response) {
        log.error("系统错误", e);
        ResultModel<Object> resultModel = ResultModel.exception(new SystemException("系统异常"));
        response.setStatus(resultModel.getStatus());
        return resultModel;
    }


    @ResponseBody
    @ExceptionHandler(AbstractBusinessException.class)
    public ResultModel<Object> handlerException(AbstractBusinessException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        ResultModel<Object> resultModel = ResultModel.exception(e);
        response.setStatus(resultModel.getStatus());
        return resultModel;
    }
}
