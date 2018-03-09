package com.job.entity;

import java.io.Serializable;

/**
 * @author 子羽
 * @date 2018年03月07日
 */
public class ZookConfig implements Serializable {

    private String				application;

    private String				registry;

    private String				registryGroup;

    private String				group;

    private String				version;

    private  Integer			retries;

    private Integer           timeOut;

    public String getApplication() {
        return application;
    }

    public String getRegistry() {
        return registry;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }
}
