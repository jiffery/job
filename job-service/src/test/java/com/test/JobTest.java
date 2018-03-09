package com.test;

import com.job.entity.ScheduleJob;
import com.job.service.api.SchedulerManageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class JobTest extends  BaseJunit4Test{
    @Autowired
    private SchedulerManageService schedulerManageService;

    @Test
    public void addJob()throws Exception{
        ScheduleJob job=new ScheduleJob();
        job.setScheduleJobId(1L);
        job.setJobName("simpleJob");
        job.setJobGroup("test");
        job.setJobStatus("2");
        job.setClazz("com.job.task.JobDubboTask");
        job.setJobText("123");
        job.setJobParam("111");
        job.setCronExpression("0/10 * * * * ?");
        job.setCreateMan(1L);
        job.setCreateTime(new Date());
        job.setUpdateMan(1L);
        job.setUpdateTime(new Date());
        schedulerManageService.add(job);
    }

    @Test
    public void stop()throws  Exception{
        ScheduleJob job=new ScheduleJob();
        job.setJobName("simpleJob");
        job.setJobGroup("test");
        schedulerManageService.pauseJob(job);
    }

    @Test
    public  void resum()throws  Exception{
        ScheduleJob job=new ScheduleJob();
        job.setJobName("simpleJob");
        job.setJobGroup("test");
        schedulerManageService.resumeJob(job);
    }

    @Test
    public void update()throws  Exception{
        ScheduleJob job=new ScheduleJob();
        job.setJobName("simpleJob");
        job.setJobGroup("test");
        job.setJobStatus("2");
        job.setClazz("com.job.task.JobDubboTask");
        job.setCronExpression("0/5 * * * * ?");
        schedulerManageService.update(job);
    }
}
