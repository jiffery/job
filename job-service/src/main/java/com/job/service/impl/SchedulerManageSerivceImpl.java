package com.job.service.impl;

import com.job.dao.ScheduleJobDao;
import com.job.entity.ScheduleJob;
import com.job.exception.JobException;
import com.job.service.api.SchedulerManageService;
import com.job.util.DateUtil;
import com.job.util.JsonUtil;
import com.job.util.StringUtil;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
@Service
public class SchedulerManageSerivceImpl  implements SchedulerManageService {


    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobDao scheduleJobDao;
    /**
     * 新增任务
     * @param scheduleJob
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ScheduleJob scheduleJob)  throws Exception{
        int count=scheduleJobDao.insert(scheduleJob);
        if(count==0){
            throw new JobException("新增Job失败");
        }
        if(!StringUtil.isBlank(scheduleJob.getCronExpression())){
            this.addJobCron(scheduleJob);
        }else{
            this.addJobSimple(scheduleJob);
        }
    }

    /**
     * 更新任务
     * @param scheduleJob
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJob scheduleJob)  throws Exception{
        int count=scheduleJobDao.updateByPrimaryKeySelective(scheduleJob);
        if(count==0){
            throw new JobException("修改Job失败");
        }
        if(!StringUtil.isBlank(scheduleJob.getCronExpression())){
            this.updateJobCron(scheduleJob);
        }else{
            this.updateJobSimple(scheduleJob);
        }

    }

    /**
     * 新增任务
     * @param scheduleJob
     * @throws Exception
     */
    private void addJobCron(ScheduleJob scheduleJob) throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //任务触发
        Trigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(scheduleJob.getClazz()))
                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            /*withMisfireHandlingInstructionDoNothing
            ——不触发立即执行
            ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行

            withMisfireHandlingInstructionIgnoreMisfires
            ——以错过的第一个频率时间立刻开始执行
            ——重做错过的所有频率周期后
            ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行

            withMisfireHandlingInstructionFireAndProceed
            ——以当前时间为触发频率立刻触发一次执行
            ——然后按照Cron频率依次执行*/
            trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("新增Cron任务："+ JsonUtil.toJson(scheduleJob));
        }else {
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            /*CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info(CC.LOG_PREFIX + "任务"+JasonUtils.Object2String(scheduleJob)+"已经存在，更新trigger");*/
            this.updateJobCron(scheduleJob);
        }
    }

    /**
     * 更新任务的时间表达式
     * @param scheduleJob
     */
    @Override
    public void updateJobCron(ScheduleJob scheduleJob) throws Exception{
        /*TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

        //按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
        //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);*/
        //为什么要删除再新增呢？因为不这样，JobDetail的JobDataMap不更新。注解什么都试过了，没起作用。
        this.deleteJob(scheduleJob);
        this.addJobCron(scheduleJob);
        log.info("更新Cron任务（先删除再更新）："+JsonUtil.toJson(scheduleJob));
    }

    /**
     * 新增任务
     * @param scheduleJob
     * @throws Exception
     */
    private void addJobSimple(ScheduleJob scheduleJob) throws Exception{
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //任务触发
        SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(scheduleJob.getClazz()))
                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
            Date triggerStartTime = new Date();
            if("秒".equals(scheduleJob.getTimeType()) ||
                    "second".equalsIgnoreCase(scheduleJob.getTimeType())){
                simpleScheduleBuilder.withIntervalInSeconds(scheduleJob.getTimeValue());
                triggerStartTime = DateUtil.dateAddSeconds(triggerStartTime, scheduleJob.getTimeValue());
            }else if("分".equals(scheduleJob.getTimeType()) || "分钟".equals(scheduleJob.getTimeType()) ||
                    "minute".equalsIgnoreCase(scheduleJob.getTimeType())){
                simpleScheduleBuilder.withIntervalInMinutes(scheduleJob.getTimeValue());
                triggerStartTime = DateUtil.dateAddMinutes(triggerStartTime, scheduleJob.getTimeValue());
            }else if("时".equals(scheduleJob.getTimeType()) || "小时".equals(scheduleJob.getTimeType()) ||
                    "hour".equalsIgnoreCase(scheduleJob.getTimeType())){
                simpleScheduleBuilder.withIntervalInHours(scheduleJob.getTimeValue());
                triggerStartTime = DateUtil.dateAddHours(triggerStartTime, scheduleJob.getTimeValue());
            }else if("天".equals(scheduleJob.getTimeType()) ||
                    "date".equalsIgnoreCase(scheduleJob.getTimeType())){
                simpleScheduleBuilder.withIntervalInHours(scheduleJob.getTimeValue()*24);//2017-09-15修正问题，少了24
                triggerStartTime = DateUtil.dateAddDays(triggerStartTime, scheduleJob.getTimeValue());
            }

            trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .startAt(triggerStartTime).withSchedule(simpleScheduleBuilder.repeatForever().withMisfireHandlingInstructionNextWithRemainingCount()).build();
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("新增简单任务："+JsonUtil.toJson(scheduleJob));
        }else {
            this.updateJobCron(scheduleJob);
        }
    }

    /**
     * 更新任务的时间表达式
     * @param scheduleJob
     * @throws Exception
     */
    private void updateJobSimple(ScheduleJob scheduleJob) throws Exception{
        //为什么要删除再新增呢？因为不这样，JobDetail的JobDataMap不更新。注解什么都试过了，没起作用。
        this.deleteJob(scheduleJob);
        this.addJobSimple(scheduleJob);
        log.info( "更新简单任务（先删除再更新）："+JsonUtil.toJson(scheduleJob));
    }



    /**
     * 暂停任务
     * @param scheduleJob
     * @throws Exception
     */
    @Override
    public void pauseJob(ScheduleJob scheduleJob) throws Exception{
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
        log.info("暂停任务："+JsonUtil.toJson(scheduleJob));
    }

    /**
     * 暂停全部任务
     * @throws SchedulerException
     */
    @Override
    public void pauseAll() throws Exception{
        scheduler.pauseAll();
        log.info("暂停所有任务");
    }

    /**
     * 恢复任务
     * @param scheduleJob
     * @throws Exception
     */
    @Override
    public void resumeJob(ScheduleJob scheduleJob) throws Exception{
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
        log.info("恢复任务："+JsonUtil.toJson(scheduleJob));
    }


    /**
     * 恢复所有任务
     * @throws Exception
     */
    @Override
    public void resumeAll() throws Exception{
        scheduler.resumeAll();
        log.info("恢复所有任务");
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * @param scheduleJob
     * @throws Exception
     */
    @Override
    public void deleteJob(ScheduleJob scheduleJob) throws Exception{
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);//先暂停任务
        scheduler.deleteJob(jobKey);//再删除任务
        log.info("删除任务："+JsonUtil.toJson(scheduleJob));
    }

    /**
     * 立即运行任务
     * @param scheduleJob
     * @throws Exception
     */
    @Override
    public void triggerJob(ScheduleJob scheduleJob) throws Exception{
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
        log.info("运行任务："+JsonUtil.toJson(scheduleJob));
    }


    /**
     * 获取quartz调度器的计划任务
     * @return
     */
    @Override
    public List<ScheduleJob> getScheduleJobList(){
        List<ScheduleJob> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<ScheduleJob>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob job = new ScheduleJob();
                    job.setJobName(jobKey.getName());
                    job.setJobGroup(jobKey.getGroup());
                    job.setClazz(jobKey.getClass().toString());
                    job.setJobDesc("触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    job.setJobStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        job.setCronExpression(cronExpression);
                    }else if(trigger instanceof SimpleTrigger){
                        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                        long milliseconds = simpleTrigger.getRepeatInterval();
                        job.setTimeValue((int) (milliseconds/1000));
                    }
                    jobList.add(job);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobList;
    }

    /**
     * 获取quartz调度器的运行任务
     * @return
     */
    @Override
    public List<ScheduleJob> getScheduleJobRunningList(){
        List<ScheduleJob> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<ScheduleJob>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJob job = new ScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setClazz(jobKey.getClass().toString());
                job.setJobDesc("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }else if(trigger instanceof SimpleTrigger){
                    SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                    long milliseconds = simpleTrigger.getRepeatInterval();
                    job.setTimeValue((int) (milliseconds/1000));
                }
                jobList.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobList;
    }

}
