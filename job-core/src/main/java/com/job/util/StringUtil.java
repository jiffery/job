package com.job.util;

import com.job.entity.ZookConfig;
import org.apache.commons.lang.StringUtils;

/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class StringUtil extends StringUtils{

    public static String concat(String topic, String tag) {
        return topic + "_" + tag;
    }

    public static String getServiceKey(ZookConfig config) {
        return concat(config.getRegistryGroup(), concat(config.getGroup(), config.getVersion()));
    }
}
