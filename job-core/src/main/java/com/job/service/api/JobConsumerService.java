package com.job.service.api;

/**
 * @author 子羽
 * @date 2018年03月07日
 */
public interface JobConsumerService {

    /**
     * 执行job默认方法
     * @return
     */
    public Boolean doHandler(String msg);
}
