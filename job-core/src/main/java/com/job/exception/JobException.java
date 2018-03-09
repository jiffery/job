package com.job.exception;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class JobException extends  RuntimeException {

    /**
     * 错误代码
     */
    private String errorCode = "2000";

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 错误异常
     * @param errorMessage
     */
    public JobException(String errorMessage) {
        super(errorMessage);
    }


    /**
     * 使用错误码、默认提示定义JobException
     *
     * @param errorCode    错误码
     * @param errorMessage 默认错误信息
     */
    public JobException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
