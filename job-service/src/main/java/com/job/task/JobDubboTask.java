package com.job.task;

import com.job.entity.ScheduleJob;
import com.job.entity.ZookConfig;
import com.job.factory.DubboFactory;
import com.job.pool.JobThreadPoolExecutor;
import com.job.service.api.JobConsumerService;
import com.job.util.DateUtil;
import com.job.util.JsonUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class JobDubboTask implements Job {

    private Logger log = LoggerFactory.getLogger(JobDubboTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap =jobExecutionContext.getMergedJobDataMap();
        for(Object obj:dataMap.values()){
           if(obj instanceof ScheduleJob){
               ScheduleJob job=(ScheduleJob) obj;
               ZookConfig config= JsonUtil.fromJson(job.getJobText(),ZookConfig.class);
               if(config==null){
                   log.info("未找到远程接口"+job);
                   continue;
               }
               config.setRetries(0);
               config.setTimeOut(60000);
               log.info("数据明细"+job);
               Runnable task = new Runnable() {
                   @Override
                   public void run() {
                       try {
                           long startTime=System.currentTimeMillis();
                           JobConsumerService service = DubboFactory.init(config);
                           boolean flag = service.doHandler(job.getJobParam());
                           long endTime=System.currentTimeMillis();
                           if(flag){

                           }
                           log.info("执行成功,总耗时：time="+ DateUtil.getDatePoor(startTime,endTime));
                       }catch (Exception e){
                           log.error("执行异常",e);
                       }finally {

                       }
                   }
               };
               JobThreadPoolExecutor.getDefaultThreadPoolExecutor().execute(task);
           }
        }
    }
}
