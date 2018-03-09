package com.job.factory;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.job.entity.ZookConfig;
import com.job.service.api.JobConsumerService;
import com.job.util.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 子羽
 * @date 2018年03月07日
 */
public class DubboFactory {

    /**
     * 缓存消费对象
     */
    private static ConcurrentHashMap<String,  JobConsumerService>	servicesCache	= new ConcurrentHashMap<String,JobConsumerService>();

    /**
     * dubbo 注册中心
     */
    private static ConcurrentHashMap<String, RegistryConfig> registries		= new ConcurrentHashMap<String, RegistryConfig>();

    /**
     * dubbo 应用
     */
    private static ConcurrentHashMap<String, ApplicationConfig> applications	= new ConcurrentHashMap<String, ApplicationConfig>();


    /**
     * 初始化远程服务
     *
     * @author 子羽
     * @since 2016年11月2日
     * @param config
     * @return false 表明远程服务已经初始化
     */
    public static synchronized JobConsumerService init(ZookConfig config) {
        String group = config.getGroup();// 服务分组
        String version = config.getVersion();// 服务版本
        String serviceKey= StringUtil.getServiceKey(config);
        JobConsumerService jobService=servicesCache.get(serviceKey);
        if (jobService != null) {
            return jobService;
        }
        // 检查注册中心是否已经加载
        RegistryConfig registryConfig = registries.get(config.getRegistry()+"#"+config.getRegistryGroup());
        if (registryConfig == null) {
            registryConfig = new RegistryConfig(config.getRegistry());
            registryConfig.setGroup(config.getRegistryGroup());
            registries.put(config.getRegistry(), registryConfig);
        }

        // 检查dubbo 应用是否已经加载
        ApplicationConfig applicationConfig = applications.get(config.getApplication());
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig(config.getApplication());
            applications.put(config.getApplication(), applicationConfig);
        }

        // 实例化远程服务
        ReferenceConfig<JobConsumerService> referenceConfig = new ReferenceConfig<JobConsumerService>();
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setTimeout(config.getTimeOut());
        referenceConfig.setProtocol("dubbo");
        referenceConfig.setInterface(JobConsumerService.class);
        referenceConfig.setGroup(group);
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setVersion(version);
        referenceConfig.setRetries(config.getRetries());
        referenceConfig.setCheck(false);
        jobService = referenceConfig.get();
        // 缓存远程服务
        servicesCache.put(serviceKey, jobService);
        return jobService;
    }
}
