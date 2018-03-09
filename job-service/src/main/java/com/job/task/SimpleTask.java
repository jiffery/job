package com.job.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class SimpleTask implements Job {

    private Logger log = LoggerFactory.getLogger(SimpleTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap =jobExecutionContext.getMergedJobDataMap();

        System.out.println("测试job工作配置======"+Thread.currentThread());
        log.info("job执行完毕");
    }
}
