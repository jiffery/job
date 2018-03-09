package com.job.entity;

import java.io.Serializable;
import java.util.Date;

public class ScheduleJob implements Serializable{
    private Long scheduleJobId;

    private String jobName;

    private String jobGroup;

    private String clazz;

    private String jobStatus;

    private String cronExpression;

    private String jobText;

    private String jobParam;

    private Integer timeValue;

    private String timeType;

    private String jobDesc;

    private Long createMan;

    private Date createTime;

    private Long updateMan;

    private Date updateTime;

    public Long getScheduleJobId() {
        return scheduleJobId;
    }

    public void setScheduleJobId(Long scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz == null ? null : clazz.trim();
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    public Integer getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Integer timeValue) {
        this.timeValue = timeValue;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType == null ? null : timeType.trim();
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc == null ? null : jobDesc.trim();
    }

    public Long getCreateMan() {
        return createMan;
    }

    public void setCreateMan(Long createMan) {
        this.createMan = createMan;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateMan() {
        return updateMan;
    }

    public void setUpdateMan(Long updateMan) {
        this.updateMan = updateMan;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getJobText() {
        return jobText;
    }

    public void setJobText(String jobText) {
        this.jobText = jobText;
    }

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    @Override
    public String toString() {
        return "ScheduleJob{" +
                "scheduleJobId=" + scheduleJobId +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", clazz='" + clazz + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", jobText='" + jobText + '\'' +
                ", jobParam='" + jobParam + '\'' +
                ", timeValue=" + timeValue +
                ", timeType='" + timeType + '\'' +
                ", jobDesc='" + jobDesc + '\'' +
                ", createMan=" + createMan +
                ", createTime=" + createTime +
                ", updateMan=" + updateMan +
                ", updateTime=" + updateTime +
                '}';
    }
}