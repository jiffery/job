package com.job.dao;


import com.job.entity.ScheduleJob;

public interface ScheduleJobDao {
    int deleteByPrimaryKey(Long scheduleJobId);

    int insert(ScheduleJob record);

    int insertSelective(ScheduleJob record);

    ScheduleJob selectByPrimaryKey(Long scheduleJobId);

    int updateByPrimaryKeySelective(ScheduleJob record);

    int updateByPrimaryKey(ScheduleJob record);
}