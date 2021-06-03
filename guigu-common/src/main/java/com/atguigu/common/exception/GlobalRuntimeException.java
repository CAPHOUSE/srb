package com.atguigu.common.exception;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalRuntimeException extends RuntimeException {

    private Integer code;
    private String message;

    /**
     *
     * @param message 错误消息
     */
    public GlobalRuntimeException(String message) {
        this.message = message;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     */
    public GlobalRuntimeException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     * @param cause 原始异常对象
     */
    public GlobalRuntimeException(String message, Integer code, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     */
    public GlobalRuntimeException(ResultEnum resultCodeEnum) {
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     * @param cause 原始异常对象
     */
    public GlobalRuntimeException(ResultEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }


}
