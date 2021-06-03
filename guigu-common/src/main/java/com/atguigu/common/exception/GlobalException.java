package com.atguigu.common.exception;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalException {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error(e.getMessage());
        return Result.setResult(ResultEnum.FAIL);
    }


    /**
     * 特定异常
     */
    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public Result handleBadSqlGrammarException(BadSqlGrammarException e){
        log.error(e.getMessage(),e);
        return Result.setResult(ResultEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    /**
     * 自定义异常
     */
    @ResponseBody
    @ExceptionHandler(GlobalRuntimeException.class)
    public Result error(GlobalRuntimeException e){
        log.error(e.getMessage(),e);
        return Result.ok().message(e.getMessage()).code(e.getCode());
    }

    /**
     * Controller上一层相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public Result handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        //SERVLET_ERROR(-102, "servlet请求异常"),
        return Result.fail().message(ResultEnum.SERVLET_ERROR.getMessage()).
                code(ResultEnum.SERVLET_ERROR.getCode());
    }
}
