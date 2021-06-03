package com.atguigu.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public static <T> Result<T> setResult(ResultEnum resultEnum){
        Result<T> result = new Result<>();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

    public Result(T data,ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    private static<T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T data,ResultEnum resultEnum){
        Result<T> result = build(data);
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

    public static <T> Result<T> ok(){
        return Result.ok(null);
    }

    public static <T> Result<T> ok(T data){
        return Result.build(data,ResultEnum.SUCCESS);
    }

    public static <T> Result<T> fail(){
        return Result.fail(null);
    }

    public static <T> Result<T> fail(T data){
        return Result.build(data,ResultEnum.ERROR);
    }

    /**
     * 设置特定的响应消息
     * @param message
     * @return
     */
    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }


    /**
     * 设置特定的响应码
     * @param code
     * @return
     */
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
