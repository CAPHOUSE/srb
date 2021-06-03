package com.atguigu.common.exception;

import com.atguigu.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public abstract class Assert {

    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     * @param obj 待判断对象
     */
    public static void notNull(Object obj, ResultEnum responseEnum) {
        if (obj == null) {
            log.info("obj is null...............");
            throw new GlobalRuntimeException(responseEnum);
        }
    }


    /**
     * 断言对象为空
     * 如果对象obj不为空，则抛出异常
     * @param object
     * @param responseEnum
     */
    public static void isNull(Object object, ResultEnum responseEnum) {
        if (object != null) {
            log.info("obj is not null......");
            throw new GlobalRuntimeException(responseEnum);
        }
    }

    /**
     * 断言表达式为真
     * 如果不为真，则抛出异常
     *
     * @param expression 是否成功
     */
    public static void isTrue(boolean expression, ResultEnum responseEnum) {
        if (!expression) {
            log.info("fail...............");
            throw new GlobalRuntimeException(responseEnum);
        }
    }

    /**
     * 断言两个对象不相等
     * 如果相等，则抛出异常
     * @param m1
     * @param m2
     * @param responseEnum
     */
    public static void notEquals(Object m1, Object m2,  ResultEnum responseEnum) {
        if (m1.equals(m2)) {
            log.info("equals...............");
            throw new GlobalRuntimeException(responseEnum);
        }
    }

    /**
     * 断言两个对象相等
     * 如果不相等，则抛出异常
     * @param m1
     * @param m2
     * @param responseEnum
     */
    public static void equals(Object m1, Object m2,  ResultEnum responseEnum) {
        if (!m1.equals(m2)) {
            log.info("not equals...............");
            throw new GlobalRuntimeException(responseEnum);
        }
    }

    /**
     * 断言参数不为空
     * 如果为空，则抛出异常
     * @param s
     * @param responseEnum
     */
    public static void notEmpty(String s, ResultEnum responseEnum) {
        if (StringUtils.isEmpty(s)) {
            log.info("is empty...............");
            throw new GlobalRuntimeException(responseEnum);
        }
    }
}